package codex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codexmobile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import chat.Server;
import chat.model.Message;
import chat.model.User;
import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;
import domain.Client;
import domain.api.ClientResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ApiRetrofit;
import service.CodexVolleyService;
import service.ServiceAPI;

public class LoginActivity extends AppCompatActivity{
    private Button btn_login;
    private TextView lblPassword,lblUsername;
    private EditText txtUserName, txtPassword;
    private ServiceAPI serviceAPI = new ServiceAPI(LoginActivity.this);


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView btn_google;
    Client clientApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        checkAutoLogin();
        initViews();


        loadClientLogin();
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                login(username,password);
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gsc = GoogleSignIn.getClient(LoginActivity.this,gso);

                signIn();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                if(acct != null){
                    checkIfAccountExists(acct.getEmail(),acct);
                }


            }
        });

    }

    private void login(String username,String password) {

        clientApp = serviceAPI.executeLogin(username,password);


        if(clientApp == null){
            Toast.makeText(LoginActivity.this,"error login",Toast.LENGTH_LONG).show();
        }
        else{

            SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
            //now get Editor
            SharedPreferences.Editor editor= sharedPref.edit();
            //put your value
            editor.putString("username", username);
            editor.putString("password", password);

            //commits your edits
            editor.commit();

            navigateToMainActivity(clientApp);
        }
    }

    private void checkAutoLogin() {
        SharedPreferences mPrefs = getSharedPreferences("mypref", 0);
        String username = mPrefs.getString("username", "invalid");
        String password = mPrefs.getString("password", "invalid");
        if(username != null && password != null){
            login(username,password);
        }

    }

    private void loadClientLogin() {
        try {
            if ((Client) getIntent().getExtras().getSerializable("clientDetails") != null) {
                Intent intent = getIntent();
                clientApp = (Client) intent.getExtras().getSerializable("clientDetails");
                txtUserName.setText(clientApp.userName);
            }
        }catch(Exception ex){

        }
    }
    private boolean checkIfAccountExists(String email,GoogleSignInAccount acct) {
        CodexVolleyService codexVolleyService = new CodexVolleyService(LoginActivity.this);

        codexVolleyService.checkIfAccountExists(email, new CodexVolleyService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Client clientDetails = new Client();
                clientDetails.setEmail(acct.getEmail());
                clientDetails.setUserName((acct.getEmail().split("@"))[0]);
                clientDetails.setFirstName(acct.getGivenName());
                clientDetails.setLastName(acct.getFamilyName());
                navigateToCreateAccountActivity(clientDetails);
            }

            @Override
            public void onResponse(Client response) {
                if(response.getEmail().equals(email)){
                    //navigateToMainActivity(response);
                    txtUserName.setText(response.userName);
                }

            }
        });
        return false;

    }



    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);

            }catch(ApiException e){
                Toast.makeText(LoginActivity.this,"error google login",Toast.LENGTH_LONG).show();
            }
        }
    }
    void navigateToMainActivity(Client clientDetails){
        finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("clientDetails", clientDetails);
        startActivity(intent);
    }
    void navigateToCreateAccountActivity(Client clientDetails){
        finish();
        Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
        intent.putExtra("clientDetails", clientDetails);
        startActivity(intent);
    }

    private void initViews(){
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        lblUsername = findViewById(R.id.lblUsername);
        lblPassword = findViewById(R.id.lblPassword);
        btn_login = findViewById(R.id.btn_login);
        btn_google = findViewById(R.id.btn_google);
    }
}



