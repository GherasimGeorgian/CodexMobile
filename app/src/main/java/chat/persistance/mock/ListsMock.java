package chat.persistance.mock;

import java.util.ArrayList;

import chat.model.FriendsListModel;
import chat.model.MessagesListModel;

public class ListsMock {
    private static ListsMock instance;
    private static String selectedFriend;

    private static FriendsListModel friendsModel;
    private static MessagesListModel messagesModel;

    public String getSelectedFriend() {
        return selectedFriend;
    }

    public void setSelectedFriend(String selectedFriend) {
        ListsMock.selectedFriend = selectedFriend;
    }

    public FriendsListModel getFriendsModel() {
        return friendsModel;
    }

    public void setFriendsModel(FriendsListModel friendsModel) {
        ListsMock.friendsModel = friendsModel;
    }

    public MessagesListModel getMessagesModel() {
        return messagesModel;
    }

    public void setMessagesModel(MessagesListModel messagesModel) {
        ListsMock.messagesModel = messagesModel;
    }

    public ListsMock(){
        friendsModel=new FriendsListModel();
        messagesModel=new MessagesListModel();
        selectedFriend = "";
    }

    public static ListsMock getInstance() {
        if(null != instance){
            return instance;
        }
        else{
            instance = new ListsMock();
            return instance;
        }
    }


}
