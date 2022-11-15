package com.example.demoback.common.util;


import com.example.demoback.common.exception.BadRequestException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 验证工具
 */
public class ValidationUtil {

    /**
     * 验证空
     * @param optional
     */
    public static void isNull(Optional optional, String entity,String parameter , Object value){
        if(!optional.isPresent()){
            String msg = entity
                         + " 不存在 "
                         +"{ "+ parameter +":"+ value.toString() +" }";
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     * @param string
     * @return
     */
    public static boolean isEmail(String string) {
        if (string == null){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return string.matches(regEx1);
    }

    /**
     * 是否为手机号码
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isPhone(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
