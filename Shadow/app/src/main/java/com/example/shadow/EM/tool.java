package com.example.shadow.EM;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import data_struct.User;

public class tool {
    static final String Appkey = "1136220502165613#test";
    static String Username="";
    static String password="";
    public static void init(Context context){
        EMOptions options = new EMOptions();
        options.setAppKey(Appkey);
        EMClient.getInstance().init(context, options);
    }
    public static void setUsername(String str){ Username=str; }
    public static void setPassword(String str){ password=str; }
    public static void addClient(String tmp_username,String tmp_password) throws HyphenateException {
        EMClient.getInstance().createAccount(tmp_username, tmp_password);
    }


    public static void freind_add(String toAddUsername) throws HyphenateException {
        EMClient.getInstance().contactManager().addContact(toAddUsername, "hello");
        Log.d("Friend_add",toAddUsername);
    }

    public static void log(){
        EMClient.getInstance().login(Username, password, new EMCallBack() {
            /**
             * Sign in success callback
             */
            @Override
            public void onSuccess() {
                // Login success
            }

            /**
             * Sign in failed callback
             * @param code  failed code
             * @param error failed message
             */
            @Override
            public void onError(final int code, final String error) {
                // Login error
            }

            @Override
            public void onProgress(int i, String s) {

            }

        });
    }
}
