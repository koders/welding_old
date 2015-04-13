package lv.welding.orders.dao;

import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.CountryEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class CountryJpaDao extends GenericJPADAO<CountryEntity, Long> implements CountryDao {
	
	public CountryJpaDao() {
		super(CountryEntity.class);
	}

	@SuppressWarnings("unchecked")
	public CountryEntity getCountry(String country) {
		List<CountryEntity> result;
		Query query = getEntityManager().createQuery("select country from CountryEntity country where country.name = :country");
		query.setParameter("country", country);
		result = query.getResultList();
		query.setMaxResults(1);
		if(result.isEmpty())
			return null;
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<CountryEntity> getCountries() {
		List<CountryEntity> results;
		Query query = getEntityManager().createQuery("from CountryEntity");
		results = query.getResultList();
		return results;
	}

}
