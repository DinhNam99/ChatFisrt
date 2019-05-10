package com.demochat.dell.demochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageChat extends AppCompatActivity {

    Button btnSendMess;
    EditText edMss;
    ListView lvUser;
    ArrayList<String> listUser = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    String username, selectTopic, user_key;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        btnSendMess = findViewById(R.id.send);
        edMss = findViewById(R.id.edMess);
        lvUser = findViewById(R.id.lvUserMess);

        arrayAdapter =  new ArrayAdapter(this,android.R.layout.simple_list_item_1,listUser);
        lvUser.setAdapter(arrayAdapter);

        username = getIntent().getExtras().get("User_name").toString();
        selectTopic = getIntent().getExtras().get("Selected_Topic").toString();
        setTitle("Room : "+selectTopic);
        reference = FirebaseDatabase.getInstance().getReference().child(selectTopic);

        btnSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String,Object>();
                user_key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference reference2 = reference.child(user_key);
                Map<String,Object> map2 = new HashMap<String,Object>();
                map2.put("msg",edMss.getText().toString());
                map2.put("user",username);
                reference2.updateChildren(map2);
                edMss.setText("");
                edMss.requestFocus();

            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    String mess, user;
    public void Conversation(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            mess = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();
            String conversation = user+" : "+mess;
            arrayAdapter.insert(conversation,arrayAdapter.getCount());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
