package com.example.demoback.baseModule.test;

import com.example.demoback.api.log.aop.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjq
 * @create 2022-09-30 11:04
 */

@RestController
@RequestMapping("/sys")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Log("-----------------------123---")
    @RequestMapping("/test")
    public String test(){
        logger.info("测试接口~~~~~~~~~~~");
        return "success~~~~";
    }
}
