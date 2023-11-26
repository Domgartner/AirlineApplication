package Code;

public class User {
    private String email;
    private String password;


    public User(String e, String p) {
        email = e;
        password = p;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
