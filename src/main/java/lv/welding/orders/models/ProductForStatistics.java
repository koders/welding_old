package lv.welding.orders.models;

public class ProductForStatistics {
	
	private String category;
	private Integer pcs;
	private Integer withHeating;
	
	public ProductForStatistics(String category, Integer pcs, Integer withHeating) {
		this.category = category;
		this.pcs = pcs;
		this.withHeating = withHeating;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getPcs() {
		return pcs;
	}
	public void setPcs(Integer pcs) {
		this.pcs = pcs;
	}
	public Integer getWithHeating() {
		return withHeating;
	}
	public void setWithHeating(Integer withHeating) {
		this.withHeating = withHeating;
	}
	
}
