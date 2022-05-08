package com.example.shadow.setction.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shadow.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.util.EMLog;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    public BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        clearFragmentsBeforeCreate();;
    }

    /**
     * 初始化toolbar
     * @param toolbar
     */
    public void initToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//有返回
            getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示title
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * 设置返回按钮的颜色
     * @param mContext
     * @param colorId
     */
    public static void setToolbarCustomColor(AppCompatActivity mContext, int colorId) {
        Drawable leftArrow = ContextCompat.getDrawable(mContext, R.drawable.abc_ic_ab_back_material);
        if(leftArrow != null) {
            leftArrow.setColorFilter(ContextCompat.getColor(mContext, colorId), PorterDuff.Mode.SRC_ATOP);
            if(mContext.getSupportActionBar() != null) {
                mContext.getSupportActionBar().setHomeAsUpIndicator(leftArrow);
            }
        }
    }

    @Override
    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null&&getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                super.onBackPressed();
            }else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }

    }


    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null&&getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }

        return super.onTouchEvent(event);
    }
    /**
     * 处理因为Activity重建导致的fragment叠加问题
     */
    public void clearFragmentsBeforeCreate(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() == 0){
            return ;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment: fragments){
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commitNow();
    }
}
