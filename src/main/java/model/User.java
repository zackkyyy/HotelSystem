package model;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Zacky Kharboutli
 * @Date: 2018-04-16
 * @Project : HotelSystem
 */


public class User {
    private String name;
    private String LastName;
    private String userName;
    private String password;

    public User(String name, String lastName, String userName, String password) {
        this.name = name;
        this.LastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    public User() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
