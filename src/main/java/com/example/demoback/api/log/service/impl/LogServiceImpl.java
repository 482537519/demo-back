package com.example.demoback.api.log.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import com.example.demoback.api.log.aop.Log;
import com.example.demoback.api.log.dto.LogQueryCriteria;
import com.example.demoback.api.log.mapper.LogErrorMapper;
import com.example.demoback.api.log.mapper.LogSmallMapper;
import com.example.demoback.api.log.model.SysLog;
import com.example.demoback.api.log.repository.LogRepository;
import com.example.demoback.api.log.service.LogService;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.QueryHelp;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogErrorMapper logErrorMapper;

    @Autowired
    private LogSmallMapper logSmallMapper;

    private final String LOGINPATH = "login";

    @Override
    public Object queryAll(LogQueryCriteria criteria, Pageable pageable){
        criteria.setNotDescription("用户登录");
        Page<SysLog> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)),pageable);
        if ("ERROR".equals(criteria.getLogType())) {
            return PageUtil.toPage(page.map(logErrorMapper::toDto));
        }
        return page;
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<SysLog> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)),pageable);
        return PageUtil.toPage(page.map(logSmallMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String name,String ip, ProceedingJoinPoint joinPoint, SysLog log){
        log= Optional.ofNullable(log).orElse(new SysLog());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 描述
        log.setDescription(aopLog.value());
        // 获取IP地址
        log.setRequestIp(ip);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        String params = "{";
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params += " " + argNames[i] + ": " + argValues[i];
            }
        }
        if(LOGINPATH.equals(signature.getName())){
            try {
                JSONObject jsonObject = new JSONObject(argValues[0]);
                username = jsonObject.get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setMethod(methodName);
        log.setUsername(username);
        log.setName(name);
        log.setParams(params + " }");
        logRepository.save(log);
    }

    @Override
    public Object findByErrDetail(String id) {
        return Dict.create().set("exception", logRepository.findExceptionById(id));
    }
    @Override
    public void export(LogQueryCriteria criteria, HttpServletResponse response) {
        List<SysLog> list = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
        if ("ERROR".equals(criteria.getLogType())) {
            list = list.stream().filter(sysLog -> "ERROR".equals(sysLog.getLogType())).collect(Collectors.toList());
        }
        Workbook workbook = DefaultExcelBuilder.of(SysLog.class)
                .build(list);
        AttachmentExportUtil.export(workbook, "日志", response);

    }
}
