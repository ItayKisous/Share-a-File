package com.itay.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBref {

    public static FirebaseAuth Auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase DataBase = FirebaseDatabase.getInstance();

    public static DatabaseReference refUsers = DataBase.getReference("Users");

}
