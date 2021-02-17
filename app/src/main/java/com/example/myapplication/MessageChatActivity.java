package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class MessageChatActivity extends AppCompatActivity  implements MessageChatActivityView{

    private MessageChatActivityPresenter messageChatActivityPresenter;
    private RecyclerView currentChat;

    private CircleImageView profilePicture;
    private TextView username;
    private TextView lastSeenTime;
    private EditText message;
    private ImageButton sendMessageButton;
    private Toolbar toolbar;

    private String userIdOfReceiver;
    private String usernameOfReceiver;
    private String pictureOfReceiver;


    private String userIdOfSender;
    private final List<ChatMessages> chatMessagesList = new ArrayList<>();

    private GetChatMessages getChatMessages;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    private DatabaseReference notiyRef;

    private LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        initialisePresenter();

        firebaseAuth = FirebaseAuth.getInstance();
        userIdOfSender = firebaseAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        notiyRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

        //Get receiver details
        userIdOfReceiver = getIntent().getExtras().get("chatUserId").toString();
        usernameOfReceiver = getIntent().getExtras().get("chatUsername").toString();
        pictureOfReceiver = getIntent().getExtras().get("chatPicture").toString();

        InitialiseFields();

        messageChatActivityPresenter.ShowReceiverDetails(usernameOfReceiver, pictureOfReceiver);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

        LastSeenDateTime();


    }

    private void initialisePresenter() {
        messageChatActivityPresenter = new MessageChatActivityPresenter(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("Messages").child(userIdOfSender).child(userIdOfReceiver).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessages chatMessages = dataSnapshot.getValue(ChatMessages.class);
                chatMessagesList.add(chatMessages);

                getChatMessages.notifyDataSetChanged();

                //Auto scroll to bottom
                currentChat.smoothScrollToPosition(currentChat.getAdapter().getItemCount());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void InitialiseFields() {
        toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.chat_bar,null);
        actionBar.setCustomView(view);

        currentChat = (RecyclerView) findViewById(R.id.currentChat);
        username = (TextView) findViewById(R.id.profileName);
        lastSeenTime = (TextView) findViewById(R.id.lastSeen);
        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        message = (EditText) findViewById(R.id.typeMessage);
        sendMessageButton = (ImageButton) findViewById(R.id.send_message_button);

        getChatMessages = new GetChatMessages(chatMessagesList);
        layoutManager = new LinearLayoutManager(this);
        currentChat.setLayoutManager(layoutManager);
        currentChat.setAdapter(getChatMessages);

    }

    public void showReceiverDetails(String usernameOfReceiver, String pictureOfReceriver) {
        //Display user name and user profile picture
        username.setText(usernameOfReceiver);
        Picasso.get().load(pictureOfReceriver).placeholder(R.drawable.profile_image).into(profilePicture);
    }

    private void SendMessage() {
        //Send message
        String textOfMessage = message.getText().toString();

        if(TextUtils.isEmpty(textOfMessage)) {
            messageChatActivityPresenter.sendMessageToUser(textOfMessage);
        }
        else{
            //Store message
            String senderOfMessage = "Messages/" + userIdOfSender + "/" + userIdOfReceiver;
            String receiverOfMessage = "Messages/" + userIdOfReceiver + "/" + userIdOfSender;

            //Store each new message
            DatabaseReference messageRef = rootRef.child("Messages").child(userIdOfSender).child(userIdOfReceiver).push();
            //Store unique key for each new message
            String messageKey = messageRef.getKey();

            //Store message body details on hash map
            Map messageBody = new HashMap();
            messageBody.put("message", textOfMessage);
            messageBody.put("typeOfMessage", "text");
            messageBody.put("sender", userIdOfSender);
            messageBody.put("to", userIdOfReceiver);
            messageBody.put("messageID", messageKey);


            //Store message details on hash map
            Map messageDetails = new HashMap();
            //For sender
            messageDetails.put(senderOfMessage + "/" + messageKey, messageBody);
            //For receiver
            messageDetails.put(receiverOfMessage + "/" + messageKey, messageBody);

            //Update whenever new text message is sent
            rootRef.updateChildren(messageDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    message.setText("");

                    //Hash map for notifications
                    HashMap<String, String> messageNotification = new HashMap<>();
                    //Store sender user id and type
                    messageNotification.put("from", userIdOfSender);
                    messageNotification.put("type", "message");
                    //Notify receiver
                    notiyRef.child(userIdOfReceiver).push().setValue(messageNotification)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                }
            });

        }

    }

    private void LastSeenDateTime(){

        rootRef.child("Users").child(userIdOfSender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageChatActivityPresenter.lastSeen(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void updateApp(){
        lastSeenTime.setText("Not Updated");
    }
    public void userIsOnline(){
        lastSeenTime.setText("User Is Online");
    }
    public void userIsOffline(String date, String time){
        lastSeenTime.setText("Last Seen: "  + date + " " + time);
    }
    public void enterText(){
        Toast.makeText(this, "Please enter a message...", Toast.LENGTH_SHORT).show();
    }

}
