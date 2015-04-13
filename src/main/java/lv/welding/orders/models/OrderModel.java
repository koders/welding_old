package lv.welding.orders.models;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class OrderModel extends ListDataModel<OrderEntity> implements SelectableDataModel<OrderEntity>{

	
	 public OrderModel() {}  
	  
     public OrderModel(List<OrderEntity> data) {  
        super(data);  
     } 
	
	@SuppressWarnings("unchecked")
	public OrderEntity getRowData(String rowKey) {
		 List<OrderEntity> orders = (List<OrderEntity>) getWrappedData();  
         
	        for(OrderEntity o : orders) {  
	            if(o.getOrderData().getOcnr().equals(rowKey))  
	                return o;  
	        }  
	          
	        return null;  
	}

	public Object getRowKey(OrderEntity object) {
		return object.getOrderData().getOcnr();
	}


}
