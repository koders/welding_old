package lv.welding.orders.dao;

import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.TermsEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class TermsJpaDao extends GenericJPADAO<TermsEntity, Long> implements TermsDao {
	
	public TermsJpaDao() {
		super(TermsEntity.class);
	}

	@SuppressWarnings("unchecked")
	public TermsEntity getTerms(String terms) {
		List<TermsEntity> result;
		
		Query query = getEntityManager().createQuery("select t from TermsEntity t where t.terms = :terms");
		query.setParameter("terms", terms);
		
		result = query.getResultList();
		
		if(result.isEmpty())
			return null;
		
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<TermsEntity> getAllTerms() {
		List<TermsEntity> results;
		Query query = getEntityManager().createQuery("from TermsEntity");
		results = query.getResultList();
		return results;
	}
	
	
	
}
