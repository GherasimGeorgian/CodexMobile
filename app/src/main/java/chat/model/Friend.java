package chat.model;

import android.graphics.Bitmap;

public class Friend {
    private User user;
    private Boolean online;
    private Boolean isExpanded;
    private Bitmap image;

    public Friend(User user, Boolean online, Boolean isExpanded) {
        this.user = user;
        this.online = online;
        this.isExpanded = isExpanded;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }
}
