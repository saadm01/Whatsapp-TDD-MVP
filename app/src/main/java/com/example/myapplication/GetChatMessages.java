package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class GetChatMessages extends RecyclerView.Adapter<GetChatMessages.messages>{


    //Create list for chat messages
    private List<ChatMessages> chatMessagesList;

    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;

    public GetChatMessages(List<ChatMessages> chatMessagesList){
        this.chatMessagesList = chatMessagesList;
    }

    @NonNull
    @Override
    public messages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Display messages

        
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout2, parent, false);

        firebaseAuth = FirebaseAuth.getInstance();

        return new messages(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final messages holder, final int position) {
        //Get and display the messages

        //Get current logged in sender user Id
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        ChatMessages chatMessages = chatMessagesList.get(position);


        //Get message details
        String userId = chatMessages.getSender();
        String messageType = chatMessages.getTypeOfMessage();
        String userTo = chatMessages.getTo();
        String messageId = chatMessages.getMessageID();

        //Get profile picture of chat user
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("picture")){
                    //Get information about receiver
                    //Get receiver user profile picture from database
                    String getReceiverProfilePic = dataSnapshot.child("picture").getValue().toString();

                    //Display profile picture of user
                    Picasso.get().load(getReceiverProfilePic).placeholder(R.drawable.profile_image).into(holder.pictureOfReceiver);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Default settings
        holder.messageOfSender.setVisibility(View.INVISIBLE);
        holder.pictureOfReceiver.setVisibility(View.INVISIBLE);
        holder.messageOfReceiver.setVisibility(View.INVISIBLE);



        if(messageType.equals("text")){

            //Sender
            if(userId.equals(currentUserId)){
                //Set message
                holder.messageOfSender.setVisibility(View.VISIBLE);
                holder.messageOfSender.setBackgroundResource(R.drawable.messages_layout1);
                holder.messageOfSender.setText(chatMessages.getMessage());
            }
            else{
                //Receiver
                //Show message
                holder.pictureOfReceiver.setVisibility(View.VISIBLE);
                holder.messageOfReceiver.setVisibility(View.VISIBLE);

                //Set message
                holder.messageOfReceiver.setBackgroundResource(R.drawable.messages_layout);
                holder.messageOfReceiver.setText(chatMessages.getMessage());
            }

        }

        //Delete message from sender side
        if(userId.equals(currentUserId)){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatMessagesList.get(position).getTypeOfMessage().equals("text")){

                        //Set up options for user to select which type of delete
                        CharSequence charSequence[] = new CharSequence[]{
                               "Delete Only For Me",
                               "Delete For Everyone",
                               "Cancel"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int item) {
                                if(item == 0){
                                    //User selects delete only for themselves
                                    deleteForSender(position, holder);
                                    //Send user back to main home page to refresh chat
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                else if(item == 1){
                                    //User selects delete message for everyone
                                    deleteForEveryone(position, holder);
                                    //Send user back to main home page to refresh chat
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                //Cancel will auto work

                            }
                        });
                        //Show dialog
                        builder.show();
                    }

                }
            });
        }
        //Delete message from receiver side
        //User cannot delete someone else's message for the both users
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatMessagesList.get(position).getTypeOfMessage().equals("text")){

                        //Set up options for user to select which type of delete
                        CharSequence charSequence[] = new CharSequence[]{
                                "Delete Only For Me",
                                "Cancel"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Delete?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int item) {
                                if(item == 0){
                                    //User selects delete only for themselves
                                    deleteForReceiver(position, holder);
                                    //Send user back to main home page to refresh chat
                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                //Cancel will auto work
                            }
                        });
                        //Show dialog
                        builder.show();
                    }

                }
            });

        }
    }



    @Override
    public int getItemCount() {
        //Get how many messages there are in the list
        return chatMessagesList.size();
    }


    private void deleteForSender(final int position, final messages messagesholder){
        //Get clicked on message
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //Get message details and then delete
        rootRef.child("Messages").child(chatMessagesList.get(position).getSender())
                .child(chatMessagesList.get(position).getTo())
                .child(chatMessagesList.get(position).getMessageID()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Show deletion success message
                            Toast.makeText(messagesholder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Show error message
                            Toast.makeText(messagesholder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteForReceiver(final int position, final messages messagesholder){
        //Get clicked on message
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //Get message details and then delete
        rootRef.child("Messages").child(chatMessagesList.get(position).getTo())
                .child(chatMessagesList.get(position).getSender())
                .child(chatMessagesList.get(position).getMessageID()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Show deletion success message
                            Toast.makeText(messagesholder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Show error message
                            Toast.makeText(messagesholder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteForEveryone(final int position, final messages messagesholder){
        //Get clicked on message
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //Get message details and then delete
        rootRef.child("Messages").child(chatMessagesList.get(position).getTo())
                .child(chatMessagesList.get(position).getSender())
                .child(chatMessagesList.get(position).getMessageID()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Delete for both users
                            rootRef.child("Messages").child(chatMessagesList.get(position).getSender())
                                    //Get message details and then delete
                                    .child(chatMessagesList.get(position).getTo())
                                    .child(chatMessagesList.get(position).getMessageID()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                //Show deletion success message
                                                Toast.makeText(messagesholder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                //Show error message
                                                Toast.makeText(messagesholder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }




    public class messages extends RecyclerView.ViewHolder{

        public TextView messageOfReceiver;
        public TextView messageOfSender;
        public CircleImageView pictureOfReceiver;


        public messages(@NonNull View itemView) {
            super(itemView);

            messageOfReceiver = (TextView) itemView.findViewById(R.id.messageOfReceiver);
            messageOfSender = (TextView) itemView.findViewById(R.id.messageOfSender);
            pictureOfReceiver = (CircleImageView) itemView.findViewById(R.id.pictureForMessage);



        }
    }
}