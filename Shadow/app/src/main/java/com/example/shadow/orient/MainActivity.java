package com.example.shadow.orient;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shadow.Log_register.Log_actvity;
import com.example.shadow.Log_register.Register_activity;
import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
import com.example.shadow.sql.HTTPUtil;
import com.example.shadow.sql.SQLUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.*;

import com.google.gson.Gson;
import com.google.gson.Gson.*;

import data_struct.*;


public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {

    private CompassView cView;
    private SensorManager sManager;
    private Sensor mSensorOrientation;
    private String username="11";
    private Connection connection;
    private ResultSet resultSet;
    double orient_result;
    double distance_result;
    double x_result;
    double y_result;
    private ArrayList<Double> xs=new ArrayList<>();
    private ArrayList<Double> ys=new ArrayList<>();
    private ArrayList<double[]> locofphone=new ArrayList<>();
    private float ori=0;
    Bitmap image_bitmap;
    private int view_width=0,view_height=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();
//       ?
        cView = new CompassView(MainActivity.this);

        cView.setUsername(username);
        cView.post(new Runnable() {
            @Override
            public void run() {
                view_width=cView.getWidth(); // 获取宽度
                view_height=cView.getHeight(); // 获取高度
                Log.d("wwwwwwwwwwwwww",view_width+"       "+view_height);
            }
        });
//  ?
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorOrientation = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);

        LinearLayout ll = (LinearLayout)findViewById(R.id.container);
        ll.addView(cView);

          //setContentView(cView);

    }

    public void init(){
        //得到指向标位置
//        ImageView t = null;
//        t = (ImageView)findViewById(R.id.beacon_icon);
 //       int[] location = new int[2];
//        t.getLocationOnScreen(location);

        ShadowApplication app=(ShadowApplication)getApplication();
        username=app.getUsername();


        Button getinfo=(Button)findViewById(R.id.main_getinfo);
        getinfo.setOnClickListener(this);
        Button setinfo=(Button)findViewById(R.id.main_getinfo);
        setinfo.setOnClickListener(this);

        new Thread(runnable1).start();
    }

    public void init_me(){

        HTTPUtil tmp_http= new HTTPUtil();
        data_struct.localtion me_loc = tmp_http.init_me(username);
        orient_result=me_loc.x;distance_result=me_loc.y;

    }
    public void init_other(){
        HTTPUtil tmp_http= new HTTPUtil();

        data_struct.localtion[] other_loc = tmp_http.init_other(username);

        for(data_struct.localtion i:other_loc){
            xs.add(i.x);ys.add(i.y);
        }

    }

    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            getlocationinfo();
            getinfo();
            Looper.loop();
        }
    };

    //orient 表示o1与o正北方向夹角
    //distance 表示两者之间的距离
    private void getlocationinfo(){
        init_me();
        init_other();
    }
    private void getinfo(){
        connection= SQLUtil.openConnection();

        String sql="select image from user where username='"+username+"'";
        resultSet=SQLUtil.query(connection,sql);
        if(resultSet==null) {
            //Toast.makeText(MainActivity, "不存在此用户名", Toast.LENGTH_SHORT).show();
            Log.d("sql","location查询结果为空");
            return ;
        }else{
            Log.d("sql","location ok!");
        }

        try {

            String imagestring=resultSet.getString("image");
            image_bitmap=stringToBitmap(imagestring);
            Message message = new Message();
            message.what = 1;
            // 发送消息到消息队列中
            handler.sendMessage(message);

        } catch (SQLException e) {
            Message message = new Message();
            message.what = -1;
            handler.sendMessage(message);
            e.printStackTrace();
            e.printStackTrace();
        }



    }
    // Handler异步方式下载图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ImageView imageView;
            switch (msg.what) {
                case 1:
                    // 下载成功

                    imageView = (ImageView) findViewById(R.id.main_personal_icon);

                    imageView.setImageBitmap(image_bitmap);

                    break;
                case -1:
                    // 下载失败使用默认图片
                    imageView = (ImageView) findViewById(R.id.main_personal_icon);
                    imageView.setBackgroundResource(R.drawable.default_personal_image);
                    break;
            }
        };
    };
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
//    //100像素1m

    //左右五米，前方16m
    private void neighborloc(){


        int[] location = new int[2];
//        屏幕中间
        Log.d("d",String.valueOf(view_width));
        Log.d("d",String.valueOf(view_height));
        location[0]=view_width/2;
        location[1]=view_height;

        locofphone.clear();
        for(int i=0;i<xs.size();i++){
            double[] d = new double[2];
            d[1]=(xs.get(i)-x_result)*100+location[0];
            d[0]=+location[1]-(ys.get(i)-y_result)*100;
            if(d[1]>0){
                locofphone.add(d);
                Log.d("ssssssssssssssize", "   num"+"  "+d[0]+"   "+d[1]);
            }
        }



    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        cView.setDegree(event.values[0]);
        ori=event.values[0];
        neighborloc();
        //Log.d("hua","iiiii"+event.values[0]+"   "+locofphone.size());
        cView.setLocofphone(locofphone);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_getinfo:
                    //Intent intent0=new Intent(Log_actvity.this,MainActivity.class);
                    //intent0.putExtra("username",username);
                    Intent intent0=new Intent(MainActivity.this,Getinfo.class);
                    startActivity(intent0);
                break;
            case R.id.main_setinfo:
                Intent intent =new Intent(MainActivity.this, Register_activity.class);
                startActivity(intent);
                break;
        }
    }
}
