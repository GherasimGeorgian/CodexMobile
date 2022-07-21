package chat.model;

import java.util.ArrayList;
import java.util.List;

public class FriendsListModel{
    private List<Friend> friendsLoggedIn;

    public FriendsListModel() {
        friendsLoggedIn=new ArrayList<Friend>();
    }

    public ArrayList<Friend> getAllFriendsLoggedIn(){
        return new ArrayList<Friend>(friendsLoggedIn);
    }

    public int getSize() {
        return friendsLoggedIn.size();
    }

    public Object getElementAt(int index) {
        return friendsLoggedIn.get(index);
    }

    public void friendLoggedIn(Friend user){
        friendsLoggedIn.add(user);
    }

    public void friendLoggedOut(User user){
        friendsLoggedIn.remove(user);
    }
}
