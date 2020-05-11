package util;


import javafx.scene.control.Button;

import java.util.SplittableRandom;

public class UserTM {
    private String userName;
    private String name;
    private String role;
    private Button delet;

    public UserTM() {
    }

    public UserTM(String userName, String name, String role, Button delet) {
        this.userName = userName;
        this.name = name;
        this.role = role;
        this.delet = delet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Button getDelet() {
        return delet;
    }

    public void setDelet(Button delet) {
        this.delet = delet;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", delet=" + delet +
                '}';
    }
}
