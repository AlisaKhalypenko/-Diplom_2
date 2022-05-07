
public class User {
    public String firstName;
    public String email;
    public String password;
    public String accessToken;

    public User(){
    }

    public User(String email, String password, String firstName, String accessToken){
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword (String password){
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName (String firstName){
        this.firstName = firstName;
    }

    public String getAccessToken(){
        return  accessToken;
    }
    public void setAccessToken (String accessToken){
        this.accessToken = accessToken;
    }

}
