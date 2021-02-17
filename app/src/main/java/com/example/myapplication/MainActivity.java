package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class MainActivity extends AppCompatActivity implements MainView
{

    private String userId;

    private Toolbar toolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdaptor myTabsAccessorAdaptor;

    private DatabaseReference rootRef;
    private FirebaseAuth firebaseAuth;

    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisePresenter();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();


        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StudentZone");

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_paper);
        myTabsAccessorAdaptor = new TabsAccessorAdaptor((getSupportFragmentManager()));
        myViewPager.setAdapter(myTabsAccessorAdaptor);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }

    private void initialisePresenter() {
        mainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null) {
            SendUserToLogin();
        }
        else{
            updateStatus("yes");
            VerifyUserAccount();
        }
    }

    private void VerifyUserAccount() {

        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                }
                else {
                    SendUserToSettingsPage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_find_friends_option){
            mainPresenter.SendUserToFindFriends();
        }
        if(item.getItemId() == R.id.main_group_option){
            mainPresenter.newGroup();
        }
        if(item.getItemId() == R.id.main_settings_option){
            mainPresenter.sendUserToSettingsPage();
        }
        if(item.getItemId() == R.id.main_logout_option){
            updateStatus("no");
            firebaseAuth.signOut();
            SendUserToLogin();
        }
        return true;
    }

    @Override
    public void createNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name: ");
        final EditText groupName = new EditText(MainActivity.this);
        groupName.setHint("e.g Study Group");
        builder.setView(groupName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nameOfGroup = groupName.getText().toString();
                if(TextUtils.isEmpty(nameOfGroup)){
                    Toast.makeText(MainActivity.this, "Enter Group Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    addNewGroup(nameOfGroup);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void addNewGroup(final String nameOfGroup) {
        rootRef.child("Groups").child(nameOfGroup).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, nameOfGroup + " created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    private void updateStatus(String online){
        String date;
        String time;

        //Get current time
        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        time = simpleTimeFormat.format(calendarTime.getTime());

        //Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");
        date = simpleDateFormat.format(calendar.getTime());

        //Store date and time to db
        HashMap<String, Object> active = new HashMap<>();
        active.put("time", time);
        active.put("date", date);
        active.put("online", online);

        userId = firebaseAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(userId).child("active").updateChildren(active);

    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            updateStatus("no");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        if(firebaseUser != null) {
            updateStatus("no");
        }
    }

    @Override
    public void SendUserToSettingsPage() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void sendUserToFindFriends() {
        Intent friendsIntent = new Intent(MainActivity.this, FindPeopleActivity.class);
        startActivity(friendsIntent);
    }
}
