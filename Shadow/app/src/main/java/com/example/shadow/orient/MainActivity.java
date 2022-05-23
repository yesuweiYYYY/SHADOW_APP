package com.example.shadow.orient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shadow.Log_register.Register_activity;
import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
import com.example.shadow.setction.chat.ChatActivity;
import com.example.shadow.setction.chat.ConversationListActivity;
import com.example.shadow.sql.HTTPUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Arrays;

import data_struct.*;


public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {

    private String username="2";

    private ArrayList<localtion> locainfo_other=new ArrayList<localtion>();
    private localtion localtion_me=new localtion();

    private boolean locainfoupdate=false;
    Bitmap image_bitmap;
    private int view_width=0,view_height=0;
    //页面旋转角度 支持 [0,1,2,3] 对应 [0,90,180,270]
    private int viewtr =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_main_layout();
//        setContent(bt);
//        contain.addView(testImageView);

    }

    protected void init_main_layout(){
        Log.d("init_main_layout","tr:"+String.valueOf(viewtr));
        setContentView(R.layout.main_layout);
        locainfoupdate=false;
        init();
        init_gridlayout();
        Button bt=findViewById(R.id.spin_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewtr=(viewtr+1)%4;
                init_main_layout();
            }
        });

        Button bt2 = findViewById(R.id.ConversationList_bt);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);

                startActivity(intent);
            }
        });

        Button bt3 = findViewById(R.id.ChattingRoom_bt);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("conversationId","182368431308801");
                intent.putExtra("chatType",2);
                startActivity(intent);
            }
        });
    }

    protected void init_gridlayout(){
        // 初始化布局
        // 容器
        GridLayout container =findViewById(R.id.container);
        // layout 配置器
        GridLayout.LayoutParams LP=new GridLayout.LayoutParams();
        LP.height=150;LP.width=150;

        // 绘制 图片
        // 基站
        ImageView base_station=new ImageView(MainActivity.this);
        base_station.setImageResource(R.mipmap.base);
        if(viewtr==0){ LP.rowSpec = GridLayout.spec(0);LP.columnSpec=GridLayout.spec(9); }
        else if(viewtr==1){ LP.rowSpec = GridLayout.spec(9);LP.columnSpec=GridLayout.spec(9); }
        else if(viewtr==2){ LP.rowSpec = GridLayout.spec(9);LP.columnSpec=GridLayout.spec(0); }
        else if(viewtr==3){ LP.rowSpec = GridLayout.spec(0);LP.columnSpec=GridLayout.spec(0); }
        base_station.setLayoutParams(LP);
        container.addView(base_station);
        int[][] vis=new int[13][9];
        for(int i=0;i<13;i++)for(int j=0;j<9;j++)vis[i][j]=-1;
        for(int i=0;i<locainfo_other.size();i++){
            int x=(int)(Math.round(locainfo_other.get(i).x));
            int y=(int)(Math.round(locainfo_other.get(i).y));
            Log.d("locations:","x-"+String.valueOf(x)+",y-"+String.valueOf(y));
            if(x>=0&&x<9&&y>=0&&y<=9){
                if(viewtr==0)vis[x][y]=i;
                else if(viewtr==1)vis[y][9-x]=i;
                else if(viewtr==2)vis[9-x][9-y]=i;
                else if(viewtr==3)vis[9-y][x]=i;
            }

        }

        {
            int x=(int)(Math.round(localtion_me.x));
            int y=(int)(Math.round(localtion_me.y));
            if(x>=0&&x<9&&y>=0&&y<=9) {
                if(viewtr==0)vis[x][y]=-2;
                else if(viewtr==1)vis[y][9-x]=-2;
                else if(viewtr==2)vis[9-x][9-y]=-2;
                else if(viewtr==3)vis[9-y][x]=-2;
            }
        }

        for(int i=1;i<10;i++){
            for(int j=0;j<9;j++){
//                Params 配置器
                GridLayout.LayoutParams LP2=new GridLayout.LayoutParams();
                LP2.height=100;
                LP2.width=100;
                LP2.rowSpec=GridLayout.spec(i);LP2.columnSpec=GridLayout.spec(j);
//              配置
                Button bt=new Button(MainActivity.this);
                bt.setBackgroundResource(R.drawable.shape_circle_blue);
                bt.setLayoutParams(LP2);
                bt.setVisibility(View.INVISIBLE);
//              绑定点击事件
                if(vis[i][j]!=-1){
                    bt.setVisibility(View.VISIBLE);
                    Log.d("make_click_buttun","x-"+String.valueOf(i)+"y-"+String.valueOf(j));
                    if(vis[i][j]==-2){
                        bt.setBackgroundResource(R.drawable.shape_circle_green);
                        bt.setText(localtion_me.username.substring(0,1));
                    }else{
                        bt.setBackgroundResource(R.drawable.shape_circle_blue);
                        bt.setText(locainfo_other.get(vis[i][j]).username.substring(0,1));
                    }
                    int finalI = i;
                    int finalJ = j;
                    if(vis[i][j]!=-2){
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent0=new Intent(MainActivity.this,Getinfo.class);
                                intent0.putExtra("username",locainfo_other.get(vis[finalI][finalJ]).username);
                                intent0.putExtra("own_flag","1");
                                startActivity(intent0);
                            }
                        });

                    }

                }

                container.addView(bt);
            }
        }
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
        init_me();
        init_other();
//        new Thread(runnable1).start();
    }

    public void init_me(){
        HTTPUtil tmp_http= new HTTPUtil();
        localtion_me = tmp_http.init_me(username);
    }
    public void init_other(){
        HTTPUtil tmp_http= new HTTPUtil();

        data_struct.localtion[] other_loc = tmp_http.init_other(username);
//        locainfo_other.clear();
        for(data_struct.localtion i:other_loc){
            Log.d("init_other",i.toString());
            locainfo_other.add(i);
        }
    }

    Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            getlocationinfo();
            Looper.loop();
        }
    };

    //orient 表示o1与o正北方向夹角
    //distance 表示两者之间的距离
    private void getlocationinfo(){
        init_me();
        init_other();
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




        if(!locainfoupdate){
            locainfoupdate=true;

            for(localtion i:locainfo_other){
                Log.d("ssssssssssssssize", "   num"+"  "+i.x+"   "+i.y);
                i.x=(i.x)*100+location[0];
                i.y=location[1]-i.y*100;
                double t=i.x;i.x=i.y;i.y=t;
                Log.d("ssssssssssssssize", "   num"+"  "+i.x+"   "+i.y);
            }
        }
        for(localtion i:locainfo_other)
            Log.d("ssssssssssssssize", "   num"+"  "+i.x+"   "+i.y);


    }


    @Override
    public void onSensorChanged(SensorEvent event) {

//        cView.setDegree(event.values[0]);
        neighborloc();

//        cView.setLoca_indo(this.locainfo_other);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sManager.unregisterListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("onClick","click_id:"+String.valueOf(view.getId()));
        switch (view.getId()){
            case R.id.main_getinfo:
                Intent intent0=new Intent(MainActivity.this,Getinfo.class);
                intent0.putExtra("username",username);
                intent0.putExtra("own_flag","1");
                startActivity(intent0);
                break;
            case R.id.main_setinfo:
                    Intent intent =new Intent(MainActivity.this, Register_activity.class);
                    startActivity(intent);
                break;
            case R.id.spin_bt:
                    viewtr=(viewtr+1)%4;
//                    init_main_layout();
                break;
        }
    }

}
