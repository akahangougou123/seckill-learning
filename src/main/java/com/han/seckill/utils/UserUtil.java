package com.han.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.han.seckill.pojo.User;
import com.han.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    public static void main(String[] args) throws Exception {
        CreateUser(5000);
    }
    public static void CreateUser(int count) throws Exception {
        List<User> Users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13924400000L+i);
            user.setNickname("test0000"+i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSalt()));
            Users.add(user);
        }
        System.out.println("CreateUser");

        //插入数据库
        Connection connection = getConn();
        String sql= "insert into t_user(id,nickname,password,salt,head,login_count) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i < Users.size(); i++) {
            User user = Users.get(i);
            preparedStatement.setLong(1,user.getId());
            preparedStatement.setString(2,user.getNickname());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,user.getSalt());
            preparedStatement.setString(5,"no."+i);
            preparedStatement.setInt(6,0);
            preparedStatement.addBatch();
            preparedStatement.execute();
            preparedStatement.clearParameters();
        }

        connection.close();
        System.out.println("INSERT TO DB COMPLETED");

        //登录，获取token
        String loginUrl = "http://localhost:8088/seckill/login/doLogin1";
        File file =  new File("/Users/akahandao/Desktop/config.txt");
        if(file.exists()){
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);
        for (int i = 1; i < Users.size(); i++) {
            User user = Users.get(i);
            URL url = new URL(loginUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
             httpURLConnection.setRequestMethod("POST");
             httpURLConnection.setDoOutput(true);
            OutputStream out = httpURLConnection.getOutputStream();
            String params = "mobile="+user.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=inputStream.read(bytes))>=0){
                byteArrayOutputStream.write(bytes,0,len);
            }
            inputStream.close();
            byteArrayOutputStream.close();
            String response = new String(byteArrayOutputStream.toByteArray());
            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response,RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket:"+userTicket);
            String row = user.getId()+","+userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r".getBytes());
            System.out.println("write to file:"+user.getId());
        }
        raf.close();
        System.out.println("生成完毕，文件写入完成");

    }
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&userSSL=false&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "hjj123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }
}
