package lv.welding.orders.dao;

import java.util.List;

import javax.persistence.Query;

import lv.welding.orders.models.UserEntity;
import lv.welding.orders.models.base.GenericJPADAO;

public class UserJpaDao extends GenericJPADAO<UserEntity, Long> implements UserDao {

	private static final String QUERY_USER = "select user from UserEntity user where user.username = :userName";
	private static final String QUERY_USER1 = "select user from UserEntity user";
	
	public UserJpaDao() {
		super(UserEntity.class);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByUsername(String userName) {
		
		UserEntity userEntity = null;
		
		Query query = getEntityManager().createQuery(QUERY_USER);
		query.setParameter("userName", userName);
		query.setMaxResults(1);
		List<UserEntity> result = query.getResultList();
		if(result.isEmpty())
			return null;
		userEntity = result.get(0);
		return userEntity;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<UserEntity> getAllUsers() {
		
		UserEntity userEntity = null;
		
		Query query = getEntityManager().createQuery(QUERY_USER1);
		query.setMaxResults(10);
		
		List<UserEntity> result = query.getResultList();
		
		return result;
	}
	
	
}
