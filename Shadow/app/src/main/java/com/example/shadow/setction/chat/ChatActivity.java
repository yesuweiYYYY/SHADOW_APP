package com.example.shadow.setction.chat;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shadow.R;
import com.example.shadow.setction.base.BaseInitActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseIM;
import com.hyphenate.easeui.modules.chat.EaseChatFragment;
import com.hyphenate.easeui.manager.EaseConfigsManager;

public class ChatActivity extends AppCompatActivity {
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.demo_activity_chat);

        EaseChatFragment chatFragment;
        chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.chat_Framelayout, chatFragment).commit();
    }

    // 开启这个act

}
