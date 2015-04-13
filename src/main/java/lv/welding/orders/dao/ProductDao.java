package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface ProductDao extends GenericDAO<ProductEntity, Long> {
	List<ProductEntity> getAllProducts();
	ProductEntity getProduct(String productNumber);
	List<ProductEntity> getProductsForModel(String productModelName, String productSizeName, String productMaterialName);
	Long getProductCountForModel(String productModelName, String productSizeName, String productMaterialName);
}
