package codex.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.codexmobile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chat.Server;
import chat.model.Friend;
import chat.model.FriendsListModel;
import chat.model.Message;
import chat.model.MessagesListModel;
import chat.model.User;
import chat.persistance.mock.ListsMock;
import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;
import codex.activities.adapter.FriendsRecViewAdapter;
import codex.activities.adapter.MessageRecViewAdapter;
import domain.Client;
import service.CodexVolleyService;

public class MainActivity extends AppCompatActivity implements IChatObserver {
    private User crtUser;
    private Client clientApp;

    private IChatServices server;
    private Button btn_connect;
    private RecyclerView friendsRecView;
    private FriendsRecViewAdapter adapter;

    private MessageRecViewAdapter messageRecViewAdapter;
    private RecyclerView messageRecView;
    private TextInputLayout message;
    private FloatingActionButton sendBtn;

    private Boolean retryConnection = false;



    private ListsMock listsMock = ListsMock.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = findViewById(R.id.fab_send);
        message = findViewById(R.id.message);

        adapter  = new FriendsRecViewAdapter(this,"allConnectedFriends",listsMock,message,sendBtn);
        messageRecViewAdapter = new MessageRecViewAdapter(this,listsMock.getMessagesModel().getAllMessages());
        initViews();
        loadClientLogin();
        hideMessageBar();
        adapter.setFriendsRecView(friendsRecView);
        adapter.setMessageRecView(messageRecView);



        Server serverInit = new Server(MainActivity.this);


        btn_connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                server = serverInit.getServerApp();

                crtUser = new User(clientApp.userName,clientApp.firstName + clientApp.lastName,clientApp.password);

                messageRecViewAdapter.setUserConnected(crtUser);
                try {
                    server.login(crtUser, MainActivity.this);

                    User[] loggedInFriends=server.getLoggedFriends(crtUser);

                    User[] offlineFriends=server.getOfflineFriends(crtUser);

                    Log.i("info55", "Logged friends "+loggedInFriends.length);
                    Log.i("info55", "Offline friends "+offlineFriends.length);

                    getProfileImageByUserName("gaois.gaos");



                    for(User us : loggedInFriends){
                        listsMock.getFriendsModel().friendLoggedIn(new Friend(us,true,false));
                        //Log.i("info55", "friend "+us.getId());
                        new Thread(new Runnable() {
                            public void run() {
                                if(retryConnection){
                                    getProfileImageByUserName("gaois.gaos");
                                    System.out.println("truecon2");
                                }
                            }
                        }).start();
                    }
                    for(User us : offlineFriends){
                        listsMock.getFriendsModel().friendLoggedIn(new Friend(us,false,false));
                        //Log.i("info55", "friend "+us.getId());
                    }


                    adapter.setFriends(listsMock.getFriendsModel().getAllFriendsLoggedIn());

                } catch (ChatException e) {
                    e.printStackTrace();
                }

                btn_connect.setVisibility(View.INVISIBLE);
                friendsRecView.setVisibility(View.VISIBLE);
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendMessage(ListsMock.getInstance().getSelectedFriend(), message.getEditText().getText().toString());
                    messageRecViewAdapter.newMessage(new Message(crtUser,message.getEditText().getText().toString(),new User(ListsMock.getInstance().getSelectedFriend())));
                    message.getEditText().setText("");
                } catch (ChatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean getProfileImageByUserName(String username) {
        CodexVolleyService codexVolleyService = new CodexVolleyService(MainActivity.this);

        codexVolleyService.getProfileImageByUserName(username, new CodexVolleyService.VolleyResponseListener2() {
            @Override
            public void onError(String message){
               if(message.equals("refresh_token")){
                   retryConnection = true;
                   System.out.println("truecon");
               }
            }

            @Override
            public void onResponse(JSONObject response) {
//                if(response.getEmail().equals(email)){
//                    navigateToMainActivity(response);
//                }

            }
        });
        return false;

    }


    @Override
    public void onBackPressed() {
       messageRecViewAdapter.deleteMessages();
       messageRecView.setVisibility(View.INVISIBLE);
       friendsRecView.setVisibility(View.VISIBLE);
       hideMessageBar();

    }

    public void sendMessage(String nFriend, String txtMsg) throws ChatException {
        listsMock.getMessagesModel().newMessage(new Message(new User(crtUser.getId()),txtMsg,new User(nFriend)));
        User sender=new User(crtUser.getId());
        User receiver=new User(nFriend);
        Message message=new Message(sender,txtMsg,receiver);
        server.sendMessage(message);
    }



    public void messageReceived(Message message) throws ChatException {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listsMock.getSelectedFriend().equals(message.getSender().getId())) {
                    messageRecViewAdapter.newMessage(message);
                }
            }
        });



    }

    private void hideMessageBar(){
        message.setVisibility(View.INVISIBLE);
        sendBtn.setVisibility(View.INVISIBLE);
    }

    public void friendLoggedIn(User friend) throws ChatException {
        listsMock.getFriendsModel().friendLoggedIn(new Friend(friend,true,false));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setFriends(listsMock.getFriendsModel().getAllFriendsLoggedIn());
            }
        });
    }
    public void friendLoggedOut(User friend) throws ChatException {
        listsMock.getFriendsModel().friendLoggedOut(friend);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setFriends(listsMock.getFriendsModel().getAllFriendsLoggedIn());
            }
        });



    }

    private void loadClientLogin() {
        try {
            if ((Client) getIntent().getExtras().getSerializable("clientDetails") != null) {
                Intent intent = getIntent();
                clientApp = (Client) intent.getExtras().getSerializable("clientDetails");
            }
        }catch(Exception ex){

        }
    }


    public void logout() {
        try {
            server.logout(crtUser, this);
        } catch (ChatException e) {
            System.out.println("Logout error "+e);
        }
    }

    private void initViews(){
        friendsRecView = findViewById(R.id.friendsRecView);

        friendsRecView.setAdapter(adapter);
        friendsRecView.setVisibility(View.INVISIBLE);
        friendsRecView.setLayoutManager(new LinearLayoutManager(this));


        messageRecView = findViewById(R.id.recycleviewmessage);

        messageRecView.setAdapter(messageRecViewAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        messageRecView.setLayoutManager(llm);
        messageRecView.setVisibility(View.INVISIBLE);


        btn_connect = findViewById(R.id.btn_connect);
    }

}