package server.obj;

public class User {

    private String name;
    private String password;
    private boolean canRegisterToken;
    private boolean canGetCardNumber;

    public User() {
        this.name = "";
        this.password = "";
        this.canRegisterToken = false;
        this.canGetCardNumber = false;
    }

    public User(String name, String password, boolean canRegisterToken, boolean canGetCardNumber) {
        this.name = name;
        this.password = password;
        this.canRegisterToken = canRegisterToken;
        this.canGetCardNumber = canGetCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCanRegisterToken() {
        return canRegisterToken;
    }

    public void setCanRegisterToken(boolean canRegisterToken) {
        this.canRegisterToken = canRegisterToken;
    }

    public boolean isCanGetCardNumber() {
        return canGetCardNumber;
    }

    public void setCanGetCardNumber(boolean canGetCardNumber) {
        this.canGetCardNumber = canGetCardNumber;
    }

}
