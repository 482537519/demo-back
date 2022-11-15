package com.example.demoback.baseModule.system.sysDept.service.impl;

import com.example.demoback.baseModule.system.sysDept.dto.DeptDTO;
import com.example.demoback.baseModule.system.sysDept.dto.DeptQueryCriteria;
import com.example.demoback.baseModule.system.sysDept.mapper.DeptMapper;
import com.example.demoback.baseModule.system.sysDept.model.SysDept;
import com.example.demoback.baseModule.system.sysDept.repository.DeptRepository;
import com.example.demoback.baseModule.system.sysDept.service.DeptService;
import com.example.demoback.common.exception.BadRequestException;
import com.example.demoback.common.util.KeyWord;
import com.example.demoback.common.util.QueryHelp;
import com.example.demoback.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<DeptDTO> queryAll(DeptQueryCriteria criteria) {
        return deptMapper.toDto(deptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public DeptDTO findById(String id) {
        Optional<SysDept> dept = deptRepository.findById(id);
        ValidationUtil.isNull(dept, "Dept", "id", id);
        return deptMapper.toDto(dept.get());
    }

    @Override
    public List<DeptDTO> findByIds(Collection<String> ids) {
        return deptMapper.toDto(deptRepository.findByIdIn(ids));
    }

    @Override
    public List<SysDept> findByPid(String pid) {
        return deptRepository.findByPid(pid);
    }

    @Override
    public Set<SysDept> findByRoleIds(String id) {
        return deptRepository.findByRoles_Id(id);
    }

    @Override
    public List<DeptDTO> findDepTreeById(List<String> deptIds) {
        return deptMapper.toDto(deptRepository.findDeptTreeById(deptIds));
    }

    @Override
    public List<DeptDTO> findDepTreeByParentId(String pid) {
        return deptMapper.toDto(deptRepository.findDeptTreeByParentId(pid));
    }

    @Override
    public Object buildTree(List<DeptDTO> deptDTOS) {
        deptDTOS.sort(Comparator.comparingInt(m -> m.getSort().intValue()));
        Set<DeptDTO> trees = new LinkedHashSet<>();
        Set<DeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDTOS.stream().map(DeptDTO::getName).collect(Collectors.toList());
        Boolean isChild;
        for (DeptDTO deptDTO : deptDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (DeptDTO it : deptDTOS) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<DeptDTO>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild)
                depts.add(deptDTO);
            else if (!deptNames.contains(deptRepository.findNameById(deptDTO.getPid())))
                depts.add(deptDTO);
        }

        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }

        Integer totalElements = deptDTOS.size();

        Map map = new HashMap();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? deptDTOS : trees);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeptDTO create(SysDept resources) {
        //设置部门路径
        String deptId = KeyWord.getKeyWordTime();
        Optional<SysDept> parentDept = deptRepository.findById(resources.getPid());
        if (parentDept.isPresent()) {
            String deptPath = parentDept.get().getDeptPath();
            resources.setDeptPath(deptPath + "/" + deptId);
        } else {
            resources.setDeptPath(deptId);
        }
        resources.setId(deptId);
        return deptMapper.toDto(deptRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDept resources) {
        if (resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Optional<SysDept> optionalDept = deptRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalDept, "Dept", "id", resources.getId());
        SysDept dept = optionalDept.get();
        resources.setId(dept.getId());
        //设置部门路径
        Optional<SysDept> parentDept = deptRepository.findById(resources.getPid());
        if (parentDept.isPresent()) {
            String deptPath = parentDept.get().getDeptPath();
            resources.setDeptPath(deptPath + "/" + resources.getId());
        } else {
            resources.setDeptPath(resources.getId());
        }
        deptRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        deptRepository.deleteById(id);
    }


    @Override
    public List<DeptDTO> findDeptDataByType(String type) {
        List<DeptDTO> deptDTOS = new ArrayList<>();
        deptDTOS = deptMapper.toDto(deptRepository.findByDeptType(type));
        return deptDTOS;
    }
}
