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

import com.example.shadow.MainActivity;
import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
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

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

public class Getinfo extends AppCompatActivity {
    private Connection connection;
    private EditText info_username;
    private EditText info_age;
    private EditText info_phone;
    private EditText info_sex;
    private TextView info_description;
    private ResultSet resultSet;
    String username;
    String age;
    String phone;
    String description;
    String sex;
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
        setContentView(R.layout.info);
        init();
        new Thread(runnable).start();


    }
    private void init(){
        ShadowApplication app=(ShadowApplication)getApplication();
        username=app.getUsername();
    }
    private void setText(){
        Log.d("info","setText");
        info_username= (EditText) findViewById(R.id.info_username);
        info_username.setText(username);

        info_age= (EditText) findViewById(R.id.info_age);
        info_age.setText(age);
        info_phone= (EditText) findViewById(R.id.info_phone);
        info_phone.setText(phone);
        info_sex= (EditText) findViewById(R.id.info_sex);
        info_sex.setText(sex);
        info_description=(TextView)findViewById(R.id.info_description) ;
        info_description.setText(description);





    }
    // Handler????????????????????????
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ImageView imageView;
            switch (msg.what) {
                case 1:
                    // ????????????

                    imageView = (ImageView) findViewById(R.id.info_personal_icon);

                    imageView.setImageBitmap(image_bitmap);

                    break;
                case -1:
                    // ??????????????????????????????
                    imageView = (ImageView) findViewById(R.id.info_personal_icon);
                    imageView.setBackgroundResource(R.drawable.default_personal_image);
                    break;
            }
        };
    };
    private void getinfo(){
        connection= SQLUtil.openConnection();

        String sql="select sex,age,phone,description,image from user where username='"+username+"'";
        resultSet=SQLUtil.query(connection,sql);
        if(resultSet==null) {
            //Toast.makeText(MainActivity, "?????????????????????", Toast.LENGTH_SHORT).show();
            Log.d("sql","location??????????????????");
            return ;
        }else{
            Log.d("sql","location ok!");
        }

        try {
            sex=resultSet.getString("sex");
            age=resultSet.getString("age");
            phone=resultSet.getString("phone");
            description=resultSet.getString("description");
            String imagestring=resultSet.getString("image");
            image_bitmap=stringToBitmap(imagestring);
            Message message = new Message();
            message.what = 1;
            // ??????????????????????????????
            handler.sendMessage(message);

        } catch (SQLException e) {
            Message message = new Message();
            message.what = -1;
            handler.sendMessage(message);
            e.printStackTrace();
            e.printStackTrace();
        }
        setText();


    }

    public Bitmap stringToBitmap(String string) {
        // ?????????????????????Bitmap??????
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


}
