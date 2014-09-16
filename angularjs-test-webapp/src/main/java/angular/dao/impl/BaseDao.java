package angular.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import angular.model.PersistentObject;
import angular.model.PersistentObject_;


public abstract class BaseDao {

	@PersistenceContext
	private EntityManager em;

	public void setEm(EntityManager em) {
		this.em = em;
	}

	/*
	 * fluent accessors
	 */

	/**
	 * Fluent accessor for a single entity, by id or with property restrictions
	 */
	protected final <E extends PersistentObject> EntityAccessor<E> entity(Class<E> type) {
		return new EntityAccessorImpl<E>(type);
	}
	protected interface EntityAccessor<E extends PersistentObject> {
		public E by(Long id);
		public <T> PropertyBoundEntityAccessor<E> with(SingularAttribute<E, T> propertyName, T propertyValue);
	}
	protected interface PropertyBoundEntityAccessor<E extends PersistentObject> {
		public <T> PropertyBoundEntityAccessor<E> and(SingularAttribute<E, T> propertyName, T propertyValue);
		public E find();
	}
	private class EntityAccessorImpl<E extends PersistentObject> implements EntityAccessor<E>,PropertyBoundEntityAccessor<E> {
		private final Class<E> type;
		private CriteriaBuilder builder;
		private CriteriaQuery<E> q;
		private Root<E> r;
		private Map<Path<?>,Object> parameters;
		public EntityAccessorImpl(Class<E> type) { this.type = type; }
		@Override public E by(Long id) {
			assert(id != null);
			return em.find(type, id);
		}
		@Override public <T> PropertyBoundEntityAccessor<E> with(SingularAttribute<E, T> propertyName, T propertyValue) {
			assert(propertyName != null && propertyValue != null);
			builder = em.getCriteriaBuilder();
			q = builder.createQuery(type);
			r = q.from(type);
			parameters = new HashMap<Path<?>,Object>();
			parameters.put(r.get(propertyName), propertyValue);
			return this;
		}
		@Override public <T> PropertyBoundEntityAccessor<E> and(SingularAttribute<E, T> propertyName, T propertyValue) {
			assert(propertyName != null && propertyValue != null);
			parameters.put(r.get(propertyName), propertyValue);
			return this;
		}
		@Override public E find() throws NonUniqueResultException {
			Iterator<Entry<Path<?>,Object>> eit = parameters.entrySet().iterator();
			Entry<Path<?>,Object> entry = eit.next();
			Predicate conditions = builder.equal(entry.getKey(), entry.getValue());
			while (eit.hasNext()) {
				entry = eit.next();
				conditions = builder.and(conditions,builder.equal(entry.getKey(), entry.getValue()));
			}
			q.where(conditions);
			try { return em.createQuery(q).getSingleResult(); } catch(NoResultException nre) { return null; }
		}
	}

	/**
	 * Fluent accessor for querying a single entity, by named query or by jpql
	 */
	protected final <E extends PersistentObject> EntityQuerier<E> query(Class<E> type) {
		return new EntityQuerierImpl<E>(type);
	}
	protected interface EntityQuerier<E extends PersistentObject> {
		public EntityNamedQuerier<E> named(String queryName);
	}
	protected interface EntityNamedQuerier<E extends PersistentObject> {
		public List<E> list();
	}
	private class EntityQuerierImpl<E extends PersistentObject> implements EntityQuerier<E>, EntityNamedQuerier<E> {
		private final Class<E> type;
		private TypedQuery<E> query;
		public EntityQuerierImpl(Class<E> type) { this.type = type; }
		@Override public EntityNamedQuerier<E> named(String queryName) {
			assert(queryName != null && queryName.length() > 0);
			this.query = em.createNamedQuery(queryName, type);
			return this;
		}
		@Override public List<E> list() { return this.query.getResultList(); }
	}

	/*
	 * persistence-forwarded methods
	 */
	
	/**
	 * Gets an entity. Doesn't return proxys. May return null if non-existent object.
	 */
	protected final <T extends PersistentObject> T entityFor(Class<T> type, Long id) {
		assert(type != null && id != null);
		return em.find(type, id);
	}
	
	/**
	 * Gets an entity. May return a proxy. Never returns null, but proxy may throw an exception if object
	 * is non-existent. Best used when object is known to exist.
	 * 
	 * @throws EntityNotFoundException
	 */
	protected final <T extends PersistentObject> T referenceFor(Class<T> type, Long id) throws EntityNotFoundException {
		assert(type != null && id != null);
		return em.getReference(type, id);
	}

	/**
	 * Refresh object with new data from db, if any
	 */
	protected final <T extends PersistentObject> T refresh(T entity) {
		assert(entity != null);
		em.refresh(entity);
		return entity;
	}
	
	/**
	 * Persists the object
	 */
	protected final <T extends PersistentObject> void persist(T entity) {
		assert(entity != null);
		em.persist(entity);
	}

	/**
	 * Checks if an entity is persistent in this persistence context
	 */
	protected final boolean isPersistent(PersistentObject entity) {
		assert(entity != null);
		return em.contains(entity);
	}

	/**
	 * Deletes the object
	 */
	protected final <T extends PersistentObject> void remove(T entity) {
		assert(entity != null);
		em.remove(entity);
	}

	/*
	 * utility methods
	 */

	/**
	 * Lists all objects of type
	 */
	protected final <T extends PersistentObject> List<T> allOf(Class<T> type) {
		TypedQuery<T> q = em.createQuery(em.getCriteriaBuilder().createQuery(type));
		return q.getResultList();
	}

	/**
	 * Gets the first object of type, the one with the lowest id
	 */
	protected final <T extends PersistentObject> T first(Class<T> type) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> r = criteria.from(type);
		criteria.orderBy(builder.asc(r.get(PersistentObject_.id)));
		TypedQuery<T> q = em.createQuery(criteria);
		q.setMaxResults(1);
		return q.getSingleResult();
	}

	/*
	 * query methods
	 */

	/**
	 * Lists entities of type T having it's properties matched with values from params. Params can't be null.
	 * Property names and values arrays must be of the same size and have the same internal order.
	 */
	protected final <T extends PersistentObject> List<T> entitiesWith(Class<T> type, String[] properties, Object[] values) {
		assert(type != null && properties != null && values != null);
		assert(properties.length == values.length);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> q = builder.createQuery(type);
		Root<T> r = q.from(type);
		for (int i = 0; i < properties.length; i++) {
			//q.where(builder.)
		}
		List<T> results = em.createQuery(q).getResultList();
		return results;
	}

	/*
	 * hardmodes
	 */
	
	protected final <T extends PersistentObject> EntityGraph<T> entityGraphFor(Class<T> type) {
		return em.createEntityGraph(type);
	}

	protected final <T extends PersistentObject> TypedQuery<T> jpqlQueryFor(Class<T> type, String query) {
		return em.createQuery(query, type);
	}

	protected final Query nativeQueryFor(String query) {
		return em.createNativeQuery(query);
	}

	protected final CriteriaBuilder criteriaBuilder() {
		return em.getCriteriaBuilder();
	}

	protected final <T> List<T> query(CriteriaQuery<T> q) {
		assert(q != null);
		return em.createQuery(q).getResultList();
	}

	/**
	 * @throws NoResultException
	 * @throws NonUniqueResultException
	 */
	protected final <T> T singleResult(CriteriaQuery<T> q) {
		assert(q != null);
		return em.createQuery(q).getSingleResult();
	}

}
