package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface CompanyDao extends GenericDAO<CompanyEntity, Long> {
	public List<CompanyEntity> getCompanyList();
	public CompanyEntity getCompany(String company);
	public CompanyEntity getCompanyByAddress(String address);
}
