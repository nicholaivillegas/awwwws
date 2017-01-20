package com.saklapp.nico.saklapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.saklapp.nico.saklapp.Message;
import com.saklapp.nico.saklapp.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Nico on 1/17/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<Message> message;
    protected Context context;
    RecyclerViewHolders recyclerViewHolder;
    Message temp;

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
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        temp = message.get(position);
        recyclerViewHolder = holder;
        holder.textName.setText(message.get(position).getName());
        holder.textDate.setText(message.get(position).getTime());
        holder.textStatus.setText(message.get(position).getMessage());
        holder.textLike.setText(message.get(position).getLike());
        holder.buttonRelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
                final String relate = temp.getLike();
                Log.d(TAG, "Message Title " + relate);
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.orderByChild("like").equalTo(relate);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            int a = Integer.parseInt(appleSnapshot.child("like").getValue().toString());
                            appleSnapshot.child("like").getRef().setValue(String.valueOf(a + 1));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.message.size();
    }

    public void onUpdate(Message message, DataSnapshot datasnapshot) {
        String uid = datasnapshot.getKey();
        temp.setLike(message.getLike());
    }
}
