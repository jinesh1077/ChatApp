package com.jin10.chatnow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> users;

    public UserAdapter(Context _context,List<User> _user){
        context=_context;
        users=_user;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final User user = users.get(i);
        viewHolder.username.setText(user.getUsername());
        String s=user.getImageURL();
        Uri u= Uri.parse(s);
        Picasso.get().load(u).into(viewHolder.imgView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context,ChatActivity.class);
                i.putExtra("userid",user.getId());
                i.putExtra("username",user.getUsername());
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView imgView;

        public ViewHolder(View _item){
            super(_item);
            username=(TextView) _item.findViewById(R.id.usernames);
            imgView=(CircleImageView) _item.findViewById(R.id.imgUser);

        }

    }





}
