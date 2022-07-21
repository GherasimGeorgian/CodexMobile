package chat.network.dto;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {
    private String id;
    private String passwd;
    private Date lastTimeOnline;

    public UserDTO(String id,Date lastTimeOnline) {
        this(id,"",lastTimeOnline);
    }

    public UserDTO(String id, String passwd,Date lastTimeOnline) {
        this.id = id;
        this.passwd = passwd;
        this.lastTimeOnline = lastTimeOnline;
    }

    public void setLastTimeOnline(Date lastTimeOnline) {
        this.lastTimeOnline = lastTimeOnline;
    }

    public Date getLastTimeOnline() {
        return lastTimeOnline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString(){
        return "UserDTO["+id+' '+passwd+"]";
    }
}
