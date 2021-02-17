package com.example.myapplication;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.annotation.NonNull;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class GroupsFragmentPresenter {

    private final GroupsFragmentView groupsFragmentView;
    private DatabaseReference groupRef;


    GroupsFragmentPresenter(GroupsFragmentView groupsFragmentView) {
        this.groupsFragmentView = groupsFragmentView;
    }

    void getGroups(){
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        groupRef.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                groupsFragmentView.setupListOfGroups(set);;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }));
    }

}
