package lv.welding.orders.dao;

import lv.welding.orders.models.ProductMaterialEntity;
import lv.welding.orders.models.base.GenericJPADAO;

import javax.persistence.Query;
import java.util.List;

public class ProductMaterialJpaDao extends GenericJPADAO<ProductMaterialEntity, Long> implements ProductMaterialDao {

	public ProductMaterialJpaDao() {
		super(ProductMaterialEntity.class);
	}

    @Override
    public List<ProductMaterialEntity> getAllProductMaterials() {
        List<ProductMaterialEntity> results;
        Query query = getEntityManager().createQuery("select pm from ProductMaterialEntity pm");
        results = query.getResultList();
        return results;
    }

    @Override
    public ProductMaterialEntity getProductMaterial(String productMaterialName) {
        List<ProductMaterialEntity> result;

        Query query = getEntityManager().createQuery("select pm from ProductMaterialEntity pm where pm.name = :productMaterialName");
        query.setParameter("productMaterialName", productMaterialName);

        result = query.getResultList();

        if(result.isEmpty())
            return null;

        return result.get(0);
    }

}
