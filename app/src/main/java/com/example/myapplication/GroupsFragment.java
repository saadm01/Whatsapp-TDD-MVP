package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment implements GroupsFragmentView{

    private GroupsFragmentPresenter groupsFragmentPresenter;


    private View groupView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> stringGroupsArrayList = new ArrayList<>();

    private DatabaseReference groupRef;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        initialisePresenter();


        groupView = inflater.inflate(R.layout.fragment_groups, container, false);
        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        InitialiseFields();

        groupsFragmentPresenter.getGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionOfField, long l) {
                String groupName = adapterView.getItemAtPosition(positionOfField).toString();

                //Show groups
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupChatName", groupName);
                startActivity(groupChatIntent);
            }
        });
        return groupView;
    }

    private void initialisePresenter() {
        groupsFragmentPresenter = new GroupsFragmentPresenter(this);
    }

    private void InitialiseFields() {
        listView = (ListView) groupView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringGroupsArrayList);
        listView.setAdapter(arrayAdapter);
    }



    public void setupListOfGroups(Set<String> set){
        //Setup list
        stringGroupsArrayList.clear();
        stringGroupsArrayList.addAll(set);
        arrayAdapter.notifyDataSetChanged();
    }

}
