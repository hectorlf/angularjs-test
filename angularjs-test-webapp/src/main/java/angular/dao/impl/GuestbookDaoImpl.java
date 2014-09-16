package angular.dao.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import angular.dao.GuestbookDao;
import angular.model.Sign;

@Repository
public class GuestbookDaoImpl extends BaseDao implements GuestbookDao {

	private static final Logger logger = LoggerFactory.getLogger(GuestbookDaoImpl.class);
	
	@Override
	public List<Sign> findAll() {
		logger.debug("findAll()");
		List<Sign> results = allOf(Sign.class);
		if (results == null || results.size() == 0) return Collections.emptyList();
		return results;
	}

	@Override
	public void saveSign(Sign sign) {
		if (sign == null) throw new IllegalArgumentException("sign can't be null");
		logger.debug("saveSign() - {}", sign);
		persist(sign);
	}

}
