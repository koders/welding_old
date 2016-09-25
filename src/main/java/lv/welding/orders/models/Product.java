package lv.welding.orders.models;

public class Product {
	
	private String pno;
	private String desc;
	private String pcs;
	private String price;
	private String stockPrice;
	private String tprice;
	private String stockTPrice;
	private String weight;
	private String totalWeight;
	private String packing;
	private String idno;
	
	
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPcs() {
		return pcs;
	}
	public void setPcs(String pcs) {
		this.pcs = pcs;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getStockPrice() {
    if(stockPrice == null) {
      stockPrice = price;
    }
		return stockPrice;
	}

	public void setStockPrice(String stockPrice) {
		this.stockPrice = stockPrice;
	}

	public String getTprice() {
		return tprice;
	}
	public void setTprice(String tprice) {
		this.tprice = tprice;
	}

	public String getStockTPrice() {
    if(stockTPrice == null) {
      stockTPrice = tprice;
    }
		return stockTPrice;
	}

	public void setStockTPrice(String stockTPrice) {
		this.stockTPrice = stockTPrice;
	}

	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
}
