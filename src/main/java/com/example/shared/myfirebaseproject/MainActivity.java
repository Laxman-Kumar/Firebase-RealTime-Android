package com.example.shared.myfirebaseproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText input_name,inpur_email;
    private ListView list_data;
    private ProgressBar circular_progress;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    private List<User> list_user = new ArrayList<>();

    private User selecteduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Firebase Demo");
        setSupportActionBar(toolbar);

        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        input_name = (EditText)findViewById(R.id.name);
        inpur_email = (EditText)findViewById(R.id.email);
        list_data  = (ListView )findViewById(R.id.list_data);
        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User)adapterView.getItemAtPosition(i);
                selecteduser=user;
                input_name.setText(user.getName());
                inpur_email.setText(user.getEmail());
            }
        });

        initFirebase();
        addEventFirebaseListener();

    }

    private void addEventFirebaseListener() {

        circular_progress.setVisibility(View.VISIBLE);
        list_data.setVisibility(View.INVISIBLE);

        mdatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(list_user.size()>0)
                    list_user.clear();



                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    User user = postSnapShot.getValue(User.class);
                    list_user.add(user);
                }
                ListviewAdapter adapter = new ListviewAdapter(MainActivity.this,list_user);
                list_data.setAdapter(adapter);

                circular_progress.setVisibility(View.INVISIBLE);
                list_data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_add){
            createrUser();
        }
        else if(item.getItemId() == R.id.item_save){
            User user = new User(selecteduser.getUid(),input_name.getText().toString(),inpur_email.getText().toString());
            updateUser(user);
        }
        else if(item.getItemId() == R.id.item_remove){
            deleteUser(selecteduser);
        }
        return true;
    }

    private void deleteUser(User selecteduser) {
        mdatabaseReference.child("users").child(selecteduser.getUid()).removeValue();
        clearEdittext();
    }

    private void createrUser() {

        User user = new User(UUID.randomUUID().toString(),input_name.getText().toString(),inpur_email.getText().toString());
        mdatabaseReference.child("users").child(user.getUid()).setValue(user);
        clearEdittext();
    }

    private void updateUser(User user) {
        mdatabaseReference.child("users").child(user.getUid()).child("name").setValue(user.getName());
        mdatabaseReference.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
        clearEdittext();
    }

    private void clearEdittext() {
        inpur_email.setText("");
        input_name.setText("");
    }
}
