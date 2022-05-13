package com.example.shadow.sql;
import android.os.Message;
import android.util.Log;

import com.example.shadow.ShadowApplication;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.*;

import data_struct.*;
//Gson
import com.google.gson.Gson;
import com.google.gson.Gson.*;

public class HTTPUtil {
//    private static final String host_URL = host_URL+"";
    private static final String host_URL = "http://120.26.86.113:5000/";

    public static int int_res;
    public static localtion[] locations_res;
    public static User[] user_res;
// 登录
    public int log(String username,String password){
        OkHttpClient client = new OkHttpClient();
        // 创建表单
        FormBody.Builder formBuilder= new FormBody.Builder();
        formBuilder.add("username",username);
        formBuilder.add("password",password);
        Request request = new Request.Builder().url(host_URL+"login").post(formBuilder.build()).build();


        Call call = client.newCall(request);
        int_res = 4;
        call.enqueue(new Callback() {
            //没有收到服务器应答
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("info_OKP_login", "onFailure: " + e.getMessage());
            }
            //有应答
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 输出应答,内容不详
                Headers headers = response.headers();//取出headers.
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("info_OKP_login", headers.name(i) + ":" + headers.value(i));
                }

                // res 是一个字符 1 成功 2 不存在用户名 3 密码错误 4 服务器链接失败
                final String res = response.body().string();
                Log.d("info_OKP_login", "onFailure: " +res);
                if(res.equals("1")){//登陆成功
                    int_res=1;
                }else if(res.equals("2")){
                    int_res=2;
                }else if(res.equals("3")){
                    int_res=3;
                }
            }
        });
        try {
            Thread.sleep(1000); //1000 毫秒，也就是1秒.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return int_res;
    }
// 注册
    public int register(User s){

        OkHttpClient client = new OkHttpClient();

        //form bulider
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username",s.username);
        formBuilder.add("password",s.password);
        formBuilder.add("age",s.age);
        formBuilder.add("phone",s.phone);
        formBuilder.add("description",s.descrption);
        formBuilder.add("sex",s.sex);
        formBuilder.add("image",s.image);
        Request request = new Request.Builder().url(
                host_URL+"register"
        ).post(formBuilder.build()).build();


        // okhttp post
        int_res=0;
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            //          没有收到服务器应答
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKP_register:", "onFailure: " + e.getMessage());
            }

            //          有应答
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 输出应答,内容不详
                Log.d("info", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();//取出headers.
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("info", headers.name(i) + ":" + headers.value(i));
                }
                // res 是一个数字 1:注册成功 2:注册失败 用户已存在
                final String res = response.body().string();
                Log.d("OKP_register:", res);
                if(res.equals("1"))int_res=1;
                if(res.equals("2"))int_res=2;
                if(res.equals("3"))int_res=3;
            }
        })  ;

        // 等一下线程恢复
        try {
            Thread.sleep(1000); //1000 毫秒，也就是1秒.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        return int_res;
    }

// 返回个人消息
    public localtion init_me(String username){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder= new FormBody.Builder();
        Request request = new Request.Builder().url(host_URL+"location_me/"+username).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                Log.d("OKP", "onFailure: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("OKP", "response: " + message.obj.toString());

                Gson gson = new Gson();
                locations_res = gson.fromJson(message.obj.toString(), localtion[].class);
            }
        });
        // 等一下线程恢复
        try {
            Thread.sleep(1000); //1000 毫秒，也就是1秒.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return locations_res[0];
    }
// 返回所有人的消息
    public  localtion[] init_other(String username) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder= new FormBody.Builder();
        Log.d("OKP get", "init_other");
        Request request = new Request.Builder().url(host_URL+"location_other/"+username).post(formBuilder.build()).build();
        Call call2 = client.newCall(request);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                Log.d("OKP", "onFailure: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("OKP_init_other","response: " + message.obj.toString());

                Gson gson = new Gson();
                locations_res= gson.fromJson(message.obj.toString(), localtion[].class);

            }
        });

        // 等一下线程恢复
        try {
            Thread.sleep(1000); //1000 毫秒，也就是1秒.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return locations_res;
    }

// 返回个人消息
    public  User info(String username){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder= new FormBody.Builder();
        Request request = new Request.Builder().url(host_URL+"info/"+username).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                Log.d("OKP", "onFailure: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("OKP", "response: " + message.obj.toString());

                Gson gson = new Gson();
                user_res = gson.fromJson(message.obj.toString(), User[].class);
            }
        });
        // 等一下线程恢复
        try {
            Thread.sleep(1000); //1000 毫秒，也就是1秒.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return user_res[0];
    }


}
