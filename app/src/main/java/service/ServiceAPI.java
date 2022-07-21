package service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import codex.activities.LoginActivity;
import domain.Client;
import domain.api.ClientResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAPI {

    public Context context;
    public Client client_App = null;


    public ServiceAPI(Context context) {
        this.context = context;
    }


    public void setContext(Context context){
        this.context = context;
    }

    public Client executeLogin(String username, String password){

        ApiRetrofit apiRetrofit = ApiRetrofit.getInstance();

        ClientResponse clientResponse = apiRetrofit.getRetrofit().create(ClientResponse.class);

        Call<ResponseBody> call =  clientResponse.login(username,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"success",Toast.LENGTH_LONG).show();

                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        JSONObject json = new JSONObject(s);

                        SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
                        //now get Editor
                        SharedPreferences.Editor editor= sharedPref.edit();
                        //put your value
                        editor.putString("access_token", json.getString("access_token"));
                        editor.putString("refresh_token", json.getString("refresh_token"));
                        //commits your edits
                        editor.commit();


                        client_App = Client.getClient(json);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
            }
        });
        return client_App;
    }

}
