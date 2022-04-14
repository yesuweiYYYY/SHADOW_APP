package com.example.shadow.orient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shadow.R;
import com.example.shadow.ShadowApplication;
import com.example.shadow.sql.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data_struct.*;
/**
 * Created by Jay on 2015/11/14 0014.
 */
public class CompassView extends View implements Runnable{

    private ArrayList<localtion> loca_info=new ArrayList<>();
    private ArrayList<double[]> locofphone=new ArrayList<>();

    private String username="null";
    private Paint mTextPaint;
    private int sWidth,sHeight;
    private float dec = 0.0f;
    private String msg  = "正北 0°";


    public CompassView(Context context) {
        this(context, null);
    }
// 设置宽高
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sWidth = ScreenUtil.getScreenW(context);
        sHeight = ScreenUtil.getScreenH(context);

        init();

        new Thread(this).start();

    }


    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ArrayList<double[]> getLocofphone() {
        return locofphone;
    }

    public void setLocofphone(ArrayList<double[]> locofphone) {
        this.locofphone = locofphone;
    }
    public void setLoca_indo(ArrayList<localtion> loco_info) {
        this.loca_info = loco_info;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private void init() {

        mTextPaint = new Paint();

        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(64);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawText(msg, sWidth * 1/3 , sWidth*4/3, mTextPaint);
        canvas.drawCircle(1080,1622,40,mTextPaint);

        for (localtion i:loca_info)
            canvas.drawCircle((float) i.y,(float)i.x, 30,mTextPaint);

        if (locofphone != null) {
            for (int i = 0; i < locofphone.size(); i++) {
                canvas.drawCircle((float) locofphone.get(i)[0], (float) locofphone.get(i)[1], 30, mTextPaint);
            }
        }


    }

    // 更新指南针角度
    public void setDegree(float degree)
    {
        // 设置灵敏度
        if(Math.abs(dec - degree) >= 2 )
        {
            dec = degree;
            int range = 22;
            String degreeStr = String.valueOf(dec);

            // 指向正北
            if(dec > 360 - range && dec < 360 + range)
            {
                msg = "正北 " + degreeStr + "°";
            }

            // 指向正东
            if(dec > 90 - range && dec < 90 + range)
            {
                msg = "正东 " + degreeStr + "°";
            }

            // 指向正南
            if(dec > 180 - range && dec < 180 + range)
            {
                msg = "正南 " + degreeStr + "°";
            }

            // 指向正西
            if(dec > 270 - range && dec < 270 + range)
            {
                msg = "正西 " + degreeStr + "°";
            }

            // 指向东北
            if(dec > 45 - range && dec < 45 + range)
            {
                msg = "东北 " + degreeStr + "°";
            }

            // 指向东南
            if(dec > 135 - range && dec < 135 + range)
            {
                msg = "东南 " + degreeStr + "°";
            }

            // 指向西南
            if(dec > 225 - range && dec < 225 + range)
            {
                msg = "西南 " + degreeStr + "°";
            }

            // 指向西北
            if(dec > 315 - range && dec < 315 + range)
            {
                msg = "西北 " + degreeStr + "°";
            }
        }
    }


    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            postInvalidate();
        }
    }



}
