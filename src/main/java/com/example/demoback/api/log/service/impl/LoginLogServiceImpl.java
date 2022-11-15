package com.example.demoback.api.log.service.impl;

import com.example.demoback.api.log.dto.LoginLogQueryCriteria;
import com.example.demoback.api.log.model.SysLoginLog;
import com.example.demoback.api.log.repository.LoginLogRepository;
import com.example.demoback.api.log.service.LoginLogService;
import com.example.demoback.api.security.security.JwtUser;
import com.example.demoback.api.security.security.OnlineUser;
import com.example.demoback.baseModule.system.sysDept.repository.DeptRepository;
import com.example.demoback.baseModule.system.sysUser.repository.UserRepository;
import com.example.demoback.common.util.EncryptUtils;
import com.example.demoback.common.util.QueryHelp;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeptRepository deptRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(JwtUser jwtUser, OnlineUser onlineUser) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setSysDept(deptRepository.findById(jwtUser.getDeptId()).get());
        sysLoginLog.setSysUser(userRepository.findById(jwtUser.getId()).get());
        sysLoginLog.setTokenKey(onlineUser.getKey());
        sysLoginLog.setIp(onlineUser.getIp());
        sysLoginLog.setBrowser(onlineUser.getBrowser());
        loginLogRepository.save(sysLoginLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upLogoutTime(String token) {
        loginLogRepository.upLogoutTime(buildTokenKey(token), new Timestamp(System.currentTimeMillis()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upToken(String oldToken, String newToken) {
        loginLogRepository.upToken(buildTokenKey(oldToken), buildTokenKey(newToken));
    }

    @Override
    public Object queryAll(LoginLogQueryCriteria criteria, Pageable pageable) {
        Page<SysLoginLog> page = loginLogRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByUserId(String userId) {
        return loginLogRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByDeptId(String deptId) {
        return loginLogRepository.deleteByDeptId(deptId);
    }

    @Override
    public void export(LoginLogQueryCriteria criteria, HttpServletResponse response) {
        List<SysLoginLog> list = loginLogRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
        Stream<SysLoginLog> sysLoginLogStream = list.stream().map(sysLoginLog -> {
            sysLoginLog.setDeptName(sysLoginLog.getSysDept().getName());
            sysLoginLog.setUserName(sysLoginLog.getSysUser().getName());
            return sysLoginLog;
        });
        Workbook workbook = DefaultExcelBuilder.of(SysLoginLog.class)
                .build(sysLoginLogStream.collect(Collectors.toList()));
        AttachmentExportUtil.export(workbook, "登录日志", response);
    }
    private String buildTokenKey(String token) {
        String tokenKey = null;
        try {
            tokenKey = EncryptUtils.desEncrypt(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenKey;

    }
}
