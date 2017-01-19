package com.saklapp.nico.saklapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

/**
 * Created by Nico on 1/17/2017.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView textName, textDate, textStatus, textLike;
    public Button buttonDelete;
    private List<Message> messageObject;

    public RecyclerViewHolders(final View itemView, final List<Message> messageObject) {
        super(itemView);
        this.messageObject = messageObject;
        textName = (TextView) itemView.findViewById(R.id.text_name);
        textDate = (TextView) itemView.findViewById(R.id.text_date);
        textStatus = (TextView) itemView.findViewById(R.id.text_status);
        textLike = (TextView) itemView.findViewById(R.id.text_like);
        buttonDelete = (Button) itemView.findViewById(R.id.button_like);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
                String taskTitle = messageObject.get(getAdapterPosition()).getMessage();
                Log.d(TAG, "Message Title " + taskTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.orderByChild("task").equalTo(taskTitle);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
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
}
