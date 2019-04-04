package com.jin10.chatnow;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int _LEFT =0;
    public static final int _RIGHT =1;

    private Context context;
    private List<Chat> chat;

    FirebaseUser users;

    public MessageAdapter(Context _context, List<Chat> _chat) {
        context = _context;
        chat = _chat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chats = chat.get(i);
        viewHolder.msg.setText(chats.getMessage());

    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView msg;

        public ViewHolder(View _item) {
            super(_item);
            msg = (TextView) _item.findViewById(R.id.msg);

        }

    }

    @Override
    public int getItemViewType(int position) {
        users= FirebaseAuth.getInstance().getCurrentUser();
        if(chat.get(position).getSender().equals(users.getUid())){
            return _RIGHT;
        }else{
            return _LEFT;
        }
    }
}
