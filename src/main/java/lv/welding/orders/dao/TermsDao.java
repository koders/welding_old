package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.TermsEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface TermsDao extends GenericDAO<TermsEntity, Long> {

	public List<TermsEntity> getAllTerms();
	public TermsEntity getTerms(String terms);
}
