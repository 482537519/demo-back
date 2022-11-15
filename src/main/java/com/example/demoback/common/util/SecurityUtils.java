package com.example.demoback.common.util;

import cn.hutool.json.JSONObject;
import com.example.demoback.common.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 获取当前登录的用户
 */
public class SecurityUtils {

    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("username", String.class);
    }

    /**
     * 获取系统用户id
     *
     * @return 系统当前登录人用户id
     */
    public static String getUserId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", String.class);
    }

    /**
     * 获取当前登录人姓名
     *
     * @return
     */
    public static String getName() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("name", String.class);
    }

    /**
     * 获取当前登录人部门类型
     */
    public static String getDeptType() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("deptType", String.class);
    }

    /**
     * 获取当前登录人部门ID
     */
    public static String getDeptId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("deptId", String.class);
    }

    /**
     * 获取当前登录用户往上追溯，最近的县公司或供电局或省公司的deptId
     */
    public static String getNearestParentDeptId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("parentDeptId", String.class);
    }


    public static String getNearestParentDeptType() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("parentDeptType", String.class);
    }

    /**
     * 获取角色标识codeList
     */
    public static List<String> getRoleCode() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("codeList", List.class);
    }
}
