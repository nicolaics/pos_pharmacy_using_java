import java.io.Serializable;

/* Administrator Class */
public class Admin implements Serializable {
    private static final long serialVersionUID = 28475620291L;
    
    private String username;
    private String password;

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
