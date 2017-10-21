
package tzchoice.kisanga.joshua.tts.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TpCheckpoint {

    @SerializedName("tp")
    @Expose
    private Tp tp;
    @SerializedName("checkpoints")
    @Expose
    private List<Checkpoint> checkpoints = null;

    public Tp getTp() {
        return tp;
    }

    public void setTp(Tp tp) {
        this.tp = tp;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

}
