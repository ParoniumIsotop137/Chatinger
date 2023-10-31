package com.ferenc.chatinger;


public class User {

    private String userName;
    private String userEmail;
    private String password;
    private String userID;
    private String lastMessage;

    public User(){

    }

    public User(String userName, String userEmail, String password, String userID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.userID = userID;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


}
