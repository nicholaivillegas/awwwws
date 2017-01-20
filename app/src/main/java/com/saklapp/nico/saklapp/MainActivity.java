package com.saklapp.nico.saklapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saklapp.nico.saklapp.Adapters.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 11/23/2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText editMessage;
    private DatabaseReference databaseReference;
    private List<Message> allMessage;
    private String strName;
    private String formattedDate;
    private String enteredMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region FIREBASE AUTH
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (TextUtils.isEmpty(user.getDisplayName())) strName = "Anonymous";
        else strName = user.getDisplayName();
        Toast.makeText(this, "Welcome, " + strName, Toast.LENGTH_SHORT).show();
        //endregion

        allMessage = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editMessage = (EditText) findViewById(R.id.edit_message);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ImageButton buttonSend = (ImageButton) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredMessage = editMessage.getText().toString();
                if (TextUtils.isEmpty(enteredMessage)) {
                    Toast.makeText(MainActivity.this, "You must enter a message first", Toast.LENGTH_LONG).show();
                    return;
                }
//                if (enteredTask.length() < 6) {
//                    Toast.makeText(MainActivity.this, "Message count must be more than 6", Toast.LENGTH_LONG).show();
//                    return;
//                }
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                formattedDate = df.format(c.getTime());
                Message messageObject = new Message(strName, formattedDate, enteredMessage, "0", "yes");
                databaseReference.push().setValue(messageObject);
                editMessage.setText("");
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllStatus(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateStatus(dataSnapshot);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                statusDeletion(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getAllStatus(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        allMessage.add(new Message(message.getName(), message.getTime(), message.getMessage(), message.getLike(), message.getLikeable()));
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allMessage);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void updateStatus(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        recyclerViewAdapter.onUpdate(message, dataSnapshot);
    }


    private void statusDeletion(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for (int i = 0; i < allMessage.size(); i++) {
                if (allMessage.get(i).getMessage().equals(taskTitle)) {
                    allMessage.remove(i);
                }
            }
            Log.d(TAG, "Message tile " + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allMessage);
            recyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_logout);
        menuItem.setIcon(R.drawable.ic_exit_to_app_white_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        Log.d(TAG, "LOGOUT");
    }
}
