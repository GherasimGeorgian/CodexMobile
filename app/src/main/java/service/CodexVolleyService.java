package service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import codex.activities.CreateAccountActivity;
import codex.activities.MainActivity;
import domain.Client;
import domain.ClientRequest;

public class CodexVolleyService {

    private static final String BASE_URL = "http://192.168.1.37:8080/api";
    Context context;
    Client clientReturned;
    public CodexVolleyService(Context context) {
        this.context = context;
    }



    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(Client response);
    }

    public interface VolleyResponseListener2{
        void onError(String message);

        void onResponse(JSONObject response);
    }
    public void refresh_current_token(){
        String url = BASE_URL + "/token/refresh";
        JsonObjectRequest request = null;

        request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    SharedPreferences sharedPref= context.getSharedPreferences("mypref", 0);
                    //now get Editor
                    SharedPreferences.Editor editor= sharedPref.edit();
                    //put your value
                    editor.putString("access_token", response.getString("access_token"));

                    //commits your edits
                    editor.commit();

                    System.out.println("evrica");


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences mPrefs = context.getSharedPreferences("mypref", 0);
                String refresh_token = mPrefs.getString("refresh_token", "invalid");
                params.put("Authorization", "Bearer " + refresh_token);

                return params;
            }
        };

        // queue.add(request);
        SingletonRequestQueue.getInstance(context).addToRequestQueue(request);
    }


    public void getProfileImageByUserName(String username, VolleyResponseListener2 volleyResponseListener){
        String url = BASE_URL + "/user/get_photo/" + username;
        JsonObjectRequest request = null;

        request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    System.out.println("avem raspuns esti nebun:"+response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 403){
                    refresh_current_token();
                    volleyResponseListener.onError("refresh_token");
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences mPrefs = context.getSharedPreferences("mypref", 0);
                String access_token = mPrefs.getString("access_token", "invalid");
                params.put("Authorization", "Bearer " + access_token);

                return params;
            }
        };

        // queue.add(request);
        SingletonRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public void checkIfAccountExists(String email, VolleyResponseListener volleyResponseListener) {
        String url = BASE_URL + "/get_account_by_email/" + email;
        JsonObjectRequest request = null;

            request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        clientReturned = Client.getClientWithouToken(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    volleyResponseListener.onResponse(clientReturned);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError("Something wrong;");
                }
            });

        // queue.add(request);
        SingletonRequestQueue.getInstance(context).addToRequestQueue(request);
    }
    public void createClient(ClientRequest clientRequest,VolleyResponseListener volleyResponseListener){
        String url = BASE_URL + "/create_account";
        Gson gson = new Gson();
        String jsonClient = gson.toJson(clientRequest);
        Log.i("json", jsonClient);

        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonClient), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        clientReturned = Client.getClientWithouToken(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    volleyResponseListener.onResponse(clientReturned);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError("Something wrong;");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // queue.add(request);
        SingletonRequestQueue.getInstance(context).addToRequestQueue(request);
       // return clientReturned;
    }
}
