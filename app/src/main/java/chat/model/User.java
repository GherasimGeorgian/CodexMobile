package chat.model;



import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class User implements Comparable<User>, Serializable, Identifiable<String> {
    private String username, passwd, name;
    private Set<User> friends;
    private Date lastTimeOnline = Calendar.getInstance().getTime();

    public User() {

    }
    public User(String username) {
        this.username = username;
    }
    public User(String username,String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public User(String username,Date lastTimeOnline) {
        this(username,"","",lastTimeOnline);
    }
    public User(String username, String passwd,String name) {
        this.username = username;
        this.passwd = passwd;
        this.name=name;
    }

    public User(String username, String passwd,Date lastTimeOnline) {
        this(username,passwd,"",lastTimeOnline);
    }

    public User(String username, String passwd, String name,Date lastTimeOnline) {
        this.username = username;
        this.passwd = passwd;
        this.name = name;
        this.lastTimeOnline =lastTimeOnline;
        friends=new TreeSet<User>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastTimeOnline() {
        return lastTimeOnline;
    }

    public void setLastTimeOnline(Date lastTimeOnline) {
        this.lastTimeOnline = lastTimeOnline;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getId() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        username = id;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }


    public Iterable<User> getFriends(){
        return friends;
    }
    public void addFriend(User u){
        friends.add(u);

    }
    public void removeFriend(User u){
        friends.add(u);
    }


    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public boolean equals(Object obj) {
        if (obj instanceof User){
            User u=(User)obj;
            return username.equals(u.username);
        }
        return false;
    }

    @Override
    public String toString() {
     /*  StringBuilder friendsString=new StringBuilder();
        for (User user : friends){
            friendsString.append(user.getName());
            friendsString.append(", ");
        }*/
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                // ", friends='"+friendsString+'\''+
                '}';
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}