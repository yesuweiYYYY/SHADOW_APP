package com.example.shadow.Log_register;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
import com.example.shadow.sql.HTTPUtil;
import android.util.Log;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import okhttp3.*;

import com.example.shadow.EM.*;


public class Log_actvity extends AppCompatActivity implements View.OnClickListener{
    private ShadowApplication app;
    private Connection connection;
    private EditText username_edittext;
    private EditText password_edittext;
    private ResultSet resultSet;
    String username;
    String password;
    boolean b=false;
    Button register;
    Button log;
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            logtext();
        }
    };
    // 底部弹窗
    private void ToastmakeText(String str){
        Looper.prepare();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        Looper.loop();
        return ;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // 元素绑定
        super.onCreate(savedInstanceState);
        // EM 绑定 appkey
        tool.init(Log_actvity.this);

        setContentView(R.layout.login);
        register= (Button) findViewById(R.id.register_btn);
        log= (Button) findViewById(R.id.log_btn);
        register.setOnClickListener(this);
        log.setOnClickListener(this);
    }
    private void ifright(){
        username_edittext= (EditText) findViewById(R.id.logusername_text);
        username=username_edittext.getText().toString();
        password_edittext=(EditText) findViewById(R.id.logpassward_text);
        password=password_edittext.getText().toString();
        new Thread(runnable1).start();
    }

    private void getCheckFromServer(String url){
        HTTPUtil tmpclient = new HTTPUtil();
        final int res_flag = tmpclient.log(username,password);
        Log.d("LOGIN:",String.valueOf(res_flag));
        if(res_flag==1){
            //开启新的app
//            Toast.makeText(Log_actvity.this, "登录成功",Toast.LENGTH_SHORT).show();
            app= (ShadowApplication) getApplication();
            app.setUsername(username);
            //下面不知道噶干嘛的
            Message message = new Message();
            message.what = 1;
            // 发送消息到消息队列中
            handler.sendMessage(message);
        }else if(res_flag == 2){
//            Toast.makeText(Log_actvity.this, "密码错误",Toast.LENGTH_SHORT).show();
        }else if(res_flag == 3){
//            Toast.makeText(Log_actvity.this, "没有此用户",Toast.LENGTH_SHORT).show();
        }

    }
    private void logtext(){
        if(username.equals("")||password.equals("")){
            ToastmakeText("账号密码不能为空");
        }
        // 登录验证
        String url = "http://10.0.2.2:5000/user";
        getCheckFromServer(url);
    }
    // Handler异步方式下载图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    // 下载成功
                    tool.setUsername(username);
                    tool.setPassword(password);
                    tool.log();
                    Intent intent0=new Intent(Log_actvity.this,com.example.shadow.orient.MainActivity.class);
                    startActivity(intent0);

                    break;
                case -1:

                    break;
            }
        };
    };

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.log_btn:
                ifright();
                break;
            case R.id.register_btn:
                Intent intent =new Intent(Log_actvity.this,Register_activity.class);
                startActivity(intent);
                break;
        }
    }

}
