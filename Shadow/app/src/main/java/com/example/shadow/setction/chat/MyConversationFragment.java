package com.example.shadow.setction.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hyphenate.easeui.interfaces.OnItemClickListener;
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment;
import com.hyphenate.easeui.modules.conversation.EaseConversationListLayout;

public class MyConversationFragment extends EaseConversationListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.conversationListLayout.setOnItemClickListener(
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

//                        Log.d("Item",((TextView)view).getText().toString() );

                    }
                }
        );
    }




}
