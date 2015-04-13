package lv.welding.orders.dao;

import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class CompanyJpaDao extends GenericJPADAO<CompanyEntity, Long> implements CompanyDao {
	
	public CompanyJpaDao() {
		super(CompanyEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<CompanyEntity> getCompanyList() {
		List<CompanyEntity> results;
		Query query = getEntityManager().createQuery("select c from CompanyEntity c");
		results = query.getResultList();
		return results;
	}

	@SuppressWarnings("unchecked")
	public CompanyEntity getCompany(String company) {
		List<CompanyEntity> result;
		
		Query query = getEntityManager().createQuery("select company from CompanyEntity company where company.name = :company");
		query.setParameter("company", company);
		result = query.getResultList();
		query.setMaxResults(1);
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public CompanyEntity getCompanyByAddress(String address) {
		List<CompanyEntity> result;
		
		Query query = getEntityManager().createQuery("select company from CompanyEntity company where company.address = :address");
		query.setParameter("address", address);
		result = query.getResultList();
		query.setMaxResults(1);
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}
	
	
	
}
