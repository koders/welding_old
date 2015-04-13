package lv.welding.orders.dao;

import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.PersonEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class PersonJpaDao extends GenericJPADAO<PersonEntity, Long> implements PersonDao {
	
	public PersonJpaDao() {
		super(PersonEntity.class);
	}

	@SuppressWarnings("unchecked")
	public List<PersonEntity> getPersons() {
		List<PersonEntity> results;
		Query query = getEntityManager().createQuery("from PersonEntity");
		results = query.getResultList();
		return results;
	}
	
	public PersonEntity getPerson(String name) {
		Query query = getEntityManager().createQuery("select p from PersonEntity p where p.name = :name");
		query.setParameter("name", name);
		if(query.getResultList().isEmpty())
			return null;
		else
			return (PersonEntity)query.getResultList().get(0);
	}

	public boolean checkIfPersonExists(String person) {
		Query query = getEntityManager().createQuery("select p from PersonEntity p where p.name = :name");
		query.setParameter("name", person);
		if(query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	
	
	
}
