package lv.welding.orders.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="orders")
public class OrderEntity extends BaseEntity {

    private static final long serialVersionUID = -7778748446322369800L;
	private Integer t;
    private Integer type;
    @Lob
	private String data;
	@Transient
	private OrderData orderData = new OrderData();
	private Timestamp ct;
	private Timestamp mt;
	private String createdBy;
    @Transient
    private Integer shippedProductCount = 0;
	
	
	public Integer getT() {
		return t;
	}
	public void setT(Integer t) {
		this.t = t;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getCt() {
		return ct;
	}
	public void setCt(Timestamp ct) {
		this.ct = ct;
	}
	public Timestamp getMt() {
		return mt;
	}
	public void setMt(Timestamp mt) {
		this.mt = mt;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public OrderData getOrderData() {
		return orderData;
	}
	public void setOrderData(OrderData orderData) {
		this.orderData = orderData;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

    public Integer getShippedProductCount() {
        return shippedProductCount;
    }

    public void setShippedProductCount(Integer shippedProductCount) {
        this.shippedProductCount = shippedProductCount;
    }
}
