package com.example.demoback.baseModule.system.sysDept.repository;

import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DeptRepository extends JpaRepository<SysDept, String>, JpaSpecificationExecutor {

    /**
     * findByPid
     *
     * @param id
     * @return
     */
    List<SysDept> findByPid(String id);

    @Query(value = "select name from sys_dept where id = ?1", nativeQuery = true)
    String findNameById(String id);

    Set<SysDept> findByRoles_Id(String id);

    List<SysDept> findByIdIn(Collection<String> ids);

    /**
     * 子查父
     *
     * @return
     */
    @Query(value = "SELECT *  FROM SYS_DEPT where ID IN (?1) GROUP BY ID", nativeQuery = true)
    List<SysDept> findDeptTreeById(List<String> deptIds);

    /**
     * 父查子
     *
     * @param parentDeptId 父部门id
     */
    @Query(value = "SELECT * FROM SYS_DEPT where pid=?1", nativeQuery = true)
    List<SysDept> findDeptTreeByParentId(String parentDeptId);

    @Query(value = "SELECT * FROM SYS_DEPT WHERE DEPT_TYPE = ?1", nativeQuery = true)
    List<SysDept> findByDeptType(String type);

    @Query(value = "SELECT * FROM SYS_DEPT WHERE PID = ?1 AND DEPT_TYPE = ?2", nativeQuery = true)
    List<SysDept> findByDeptTypeAndPid(String pid, String type);

    /**
     * 从当前部门id，往上追溯到最高级别
     */
    @Query(value = "SELECT * FROM SYS_DEPT CONNECT BY PRIOR PID = ID START WITH ID = ?1", nativeQuery = true)
    List<SysDept> findDeptTreeByDeptId(String deptId);
}
