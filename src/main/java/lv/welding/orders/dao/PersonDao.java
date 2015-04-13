package lv.welding.orders.dao;

import java.util.List;

import lv.welding.orders.models.PersonEntity;
import lv.welding.orders.models.base.GenericDAO;

public interface PersonDao extends GenericDAO<PersonEntity, Long> {
	public List<PersonEntity> getPersons();
	public PersonEntity getPerson(String name);
	public boolean checkIfPersonExists(String person);
}
