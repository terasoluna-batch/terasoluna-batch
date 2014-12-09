package jp.terasoluna.fw.collector.db;

public class OrderDetailBean {
    private String detlId = null;

    private String prodId = null;

    private String prodName = null;

    private String quantity = null;

    private String amount = null;

    public String getDetlId() {
        return detlId;
    }

    public void setDetlId(String detlId) {
        this.detlId = detlId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
