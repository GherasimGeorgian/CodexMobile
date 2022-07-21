package chat;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.codexmobile.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import chat.network.rpcprotocol.ChatServicesRpcProxy;
import chat.services.IChatServices;
import codex.activities.LoginActivity;

public class Server {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    private static Server instance = null;
    private static Context context;
    private IChatServices serverApp;
    public static Server getInstance(Context context) {
        if ( instance == null ) {
            instance = new Server(context);
        }

        return instance;
    }
    public IChatServices getServerApp(){
        return serverApp;
    }


    public Server(Context context) {
        this.context = context;
        Properties clientProps=new Properties();
        try {
            InputStream inputStream = context.getApplicationContext().getResources().openRawResource(R.raw.chatclient);
            clientProps.load(inputStream);
            // clientProps.load(Server.class.getResourceAsStream("/chatclient.properties"));
            //System.out.println("Client properties set. ");
            String serverIP=clientProps.getProperty("chat.server.host",defaultServer);
            Log.i("serverIP",serverIP);
            int serverPort=defaultChatPort;
            try{
                serverPort=Integer.parseInt(clientProps.getProperty("chat.server.port"));
            }catch(NumberFormatException ex){
                Log.i("error","Wrong port number "+ex.getMessage());
                Log.i("error","Using default port: "+defaultChatPort);
            }
            Log.i("ok","Using server IP "+serverIP);
            Log.i("ok","Using server port "+serverPort);

            IChatServices server=new ChatServicesRpcProxy(serverIP, serverPort);
            serverApp = server;

            //clientProps.list(System.out);
        } catch (
                IOException e) {
            System.err.println("Cannot find chatclient.properties "+e);
            return;
        }

    }

    public void loadServer(){

    }

}
