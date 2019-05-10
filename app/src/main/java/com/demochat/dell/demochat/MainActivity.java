package com.demochat.dell.demochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btnAddRoom;
    EditText edRoom;
    ListView lvUser;
    ArrayList<String> listUser = new ArrayList<>();
    ArrayAdapter adapter;
    String username;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddRoom = findViewById(R.id.btnAdd);
        edRoom = findViewById(R.id.edRoom);
        lvUser = findViewById(R.id.lvUser);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listUser);
        lvUser.setAdapter(adapter);
        getUserName();

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference(edRoom.getText().toString());
                reference.setValue("");
                edRoom.setText("");

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                adapter.clear();
                adapter.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MessageChat.class);
                intent.putExtra("Selected_Topic",((TextView)view).getText().toString());
                intent.putExtra("User_name",username);
                startActivity(intent);
            }
        });
    }

    private void getUserName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edUN = new EditText(this);
        builder.setView(edUN);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = edUN.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getUserName();
            }
        });
        builder.show();
    }
}
