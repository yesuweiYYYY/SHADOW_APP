package com.example.shadow.Log_register;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.example.shadow.sql.SQLUtil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    //连接数据库
    String decrypt(String p,int num){
        int t=0;
        char c;
        String s="";
        while(t<num){
            c= (char) (p.charAt(t)^'!');
            s+=c;
            t++;
        }
        return s;
    }
    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            logtext();
            Looper.loop();
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void logtext(){
        String password1="";
        connection= SQLUtil.openConnection();
        String sql="select password from user where username='"+username+"'";
        resultSet=SQLUtil.query(connection,sql);
        if(resultSet==null) {
            Toast.makeText(this, "不存在此用户名", Toast.LENGTH_SHORT).show();
            return ;
        }
        try {
            int passwordindex=resultSet.findColumn("password");
            password1=resultSet.getString(passwordindex);
        } catch (SQLException e) {
           e.printStackTrace();
        }
        if(!password.equals("")) {
            String s;
            s=decrypt(password,password.length());
            if (password1.equals(s)){
                b=true;
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                app= (ShadowApplication) getApplication();
                app.setUsername(username);
                Message message = new Message();
                message.what = 1;
                // 发送消息到消息队列中
                handler.sendMessage(message);
            } else{
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }


    }
    // Handler异步方式下载图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    // 下载成功
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
