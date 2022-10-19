package com.han.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    private static final String salt="1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    //第一次加密
    public static String inputPassToFormPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    //第二次加密
    public static String formPassToDBPass(String formPass,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    //真实登录时，将用户输入的密码两次加密后与数据库获取的密码相比较是否相同，相同则密码正确
    public static String inputPassToDBPass(String inputPass,String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass,salt);
        return dbPass;
    }

    public static void main(String[] args){
        //99123456c3 => d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
