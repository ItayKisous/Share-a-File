package com.itay.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CreatePost extends AppCompatActivity {
    private Button btnUpload;
    private String EmailConn, GroupID, PostID, PostText, PdfName;
    private EditText etPDF, etPostName;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        EmailConn=DBref.Auth.getCurrentUser().getEmail();
        GroupID = getIntent().getStringExtra("GroupKey");
        etPostName=findViewById(R.id.etNamePost);
        PostID = DBref.refPosts.push().getKey();
        btnUpload=findViewById(R.id.btnUpload);
        etPDF=findViewById(R.id.etPDF);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("UploadPDF");

        btnUpload.setEnabled(false);
        etPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPDF();
            }
        });
    }

    public void SelectPDF(){
        Intent pdfIntent = new Intent();
        pdfIntent.setType("application/pdf");
        pdfIntent.setAction(pdfIntent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(pdfIntent,"PDF FILE SELECT"), 12); // RequestCode = מאיזה אינטנט חזרנו
    }

    // Intent = עצם קשירה בזמן ריצה בין רכיבים שונים
    // לדוגמה - broadcast receiver

    // Broadcast Receiver = מחלקה של היישום המאזין למסרים המיועדים לו
    // שיחות טלפון, SMS וכו

    // Content Provider = רכיב שמטרתו לספק מידע לאפליקציות אחרות

    // Service = מבצע פעולות ארוכות טווח ברקע, רכיב יכול לקשר לשירות כדי לקיים אינטרקציה או תקשורת

    // Activity - רכיב חיוני והאופן שבו פעילויות משוגרות

    protected void onActivityResult(int requestCode, int resultCode, final @Nullable Intent data){
        // מה שקורה לאחר העלאת הקובת (SelectPDF)
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!= null){
            btnUpload.setEnabled(true);
            etPDF.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());
                    PostText=etPostName.getText().toString();
                    AddPost();
                }
            });
        }
    }

    private void uploadPDFFileFirebase(Uri data){
        final ProgressDialog prgDialog = new ProgressDialog(this);
        prgDialog.setTitle("File is uploading...");
        prgDialog.show();
        StorageReference reference = storageReference.child("upload"+System.currentTimeMillis()+".pdf");
        PdfName="upload"+System.currentTimeMillis()+".pdf";

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                    Uri uri = uriTask.getResult();

                    PutPDF putPDF = new PutPDF(etPDF.getText().toString(), uri.toString());
                    databaseReference.child(PostID).setValue(putPDF);
                    Toast.makeText(CreatePost.this, "File successfully uploaded!", Toast.LENGTH_LONG).show();
                    prgDialog.dismiss();

                Intent i = new Intent(CreatePost.this, Posts.class);
                i.putExtra("GroupKey", GroupID);
                startActivity(i);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                prgDialog.setMessage("File uploaded.."+(int) progress+"%");
            }
        });
    }

    private void AddPost() {
        Post p1=new Post(PostText, PostID, EmailConn,PdfName);

        DBref.refGroups.child(GroupID).child("PostsKeys").child(PostID).setValue(p1);
        HashMap<String, Object> NewPostToGroup = new HashMap<>();
        NewPostToGroup.put(PostID, "true");
        HashMap<String, Object> NewPostToUser =new HashMap<>();
        NewPostToUser.put(PostID, "true");
        DBref.refUsers.child(EmailConn.replace('.', ' ')).child("PostsKeys").updateChildren(NewPostToUser);
    }
}
