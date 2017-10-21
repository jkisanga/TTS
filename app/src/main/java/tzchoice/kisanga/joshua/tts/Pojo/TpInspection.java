
package tzchoice.kisanga.joshua.tts.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TpInspection {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tp_id")
    @Expose
    private Integer tpId;
    @SerializedName("checkpoint_id")
    @Expose
    private Integer checkpointId;
    @SerializedName("irregularity")
    @Expose
    private String irregularity;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("action_amount")
    @Expose
    private Double actionAmount;
    @SerializedName("receipt_no")
    @Expose
    private String receiptNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("inspected_by")
    @Expose
    private String inspectedBy;
    @SerializedName("desc")
    @Expose
    private Object desc;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    private String checkpoint_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTpId() {
        return tpId;
    }

    public void setTpId(Integer tpId) {
        this.tpId = tpId;
    }

    public Integer getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Integer checkpointId) {
        this.checkpointId = checkpointId;
    }

    public String getIrregularity() {
        return irregularity;
    }

    public void setIrregularity(String irregularity) {
        this.irregularity = irregularity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getActionAmount() {
        return actionAmount;
    }

    public void setActionAmount(Double actionAmount) {
        this.actionAmount = actionAmount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCheckpointName() {
        return checkpoint_name;
    }



}
