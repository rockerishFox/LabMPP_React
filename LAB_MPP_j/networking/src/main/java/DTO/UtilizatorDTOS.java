package DTO;

import java.io.Serializable;

public class UtilizatorDTOS  extends EntityDTOS<Integer> implements Serializable {
    private String username;
    private String password;


    public UtilizatorDTOS(Integer integer, String username, String password) {
        super(integer);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
