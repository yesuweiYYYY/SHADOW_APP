package com.example.shadow.setction.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shadow.R;
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment;

public class ConversationListActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conersationlist_activity);
        MyConversationFragment myConversationFragment = new MyConversationFragment();
        //设置头像尺寸
//        conversationListFragment.conversationListLayout.setAvatarSize(EaseCommonUtils.dip2px(ConversationListActivity.this, 50));
//设置头像样式：0为默认，1为圆形，2为方形(设置方形时，需要配合设置avatarRadius，默认的avatarRadius为50dp)
//        conversationListFragment.conversationListLayout.setAvatarShapeType(2);
//设置圆角半径
//        conversationListFragment. conversationListLayout.setAvatarRadius((int) EaseCommonUtils.dip2px(ConversationListActivity.this, 5));
//设置标题字体的颜色
//        conversationListFragment.conversationListLayout.setTitleTextColor(ContextCompat.getColor(ConversationListActivity.this, R.color.red));
//设置是否隐藏未读消息数，默认为不隐藏
//        conversationListFragment.conversationListLayout.hideUnreadDot(false);
//设置未读消息数展示位置，默认为左侧
//        conversationListFragment.conversationListLayout.showUnreadDotPosition(EaseConversationSetStyle.UnreadDotPosition.LEFT);

        getSupportFragmentManager().beginTransaction().add(R.id.conversationList_Frament, myConversationFragment).commit();

    }
}

