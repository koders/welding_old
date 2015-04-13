package lv.welding.orders.dao;

import lv.welding.orders.models.ProductSizeEntity;
import lv.welding.orders.models.base.GenericDAO;

import java.util.List;

public interface ProductSizeDao extends GenericDAO<ProductSizeEntity, Long> {
	public List<ProductSizeEntity> getAllProductSizes();
	public ProductSizeEntity getProductSize(String productSizeName);
}
