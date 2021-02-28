package com.itay.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    Context context;
    List<Post> PostList;
    private String Name="", Surname="", PostID;

    public PostAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes
            int textViewResourceId, @NonNull List<Post> PostList) {
        super(context, resource, textViewResourceId, PostList);
        this.context = context;
        this.PostList = PostList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)
                context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.listpost, parent, false);
        final Post temp = PostList.get(position);
        TextView txtPostText = (TextView) view.findViewById(R.id.tvPostName);
        txtPostText.setText(temp.getPostText());
        PostID = temp.getPostID();
        DBref.refUsers.child(temp.getPostByUser().replace('.', ' ')).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u=dataSnapshot.getValue(User.class);
                Name = u.getName();
                Surname=u.getSurname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtPostText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference reference = FirebaseStorage.getInstance().getReference().child(temp.getPdfName());

                Intent i = new Intent(getContext(), PdfViewer.class);
                i.putExtra("PostID", PostID);
                context.startActivity(i);
            }
        });

        return view;
    }
}
