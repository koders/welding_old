package lv.welding.orders.services.impl;

import java.util.List;

import lv.welding.orders.dao.CompanyDao;
import lv.welding.orders.dao.PersonDao;
import lv.welding.orders.dao.ProductDao;
import lv.welding.orders.models.CompanyEntity;
import lv.welding.orders.models.PersonEntity;
import lv.welding.orders.models.Product;
import lv.welding.orders.models.ProductEntity;
import lv.welding.orders.services.MainService;

public class MainServiceImpl implements MainService {

	private CompanyDao companyDao;
	private ProductDao productDao;
	private PersonDao personDao;

	public void nextNumber(CompanyEntity company) {
		if(company == null || company.getOnr() == null || company.getOnr().equals(""))
			return;
		Long x = Long.parseLong(company.getOnr())+1;
		company.setOnr(x.toString());
		companyDao.update(company);
	}

	public CompanyDao getCompanyDao() {
		return companyDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	public void saveNewPerson(String person) {
		if(!personDao.checkIfPersonExists(person)) {
			PersonEntity p = new PersonEntity();
			p.setName(person);
			personDao.save(p);		
		}
	}
	
	public void saveNewProducts(List<Product> products) {
		for(Product p: products) {
			if(productDao.getProduct(p.getPno()) == null) {
				ProductEntity p1 = new ProductEntity();
				p1.setPno(p.getPno());
				p1.setDesc(p.getDesc());
				productDao.save(p1);
			}
		}
	}

	public CompanyEntity getCompanyByName(String company) {
		return companyDao.getCompany(company);
	}
	
	public CompanyEntity getCompanyByAddress(String address) {
		return companyDao.getCompanyByAddress(address);
	}
	
	public String getCompanyAddress(String company) {
		CompanyEntity c = companyDao.getCompany(company);
		if(c == null)
			return null;
		return c.getAddress();
	}
	
	
	
	
	
	
	
	
	
	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	
}
