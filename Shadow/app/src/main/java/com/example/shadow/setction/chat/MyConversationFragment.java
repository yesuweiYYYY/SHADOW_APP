package com.example.shadow.setction.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.shadow.orient.Getinfo;
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
                        ViewGroup viewGroup = (ViewGroup)view;
                        Log.d("ItemClick",String.valueOf(viewGroup.getChildCount()));
                        TextView tv = (TextView)viewGroup.getChildAt(2);
                        Log.d("ItemClick 2ndview.text:", tv.getText().toString());

                        Intent intent = new Intent(getActivity(),ChatActivity.class);
                        intent.putExtra("conversationId",tv.getText().toString());
                        intent.putExtra("chatType",1);
                        startActivity(intent);
//                        Log.d("Item",((TextView)view).getText().toString() );

                    }
                }
        );
    }




}
