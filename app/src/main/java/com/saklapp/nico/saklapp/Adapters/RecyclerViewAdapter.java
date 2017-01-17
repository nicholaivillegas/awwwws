package com.saklapp.nico.saklapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saklapp.nico.saklapp.Message;
import com.saklapp.nico.saklapp.R;

import java.util.List;

/**
 * Created by Nico on 1/17/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<Message> message;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<Message> message) {
        this.message = message;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, message);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.textName.setText(message.get(position).getName());
        holder.textDate.setText(message.get(position).getTime());
        holder.textStatus.setText(message.get(position).getMessage());
        holder.textLike.setText(message.get(position).getLike());
    }

    @Override
    public int getItemCount() {
        return this.message.size();
    }
}
