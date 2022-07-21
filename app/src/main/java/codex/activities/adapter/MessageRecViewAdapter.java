package codex.activities.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codexmobile.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Date;

import chat.model.Message;
import chat.model.User;

public class MessageRecViewAdapter extends RecyclerView.Adapter<MessageRecViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Message> messages;
    private User userConnected;
    public MessageRecViewAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void deleteMessages(){
        messages.clear();
        notifyDataSetChanged();
    }

    public void setUserConnected(User userConnected) {
        this.userConnected = userConnected;
    }

    public void newMessage(Message message){
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecViewAdapter.ViewHolder holder, int position) {
        holder.message.setText(messages.get(position).getText());
        holder.username.setText(messages.get(position).getSender().getId());
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        holder.dateTime.setText(currentDateTimeString);
        if(messages.get(position).getSender().getId().equals(userConnected.getId())){
            holder.card_message.setBackground(ContextCompat.getDrawable(context, R.color.purple_200));
        }else{
            holder.card_message.setBackground(ContextCompat.getDrawable(context, R.color.purple_700));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,message,dateTime;
        MaterialCardView card_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_message = itemView.findViewById(R.id.card_message);
            username = itemView.findViewById(R.id.user_username);
            message = itemView.findViewById(R.id.user_message);
            dateTime = itemView.findViewById(R.id.user_message_date_time);

        }
    }
}
