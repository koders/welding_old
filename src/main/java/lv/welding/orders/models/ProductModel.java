package lv.welding.orders.models;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class ProductModel extends ListDataModel<ProductEntity> implements SelectableDataModel<ProductEntity>{


	 public ProductModel() {}

     public ProductModel(List<ProductEntity> data) {
        super(data);  
     } 
	
	@SuppressWarnings("unchecked")
	public ProductEntity getRowData(String rowKey) {
		 List<ProductEntity> products = (List<ProductEntity>) getWrappedData();
         
	        for(ProductEntity p : products) {
	            if(p.getPno().equals(rowKey))
	                return p;
	        }  
	          
	        return null;  
	}

	public Object getRowKey(ProductEntity object) {
		return object.getPno();
	}


}
