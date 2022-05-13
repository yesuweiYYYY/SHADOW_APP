package com.example.shadow.orient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shadow.EM.tool;
import com.example.shadow.Log_register.Register_activity;
import com.example.shadow.MainActivity;
import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
import com.example.shadow.setction.chat.ChatActivity;
import com.example.shadow.sql.HTTPUtil;
import com.example.shadow.sql.SQLUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shadow.MainActivity;
import com.example.shadow.R;
import com.example.shadow.sql.SQLUtil;
import com.hyphenate.exceptions.HyphenateException;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import data_struct.User;

public class Getinfo extends AppCompatActivity {
    private Connection connection;
    private EditText info_username;
    private EditText info_age;
    private EditText info_phone;
    private EditText info_sex;
    private TextView info_description;
    private ImageView info_personal_icon;

    User user;
    String username;
    Bitmap image_bitmap;



    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            getinfo();
            Looper.loop();
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("username");
        if(getIntent().getStringExtra("own_flag").equals("1"))
            setContentView(R.layout.info);
        else
            setContentView(R.layout.info);

        init_buttun();

        new Thread(runnable).start();
    }
    /*设置按钮功能*/
    private void init_buttun(){
        Button bt = findViewById(R.id.friend_add);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tool.freind_add(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

        Button bt2 = findViewById(R.id.start_chat);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Getinfo.this, ChatActivity.class);
                intent.putExtra("conversationId",username);
                intent.putExtra("chatType",1);
                startActivity(intent);
            }
        });
    }
    private void setText(){
        Log.d("info","setText");
        info_username= (EditText) findViewById(R.id.info_username);
        info_username.setText(user.username);

        info_age= (EditText) findViewById(R.id.info_age);
        info_age.setText(user.age);
        info_phone= (EditText) findViewById(R.id.info_phone);
        info_phone.setText(user.phone);
        info_sex= (EditText) findViewById(R.id.info_sex);
        info_sex.setText(user.sex);
        info_description=(TextView)findViewById(R.id.info_description) ;
        info_description.setText(user.descrption);
//        info_personal_icon=(ImageView)findViewById(R.id.info_personal_icon);
//        info_personal_icon.setImageBitmap(image_bitmap);



    }

    // Handler异步方式下载图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ImageView imageView;
            switch (msg.what) {
                case 1:
                    // 下载成功

//                    imageView = (ImageView) findViewById(R.id.info_personal_icon);
//
//                    imageView.setImageBitmap(image_bitmap);

                    break;
                case -1:
                    // 下载失败使用默认图片
                    imageView = (ImageView) findViewById(R.id.info_personal_icon);
                    imageView.setBackgroundResource(R.drawable.default_personal_image);
                    break;
            }
        };
    };
    private void getinfo(){
            HTTPUtil httpUtil = new HTTPUtil();
            user = httpUtil.info(username);
            image_bitmap=stringToBitmap(user.image);
            Message message = new Message();
            message.what = 1;
            // 发送消息到消息队列中
            handler.sendMessage(message);
            setText();


    }

    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }


//    @Override
//    public void onClick(View view) {
//        Log.d("onClick","click_id:"+String.valueOf(view.getId()));
//        switch (view.getId()){
//            case R.id.friend_add:
//                try {
//                    tool.freind_add(username);
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }

}
