package com.example.gittogether;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MainViewHolder> {

    Context context;

    ArrayList<Post> list;

    private OnItemListener mOnItemListener;

    public PostAdapter(Context context, ArrayList<Post> list, OnItemListener onItemListener) {
        this.context = context;
        this.list = list;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new MainViewHolder(v, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Post post = list.get(position);
        holder.title.setText(post.getTitle());
        holder.message.setText(post.getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, message;

        OnItemListener onItemListener;

        public MainViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            message = itemView.findViewById(R.id.item_post_message);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(view, getBindingAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(View v, int position);
    }

}
