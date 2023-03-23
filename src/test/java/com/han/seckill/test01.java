package com.han.seckill;

import com.han.seckill.pojo.User;
import com.han.seckill.utils.MD5Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.FileAttribute;

public class test01 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName("com.han.seckill.pojo.User");
        Class<?> clazz2 = User.class;
        User user = new User();
        Class clazz3 = user.getClass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        Field[] fields3 = clazz3.getFields();

        Method[] methods = clazz.getDeclaredMethods();
        Method[] methods2 = clazz2.getDeclaredMethods();
        Method[] methods3 = clazz3.getDeclaredMethods();

        String inputpass = "2ca22cef7d9c96f77c0a50893b077e97";
        System.out.println(MD5Util.formPassToDBPass(inputpass,"1a2b3c4d"));



        System.out.println("clazz:"+clazz+",fileds:"+fields+",methods:"+methods);
        System.out.println(methods.toString());
        System.out.println("clazz:"+clazz2+",fileds:"+fields2+",methods:"+methods2);
        System.out.println();
        System.out.println("clazz:"+clazz3+",fileds:"+fields3+",methods:"+methods3);

        Method nameMethod = clazz.getMethod("setNickname",String.class);
        nameMethod.invoke(user,"han");
        System.out.println(user);
    }

}