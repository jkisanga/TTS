package tzchoice.kisanga.joshua.tts.Pojo;

/**
 * Created by user on 1/28/2017.
 */

public class User {

    private String username;
    private int pin_code;
    private int checkpoint_id;
    private int user_id;
    private String checkpoint;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPin_code() {
        return pin_code;
    }

    public void setPin_code(int pin_code) {
        this.pin_code = pin_code;
    }

    public int getCheckpoint_id() {
        return checkpoint_id;
    }

    public void setCheckpoint_id(int checkpoint_id) {
        this.checkpoint_id = checkpoint_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }
}
