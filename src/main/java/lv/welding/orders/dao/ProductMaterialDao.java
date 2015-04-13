package lv.welding.orders.dao;

import lv.welding.orders.models.ProductMaterialEntity;
import lv.welding.orders.models.ProductMaterialEntity;
import lv.welding.orders.models.base.GenericDAO;

import java.util.List;

public interface ProductMaterialDao extends GenericDAO<ProductMaterialEntity, Long> {
	public List<ProductMaterialEntity> getAllProductMaterials();
	public ProductMaterialEntity getProductMaterial(String productMaterialName);
}
