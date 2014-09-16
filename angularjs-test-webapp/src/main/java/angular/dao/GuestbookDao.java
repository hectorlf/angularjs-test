package angular.dao;

import java.util.List;

import angular.model.Sign;

public interface GuestbookDao {

	List<Sign> findAll();

	void saveSign(Sign sign);

}
