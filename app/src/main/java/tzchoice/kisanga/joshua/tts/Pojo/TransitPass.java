
package tzchoice.kisanga.joshua.tts.Pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransitPass {

    @SerializedName("tp")
    @Expose
    private Tp tp;
    @SerializedName("checkpoints")
    @Expose
    private List<Checkpoint> checkpoints = null;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
