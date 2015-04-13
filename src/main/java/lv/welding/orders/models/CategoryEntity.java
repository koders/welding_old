package lv.welding.orders.models;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="categories")
public class CategoryEntity extends ListDataModel<OrderEntity> implements Serializable, SelectableDataModel <CategoryEntity>{

    @Id
    @GeneratedValue
    private Long id;

	@Column(name="name")
	private String name;
    @Column(name="description")
    private String description;
    //Mother category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private CategoryEntity category;
    //Child categories
    @OneToMany(mappedBy="category", fetch = FetchType.EAGER, cascade={CascadeType.REMOVE})
    private List<CategoryEntity> subCategories;
    @OneToMany(fetch = FetchType.EAGER)
	private List<ProductEntity> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<CategoryEntity> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    @Override
    public CategoryEntity getRowData(String rowKey) {
        List<CategoryEntity> categories = (List<CategoryEntity>) getWrappedData();

        for(CategoryEntity c : categories) {
            if(c.getName().equals(rowKey))
                return c;
        }

        return null;
    }

    @Override
    public Object getRowKey(CategoryEntity categoryEntity) {
        return categoryEntity.getName();
    }
}
