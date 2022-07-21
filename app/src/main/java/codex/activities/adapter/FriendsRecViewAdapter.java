package codex.activities.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codexmobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import chat.model.Friend;
import chat.model.User;
import chat.persistance.mock.ListsMock;


public class FriendsRecViewAdapter extends RecyclerView.Adapter<FriendsRecViewAdapter.ViewHolder>{

    private static final String TAG = "MuseumRecViewAdapter";


    private ArrayList<Friend> friends = new ArrayList<>();
    private Context mContext;
    private String parentActivity;
    private ListsMock listsMock;
    private RecyclerView friendsRecView;
    private RecyclerView messageRecView;
    private TextInputLayout message;
    private FloatingActionButton sendBtn;
    public FriendsRecViewAdapter(Context mContext, String parentActivity, ListsMock listsMock, TextInputLayout message, FloatingActionButton sendBtn) {
        this.mContext = mContext;
        this.parentActivity = parentActivity;
        this.listsMock = listsMock;
        this.message= message;
        this.sendBtn = sendBtn;

    }

    public void setFriendsRecView(RecyclerView friendsRecView) {
        this.friendsRecView = friendsRecView;
    }

    public void setMessageRecView(RecyclerView messageRecView) {
        this.messageRecView = messageRecView;
    }
    private void showMessageBar(){
        message.setVisibility(View.VISIBLE);
        sendBtn.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_friend,parent,false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        holder.txtName.setText(friends.get(position).getUser().getId());

        //Glide.with(mContext).asBitmap().load(museums.get(position).getUrl_image()).into(holder.imgMuseum);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listsMock.setSelectedFriend(friends.get(position).getUser().getId());
                friendsRecView.setVisibility(View.INVISIBLE);
                messageRecView.setVisibility(View.VISIBLE);
                showMessageBar();
            }
        });
        holder.txtMuseum.setText(friends.get(position).getUser().getId());
        holder.txtDescription.setText(friends.get(position).getUser().getLastTimeOnline().toString());

        if(friends.get(position).getExpanded()){
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);

            if(parentActivity.equals("allConnectedFriends")){
                holder.btnView.setVisibility(View.VISIBLE);
                holder.btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are you sure you want to send a message ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listsMock.setSelectedFriend(friends.get(position).getUser().getId());
                                friendsRecView.setVisibility(View.INVISIBLE);
                                messageRecView.setVisibility(View.VISIBLE);
                                showMessageBar();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.create().show();

                    }
                });
            }

        }else{
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView parent;
        private ImageView imgMuseum;
        private TextView txtName;

        private ImageView downArrow,upArrow;
        private RelativeLayout expandedRelLayout;
        private TextView txtMuseum,txtDescription;
        private TextView btnView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.parent = itemView.findViewById(R.id.parent);
            this.imgMuseum = itemView.findViewById(R.id.imgMuseum);
            this.txtName = itemView.findViewById(R.id.txtMuseumName);

            downArrow = itemView.findViewById(R.id.btnArrowDown);
            upArrow = itemView.findViewById(R.id.btnUpArrow);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLayout);
            txtMuseum = itemView.findViewById(R.id.txtMuseum);
            txtDescription = itemView.findViewById(R.id.txtShortDesc);

            btnView = itemView.findViewById(R.id.btnView);

            downArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Friend friend = friends.get(getAdapterPosition());
                    friend.setExpanded(!friend.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            upArrow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Friend friend = friends.get(getAdapterPosition());
                    friend.setExpanded(!friend.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
