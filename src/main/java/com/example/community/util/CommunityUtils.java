package com.example.community.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

@Component
public class CommunityUtils {

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //md5加密
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
    public static String getJSONString(int code, String msg, Map<String,Object>map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("mag",msg);
        if (map!= null){
            for (String key: map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toString();
    }
    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

}
