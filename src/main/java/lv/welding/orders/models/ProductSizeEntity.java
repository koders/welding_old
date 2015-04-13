package lv.welding.orders.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Rihards on 11.04.2015.
 */
@Entity
@Table(name="product_sizes")
public class ProductSizeEntity implements Serializable{

    private static final long serialVersionUID = 3943563383817805003L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name")
    private String name;

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

}
