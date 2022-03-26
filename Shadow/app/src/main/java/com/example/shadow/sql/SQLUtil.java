//连接数据库用
package com.example.shadow.sql;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;

/**
 * Created by lenovo on 2018/6/15.
 */

public class SQLUtil {
    //连接数据库
    private static final String URL = "jdbc:mysql://rm-bp151716jpy7k5711zo.mysql.rds.aliyuncs.com:3306/ryxs?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789yY";
//    建立连接，返回一个con 对象
    public static Connection openConnection(){
        Connection con=null;
        Log.d("connecting","连接数据库");
        try{

            Class.forName("com.mysql.jdbc.Driver");
            Log.d("加载驱动", "完成");
            //引用代码此处需要修改，address为数据IP，Port为端口号，DBName为数据名称，UserName为数据库登录账户，Password为数据库登录密码
            Log.d("connect",URL+ USER+ PASSWORD);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.d("connect","数据库连接成功");
        } catch (ClassNotFoundException e) {
            Log.e("connect","连接失败1");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("connect","连接失败2");
            e.printStackTrace();
        }
        return con;
    }
    //执行数据库语句，并且将结果返回


    public static ResultSet query(Connection conn, String sql){
//  建立连接
        if(conn==null){
            Log.d("connect","连接失败");
            return null;
        }
//        SQL 运行结果
        Statement statement=null;
//        查询结果
        ResultSet result=null;
        try {

            statement=conn.createStatement();
            result=statement.executeQuery(sql);

            if(result!=null&&result.first()){
                Log.d("sql","sql执行成功");
                return result;
            }else{
                Log.d("sql","sql查询为空");
            }
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }
    //仅执行数据库操作，返回的结果仅为该语句是否执行成功 不知道干嘛的
    public static boolean execSQL(Connection conn,String sql){
        boolean execResult=false;
        if(conn==null){
            return execResult;
        }
        Statement statement=null;
        try {
            statement=conn.createStatement();
            if(statement!=null){
                execResult=statement.execute(sql);
                Log.e("execute","sql语句执行成功"+execResult);
            }
        } catch (SQLException e) {
            execResult=false;
        }
        return execResult;
    }
    public static int execUpdate(Connection connection,String sql){
        if(connection==null){
            return -1;
        }
        Statement statement=null;
        int results=0;
        try {
            statement=connection.createStatement();
            results=statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  results;
    }
    public static boolean isExecUpdate(Connection connection,String sql){
        if(connection==null){
            Log.d("connect","连接失败");
            return false;
        }
        Statement statement=null;
        try {
            statement=connection.createStatement();
            statement.executeUpdate(sql);
            Log.d("sql","sql执行成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d("sql","sql执行失败");
        return false;
    }

}
