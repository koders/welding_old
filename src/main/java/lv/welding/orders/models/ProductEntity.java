package lv.welding.orders.models;

import lv.welding.orders.models.base.BaseEntity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="products")
public class ProductEntity extends BaseEntity implements Serializable{
	
	private String pno;
	@Column(name="[desc]")
	private String desc;
    @Column(columnDefinition = "int default 0")
	private Integer inStock = 0;
    @Column(columnDefinition = "int default 0")
	private Integer totalShipped = 0;

    @Transient
    private Integer addToStock = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductModelEntity productModel;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductSizeEntity productSize;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductMaterialEntity productMaterial;
	
	
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

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Integer getAddToStock() {
        return addToStock;
    }

    public void setAddToStock(Integer addToStock) {
        this.addToStock = addToStock;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Integer getTotalShipped() {
        return totalShipped;
    }

    public void setTotalShipped(Integer totalShipped) {
        this.totalShipped = totalShipped;
    }

    public ProductModelEntity getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModelEntity productModel) {
        this.productModel = productModel;
    }

    public ProductSizeEntity getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSizeEntity productSize) {
        this.productSize = productSize;
    }

    public ProductMaterialEntity getProductMaterial() {
        return productMaterial;
    }

    public void setProductMaterial(ProductMaterialEntity productMaterial) {
        this.productMaterial = productMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductEntity))
            return false;
        if (o == this)
            return true;

        ProductEntity that = (ProductEntity) o;
        if(this.pno == null && that.pno == null)
            return true;
        if(this.pno.equals(that.pno))
            return true;

        return false;
    }

}
