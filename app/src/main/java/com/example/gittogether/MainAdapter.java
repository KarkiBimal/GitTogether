package com.example.gittogether;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Context context;

    ArrayList<User> list;

    private OnItemListener mOnItemListener;

    public MainAdapter(Context context, ArrayList<User> list, OnItemListener onItemListener) {
        this.context = context;
        this.list = list;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MainViewHolder(v, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        User user = list.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.address.setText(user.getAddress());
        holder.hobbies.setText(user.getHobby1() + ", " + user.getHobby2() + ", " + user.getHobby3());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView firstName, hobbies, address;

        OnItemListener onItemListener;

        public MainViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            firstName = itemView.findViewById(R.id.tvfirstName);
            address = itemView.findViewById(R.id.tvCity);
            hobbies = itemView.findViewById(R.id.tvHobbies);
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
