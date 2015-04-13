package lv.welding.orders.models.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

/**
 * GenericDAO interface abstract implementation
 * 
 */
@Transactional
public abstract class GenericJPADAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private Class<T> persistentClass;
	
	private EntityManager entityManager;
	
	public GenericJPADAO(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	@Transactional(readOnly=true)
	public T findById(ID id) {
		T entity = (T) getEntityManager().find(getPersistentClass(), id);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<T> findAll() {
		return getEntityManager()
			.createQuery("select x from " + getPersistentClass().getSimpleName() + " x")
			.getResultList();
	}
	
	public T save(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}
	
	public T update(T entity) {
		T mergedEntity = getEntityManager().merge(entity);
		return mergedEntity;
	}
	
	public void delete(T entity) {
		if (BaseEntity.class.isAssignableFrom(persistentClass)) {
			getEntityManager().remove(
					getEntityManager().getReference(entity.getClass(), 
							((BaseEntity)entity).getId()));
		} else {
			entity = getEntityManager().merge(entity);
			getEntityManager().remove(entity);
		}
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void flush() {
		getEntityManager().flush();
	}
	
}
