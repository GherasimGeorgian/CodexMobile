package chat.model;

import java.util.ArrayList;
import java.util.List;

public class MessagesListModel {
    private List<Message> messages;
    public MessagesListModel() {
        messages=new ArrayList<Message>();
    }

    public int getSize() {
        return messages.size();
    }

    public void cleanMesg(){
        messages.clear();
    }

    public ArrayList<Message> getAllMessages(){
        return new ArrayList<Message>(messages);
    }

    public Object getElementAt(int index) {
        return messages.get(index);
    }

    public void newMessage(Message mesg){
        messages.add(mesg);
    }
}
