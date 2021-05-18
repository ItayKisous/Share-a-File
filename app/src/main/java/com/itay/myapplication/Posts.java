package com.itay.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Posts extends AppCompatActivity {

    private String EmailConn, GroupID, GroupName2;
    private ListView lv;
    HashMap<String, Object> PostsKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Intent i = getIntent();
        lv = findViewById(R.id.glv);
        EmailConn=DBref.Auth.getCurrentUser().getEmail();
        GroupID = getIntent().getStringExtra("GroupKey");

        GetAllPosts();
        EventHandler();
    }


    public void EventHandler(){
        DBref.refGroups.child(GroupID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group g = dataSnapshot.getValue(Group.class);
                TextView GroupName = findViewById(R.id.tvGroupName);
                GroupName.setText(g.getGroupName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.postmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        DBref.refGroups.child(GroupID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group g = snapshot.getValue(Group.class);
                GroupName2 = g.getGroupName().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        switch(item.getItemId()){
            case R.id.AddPost:
                Intent i = new Intent(Posts.this, CreatePost.class);
                i.putExtra("GroupKey", GroupID);
                startActivity(i);
                break;

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

            case R.id.AddMember:
                final View customDialog = getLayoutInflater().inflate(R.layout.addmember_dialog, null);
                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.logo)
                        .setView(customDialog)
                        .setTitle("Add a member")
                        .setMessage("Add a member to " + GroupName2)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etEmail = customDialog.findViewById(R.id.etMemberMail);
                                HashMap<String, Object> AddGroupToUser =new HashMap<>();
                                AddGroupToUser.put(GroupID, true);
                                DBref.refUsers.child(etEmail.getText().toString().replace('.', ' ')).child("GroupKeys").updateChildren(AddGroupToUser);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
        }
        return true;
    }

    public void GetAllPosts() {
        DBref.refGroups.child(GroupID).child("PostsKeys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostsKeys = (HashMap<String, Object>) dataSnapshot.getValue();
                ShowData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void ShowData(){
        DBref.refGroups.child(GroupID).child("PostsKeys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Post> ArrPosts = new ArrayList<Post>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post g = data.getValue(Post.class);
                    if(g != null)
                    for (String key : PostsKeys.keySet()) {
                        if (key.equals(g.getPostID()))
                            ArrPosts.add(g);
                    }
                }
                PostAdapter adpt = new PostAdapter(Posts.this, 0, 0,
                        ArrPosts);
                lv.setAdapter(adpt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
