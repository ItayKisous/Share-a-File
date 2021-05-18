package com.itay.myapplication;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private ListView lv;
    private EditText etGroupName;
    HashMap<String, Object> GroupKeys;
    private String EmailConn = DBref.Auth.getCurrentUser().getEmail();
    private String GroupName, GroupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        lv = findViewById(R.id.lv);
        etGroupName=findViewById(R.id.etGroupName);
        GetAllGroups();


        EventHandler();
    }

    private void EventHandler() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group g=(Group) parent.getItemAtPosition(position);
                Intent i=new Intent(MainMenu.this,Posts.class);
                i.putExtra("GroupKey",g.getGroupID());
                startActivity(i);
            }
        });
    }

    public void GetAllGroups() {
        DBref.refUsers.child(EmailConn.replace('.', ' ')).child("GroupKeys").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            GroupKeys = (HashMap<String, Object>) dataSnapshot.getValue();
            ShowData();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }

    private void ShowData(){
        DBref.refGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Group> ArrGroups = new ArrayList<Group>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Group g = data.getValue(Group.class);
                    if(g != null && GroupKeys != null)
                    for (String key : GroupKeys.keySet()) {
                        if (key.equals(g.getGroupID()))
                            ArrGroups.add(g);
                    }
                }
                GroupAdapter adpt = new GroupAdapter(MainMenu.this, 0, 0,
                        ArrGroups);
                lv.setAdapter(adpt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.Logout:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logo)

                        .setTitle("You sure you want to log out?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                DBref.Auth.signOut();
                                startActivity(i);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Didn't Log Out", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                break;

            case R.id.AddGroup:
                final View customDialog = getLayoutInflater().inflate(R.layout.edittext_dialog, null);
                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.logo)
                        .setView(customDialog)
                        .setTitle("Create a Group")
                        .setMessage("Choose a name for your group")
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etGroupName = customDialog.findViewById(R.id.etGroupName);
                                GroupName = etGroupName.getText().toString();
                                addGroup();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;


        }
        return true;
    }


    private void addGroup(){
        EmailConn=DBref.Auth.getCurrentUser().getEmail();
        Group g1=new Group();
        g1.setGroupAdmin(EmailConn.replace('.',' '));
        GroupID= DBref.refGroups.push().getKey();
        g1.setGroupID(GroupID);
        g1.setGroupName(GroupName);
        g1.setGroupAdmin(EmailConn);
        g1.setGroupID(GroupID);
        DBref.refGroups.child(GroupID).setValue(g1);

        HashMap<String, Object> NewGroupToUser = new HashMap<>();
        NewGroupToUser.put(GroupID, "true");
        DBref.refUsers.child(EmailConn.replace('.',' ')).child("GroupKeys").updateChildren(NewGroupToUser);
    }
}
