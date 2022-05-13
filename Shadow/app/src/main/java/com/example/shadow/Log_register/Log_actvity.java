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
    private EditText username_edittext;
    private EditText password_edittext;
    String username;
    String password;
    Button register;
    Button log;
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            logtext();
            Looper.loop();
        }
    };

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
        username_edittext = (EditText) findViewById(R.id.logusername_text);
        username=username_edittext.getText().toString();
        password_edittext = (EditText) findViewById(R.id.logpassward_text);
        password=password_edittext.getText().toString();
        new Thread(runnable1).start();
    }

    private void getCheckFromServer(){
        HTTPUtil tmpclient = new HTTPUtil();
        final int res_flag = tmpclient.log(username,password);
        Log.d("LOGIN:",String.valueOf(res_flag));
        if(res_flag==1){
            //开启新的app
            Toast.makeText(Log_actvity.this, "登录成功",Toast.LENGTH_SHORT).show();
            app= (ShadowApplication) getApplication();
            app.setUsername(username);
            // 发送消息到消息队列中
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }else if(res_flag == 3){
            Toast.makeText(Log_actvity.this, "密码错误",Toast.LENGTH_SHORT).show();
        }else if(res_flag == 2){
            Toast.makeText(Log_actvity.this, "没有此用户",Toast.LENGTH_SHORT).show();
        }else if(res_flag == 4){
            Toast.makeText(Log_actvity.this, "服务器链接失败",Toast.LENGTH_SHORT).show();
        }

    }
    private void logtext(){
        if(username.equals("")||password.equals("")){
            Toast.makeText(Log_actvity.this, "账户密码不能为空",Toast.LENGTH_SHORT).show();
            return ;
        }
        // 登录验证
        getCheckFromServer();
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
