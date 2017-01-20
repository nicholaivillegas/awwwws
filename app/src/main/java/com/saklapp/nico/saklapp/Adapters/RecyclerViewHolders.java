package com.saklapp.nico.saklapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.saklapp.nico.saklapp.Message;
import com.saklapp.nico.saklapp.R;

import java.util.List;

/**
 * Created by Nico on 1/17/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView textName, textDate, textStatus, textLike;
    public Button buttonRelate;
    private List<Message> messageObject;

    public RecyclerViewHolders(final View itemView, final List<Message> messageObject) {
        super(itemView);
        this.messageObject = messageObject;
        textName = (TextView) itemView.findViewById(R.id.text_name);
        textDate = (TextView) itemView.findViewById(R.id.text_date);
        textStatus = (TextView) itemView.findViewById(R.id.text_status);
        textLike = (TextView) itemView.findViewById(R.id.text_like);
        buttonRelate = (Button) itemView.findViewById(R.id.button_relate);

    }
}
