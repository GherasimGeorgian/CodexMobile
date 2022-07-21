package domain;

import android.accounts.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Client implements Serializable {
    public String userName;
    public String access_token;
    public String refresh_token;
    public String email;
    public String firstName;
    public String  lastName;
    public Date birthDay;
    public String phoneNumber;
    public AccountType accountType;
    public String password;

    public Client(String userName, String access_token, String refresh_token) {
        this.userName = userName;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }
    public Client(){

    }

    public Client(String userName, String email, String firstName, String lastName, Date birthDay, String phoneNumber, AccountType accountType) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;

    }

    public static Client getClientWithouToken(JSONObject jsonObject) throws Exception{
        String username = jsonObject.getString("userName");
        String email = jsonObject.getString("email");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");

        Date birthDay = null;
        try {
            birthDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(jsonObject.getString("birthDay"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String phoneNumber = jsonObject.getString("phoneNumber");
        AccountType accountType = AccountType.valueOf(jsonObject.getString("accountType"));
        Client user = new Client(username, email, firstName, lastName, birthDay, phoneNumber, accountType);

        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Client(String userName, String access_token, String refresh_token, String email, String firstName, String lastName, Date birthDay, String phoneNumber, AccountType accountType, String password) {
        this.userName = userName;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.password = password;
    }

    public Client(String userName, String access_token, String refresh_token, String email, String firstName, String lastName, Date birthDay, String phoneNumber, AccountType accountType) {
        this.userName = userName;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
    }

    public static Client getClient(JSONObject jsonObject) throws JSONException {
        String username = jsonObject.getString("userName");
        String access_token = jsonObject.getString("access_token");
        String refresh_token = jsonObject.getString("refresh_token");
        String email = jsonObject.getString("email");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");

        Date birthDay = null;
        try {
            birthDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(jsonObject.getString("birthDay"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String phoneNumber = jsonObject.getString("phoneNumber");
        AccountType accountType = AccountType.valueOf(jsonObject.getString("accountType"));
        Client user;
        if(email == null){
            user = new Client(username,access_token,refresh_token);
        }
        else{
            user = new Client(username, access_token, refresh_token, email, firstName, lastName, birthDay, phoneNumber, accountType);
        }
        return user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}