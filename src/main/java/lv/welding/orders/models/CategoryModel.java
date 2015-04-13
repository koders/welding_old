package lv.welding.orders.models;

import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

public class CategoryModel extends ListDataModel<CategoryEntity> implements SelectableDataModel<CategoryEntity>{


	 public CategoryModel() {}

     public CategoryModel(List<CategoryEntity> data) {
        super(data);  
     } 
	
	@SuppressWarnings("unchecked")
	public CategoryEntity getRowData(String rowKey) {
		 List<CategoryEntity> categories = (List<CategoryEntity>) getWrappedData();
         
	        for(CategoryEntity c : categories) {
	            if(c.getName().equals(rowKey))
	                return c;
	        }  
	          
	        return null;  
	}

	public Object getRowKey(CategoryEntity object) {
		return object.getName();
	}


}
