package com.ferenc.chatinger;


public class User {

    private String userName;
    private String userEmail;
    private String password;
    private String userID;
    private String userToken;

    public User(){

    }

    public User(String userName, String userEmail, String password, String userID, String userToken) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.userID = userID;
        this.userToken = userToken;

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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                ", userID='" + userID + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}
