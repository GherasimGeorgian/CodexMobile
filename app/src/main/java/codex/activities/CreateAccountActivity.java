package codex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.codexmobile.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import domain.AccountType;
import domain.Client;
import domain.ClientRequest;
import service.CodexVolleyService;
import service.ServiceAPI;
import service.SingletonRequestQueue;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText txtUpdateUserName,txtUpdateFirstName,txtUpdateLastName,txtUpdateEmail,txtSetPassword,txtSetRePassword,txtUpdateNumberPhone;
    private DatePickerDialog datePickerDialog;
    private Button dateButton,btnCreateAccount;
    private ServiceAPI serviceAPI = new ServiceAPI(CreateAccountActivity.this);
    private Client clientApp;
    private ClientRequest clientRequest = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();
        initDatePicker();
        getDataIntent();
        setData(clientApp);

        btnCreateAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    setClientDetails();
                   // RequestQueue queue = Volley.newRequestQueue(CreateAccountActivity.this);
                    CodexVolleyService codexVolleyService = new CodexVolleyService(CreateAccountActivity.this);

                    codexVolleyService.createClient(clientRequest, new CodexVolleyService.VolleyResponseListener() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponse(Client response) {
                            navigateToLoginActivity(response);
                        }
                    });




                } catch (Exception e) {
                    Toast.makeText(CreateAccountActivity.this,"Your data is invalid!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void setClientDetails() throws Exception{
        if(txtUpdateUserName.getText() != null && txtUpdateFirstName.getText() != null
         &&txtUpdateLastName.getText() != null && txtUpdateEmail.getText()!=null && dateButton.getText() != null
        &&txtSetPassword.getText() != null && txtSetRePassword.getText() != null && txtUpdateNumberPhone.getText() != null){
            if(txtSetPassword.getText().toString().equals(txtSetRePassword.getText().toString())){


                String stringDate = "2001-07-04T12:08:56.235-0700";


                clientRequest = new ClientRequest(txtUpdateUserName.getText().toString(),txtSetPassword.getText().toString(),
                        txtUpdateEmail.getText().toString(),txtUpdateFirstName.getText().toString(),txtUpdateLastName.getText().toString(),
                        stringDate,txtUpdateNumberPhone.getText().toString(),AccountType.CLIENT.toString());
            }
            else{
                throw new Exception("Datele introduse sunt incorecte!");
            }

        }
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog= new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day+ " " + year;
    }
    void navigateToMainActivity(Client clientDetails){
        finish();
        Intent intent = new Intent(CreateAccountActivity.this,MainActivity.class);
        intent.putExtra("clientDetails", clientDetails);
        startActivity(intent);
    }

    void navigateToLoginActivity(Client clientDetails){
        finish();
        Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
        intent.putExtra("clientDetails", clientDetails);
        startActivity(intent);
    }

    private String getMonthFormat(int month) {
        if(month == 1){
            return "JAN";
        }
        if(month == 2){
            return "FEB";
        }
        if(month == 3){
            return "MAR";
        }
        if(month == 4){
            return "APR";
        }
        if(month == 5){
            return "MAY";
        }
        if(month == 6){
            return "JUN";
        }
        if(month == 7){
            return "JUL";
        }
        if(month == 8){
            return "AUG";
        }
        if(month == 9){
            return "SEP";
        }
        if(month == 10){
            return "OCT";
        }
        if(month == 11){
            return "NOV";
        }
        if(month == 12){
            return "DEC";
        }
        return "JAN";

    }

    private void getDataIntent(){
        Intent intent = getIntent();
        clientApp = (Client) intent.getExtras().getSerializable("clientDetails");
    }

    private void setData(Client client){
        txtUpdateUserName.setText(client.getUserName());
        txtUpdateFirstName.setText(client.getFirstName());
        txtUpdateLastName.setText(client.getLastName());
        txtUpdateEmail.setText(client.getEmail());
        dateButton.setText(getTodaysDate());

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initViews(){
        dateButton = findViewById(R.id.datePickerButton);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        txtUpdateUserName = findViewById(R.id.txtUpdateUserName);
        txtUpdateFirstName = findViewById(R.id.txtUpdateFirstName);
        txtUpdateLastName = findViewById(R.id.txtUpdateLastName);
        txtUpdateEmail = findViewById(R.id.txtUpdateEmail);

        txtSetPassword = findViewById(R.id.txtSetPassword);
        txtSetRePassword= findViewById(R.id.txtSetRePassword);
        txtUpdateNumberPhone = findViewById(R.id.txtUpdatePhoneNumber);

    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}