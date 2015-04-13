package lv.welding.orders.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lv.welding.orders.models.base.BaseEntity;

@Entity
@Table(name="invoices")
public class InvoiceEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long number;
	private Long year;
	private String orderNumber;
	private String country;
	@Column(columnDefinition="TEXT")
	private String seller;
	@Transient
	private CompanyEntity sellerInfo = new CompanyEntity();
	@Column(columnDefinition="TEXT")
	private String buyer;
	@Transient
	private CompanyEntity buyerInfo = new CompanyEntity();
	@Column(columnDefinition="TEXT")
	private String delivery;
	@Transient
	private CompanyEntity deliveryInfo = new CompanyEntity();
	private String purchaseNumber;
	private String marking;
	private String currency;
	private String ref;
	private String contactPerson;
	private String contactTel;
	@Column(columnDefinition="TEXT")
	private String products;
	@Transient
	private List<Product> productData;
	
	private Double nettoWeight = 0.0;
	private boolean packingPlastic;
	private boolean packingOther;
	private Double plasticWeight = 0.0;
	private String otherType = "wood pallet";
	private Double otherWeight = 0.0;
	private String size;
	private String pcs;
	private Double bruttoWeight = 0.0;
	
	private String creationDate;
	private String deliveryDate;
	private String termsDelivery;
	private String termsPayment;
	private String dueDate;
	@Column(columnDefinition="TEXT")
	private String specification;
	private String pvn;
	private String pvnSpecify;
	private String hsCode;
	
	private Double amount = 0.0;
	private Double vat;
	private Double totalAmount = 0.0;
	private Date created;
	
	private String createdBy;

    @Transient
    private Integer shippedProductCount = 0;
	
	
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public CompanyEntity getSellerInfo() {
		return sellerInfo;
	}
	public void setSellerInfo(CompanyEntity sellerInfo) {
		this.sellerInfo = sellerInfo;
	}
	public CompanyEntity getBuyerInfo() {
		return buyerInfo;
	}
	public void setBuyerInfo(CompanyEntity buyerInfo) {
		this.buyerInfo = buyerInfo;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public String getMarking() {
		return marking;
	}
	public void setMarking(String marking) {
		this.marking = marking;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
	public List<Product> getProductData() {
		return productData;
	}
	public void setProductData(List<Product> productData) {
		this.productData = productData;
	}
	public Double getNettoWeight() {
		return nettoWeight;
	}
	public void setNettoWeight(Double nettoWeight) {
		this.nettoWeight = nettoWeight;
	}
	public Double getPlasticWeight() {
		return plasticWeight;
	}
	public void setPlasticWeight(Double plasticWeight) {
		this.plasticWeight = plasticWeight;
	}
	public Double getOtherWeight() {
		return otherWeight;
	}
	public void setOtherWeight(Double otherWeight) {
		this.otherWeight = otherWeight;
	}
	public Double getBruttoWeight() {
		return bruttoWeight;
	}
	public void setBruttoWeight(Double bruttoWeight) {
		this.bruttoWeight = bruttoWeight;
	}
	public String getTermsDelivery() {
		return termsDelivery;
	}
	public void setTermsDelivery(String termsDelivery) {
		this.termsDelivery = termsDelivery;
	}
	public String getTermsPayment() {
		return termsPayment;
	}
	public void setTermsPayment(String termsPayment) {
		this.termsPayment = termsPayment;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getPvn() {
		return pvn;
	}
	public void setPvn(String pvn) {
		this.pvn = pvn;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getVat() {
		return vat;
	}
	public void setVat(Double vat) {
		this.vat = vat;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Long getYear() {
		return year;
	}
	public void setYear(Long year) {
		this.year = year;
	}
	public String getOtherType() {
		return otherType;
	}
	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public boolean isPackingPlastic() {
		return packingPlastic;
	}
	public void setPackingPlastic(boolean packingPlastic) {
		this.packingPlastic = packingPlastic;
	}
	public boolean isPackingOther() {
		return packingOther;
	}
	public void setPackingOther(boolean packingOther) {
		this.packingOther = packingOther;
	}
	public String getPvnSpecify() {
		return pvnSpecify;
	}
	public void setPvnSpecify(String pvnSpecify) {
		this.pvnSpecify = pvnSpecify;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDelivery() {
		return delivery;
	}
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}
	public CompanyEntity getDeliveryInfo() {
		if(deliveryInfo == null)
			deliveryInfo = new CompanyEntity();
		return deliveryInfo;
	}
	public void setDeliveryInfo(CompanyEntity deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
	public String getPcs() {
		return pcs;
	}
	public void setPcs(String pcs) {
		this.pcs = pcs;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
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
