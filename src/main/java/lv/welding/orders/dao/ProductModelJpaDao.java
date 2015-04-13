package lv.welding.orders.dao;

import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.models.ProductModelEntity;
import lv.welding.orders.models.base.GenericJPADAO;

import javax.persistence.Query;
import java.util.List;

public class ProductModelJpaDao extends GenericJPADAO<ProductModelEntity, Long> implements ProductModelDao {

	public ProductModelJpaDao() {
		super(ProductModelEntity.class);
	}

    @Override
    public List<ProductModelEntity> getAllProductModels() {
        List<ProductModelEntity> results;
        Query query = getEntityManager().createQuery("select pm from ProductModelEntity pm");
        results = query.getResultList();
        return results;
    }

    @Override
    public ProductModelEntity getProductModel(String productModelName) {
        List<ProductModelEntity> result;

        Query query = getEntityManager().createQuery("select pm from ProductModelEntity pm where pm.name = :productModelName");
        query.setParameter("productModelName", productModelName);

        result = query.getResultList();

        if(result.isEmpty())
            return null;

        return result.get(0);
    }

}
