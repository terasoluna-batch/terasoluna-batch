package jp.terasoluna.fw.collector.db;

public class Order2Bean {
    private String ordrId = null;

    private String custId = null;

    private String custName = null;

    private String orderDate = null;

    private String detlId = null;

    private OrderDetailBean orderDetail = null;

    public String getOrdrId() {
        return ordrId;
    }

    public void setOrdrId(String ordrId) {
        this.ordrId = ordrId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDetlId() {
        return detlId;
    }

    public void setDetlId(String detlId) {
        this.detlId = detlId;
    }

    public OrderDetailBean getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailBean orderDetail) {
        this.orderDetail = orderDetail;
    }

}
