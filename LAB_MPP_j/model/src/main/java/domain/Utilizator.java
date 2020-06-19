package domain;

public class Utilizator extends Entity<Integer> {
    private String username;
    private String password;


    public Utilizator(Integer integer, String username, String password) {
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
