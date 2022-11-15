package com.example.demoback.interceptor;

import com.example.demoback.common.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 文件上传拦截器
 */
public class FileHeaderCheckInterceptor implements HandlerInterceptor {
 
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 判断是否为文件上传请求
        if (request != null && request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files = multipartRequest.getFileMap();
            Iterator<String> iterator = files.keySet().iterator();
            while (iterator.hasNext()) {
                String formKey = (String) iterator.next();
                MultipartFile multipartFile = multipartRequest.getFile(formKey);
                byte[] file = multipartFile.getBytes() ;
                
                // 获取字节流前8字节
                int HEADER_LENGTH = 8 ;
                
                if(file.length>HEADER_LENGTH){
                    // 字节数组转成16进制
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<HEADER_LENGTH;i++){
                        int v = file[i] & 0xFF;     
                        String hv = Integer.toHexString(v);     
                        if (hv.length() < 2) {     
                            sb.append(0);     
                        }     
                        sb.append(hv);
                    }
                    boolean isFound = false ;
                    String fileHead = sb.toString().toUpperCase() ;
                    List<String> headerList = FileHeaderHelper.getInstance().getHeaderList() ;
                    for(String header : headerList){
                        if(fileHead.startsWith(header)){
                            isFound = true ;
                            break ;
                        }
                    }
                    if(!isFound){
                        throw new BadRequestException("上传文件类型不符合！");
                    }
                }else {
                    throw new BadRequestException("上传文件有异常，已被系统禁止！");
                }
            }
        }
        return true;
    }
}