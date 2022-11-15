package com.example.demoback.interceptor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 读取文件流头信息
 */
public class FileHeaderHelper {    
    private static FileHeaderHelper me ;
    private static List<String> headerList ;
    private FileHeaderHelper(){}    
    public static FileHeaderHelper getInstance(){
        if(me == null){
            me = new FileHeaderHelper();
        }
        return me ;
    }
    
    public List<String> getHeaderList(){
        if(headerList == null){
            headerList = new ArrayList<String>() ;
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String classpathResource = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/checkFileHeader.properties";
            Properties p = new Properties();
            try {
                Resource[] res = resolver.getResources(classpathResource) ;
                for (Resource re : res) {
                    p.load(re.getInputStream());
                    break ;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Map.Entry<Object, Object> item : p.entrySet()) {
                headerList.add(item.getValue().toString()) ;
            }
        }        
        return headerList ;
    }
}