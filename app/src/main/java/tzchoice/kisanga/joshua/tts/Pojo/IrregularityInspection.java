
package tzchoice.kisanga.joshua.tts.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrregularityInspection {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("irregularit")
    @Expose
    private String irregularit;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("volume")
    @Expose
    private Double volume;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("action_amount")
    @Expose
    private String actionAmount;
    @SerializedName("receipt_no")
    @Expose
    private String receiptNo;
    @SerializedName("inspected_by")
    @Expose
    private String inspectedBy;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tp_id")
    @Expose
    private Integer tpId;
    @SerializedName("checkpoint_id")
    @Expose
    private Integer CheckpointId;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIrregularit() {
        return irregularit;
    }

    public void setIrregularit(String irregularit) {
        this.irregularit = irregularit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionAmount() {
        return actionAmount;
    }

    public void setActionAmount(String actionAmount) {
        this.actionAmount = actionAmount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTpId() {
        return tpId;
    }

    public void setTpId(Integer tpId) {
        this.tpId = tpId;
    }

    public Integer getCheckpointId() {
        return CheckpointId;
    }

    public void setCheckpointId(Integer CheckpointId) {
        this.CheckpointId = CheckpointId;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}
