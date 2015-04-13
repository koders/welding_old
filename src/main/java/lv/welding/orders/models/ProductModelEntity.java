package lv.welding.orders.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rihards on 11.04.2015.
 */
@Entity
@Table(name="product_models")
public class ProductModelEntity implements Serializable{

    private static final long serialVersionUID = -4381648957177399783L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;

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
}
