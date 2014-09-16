package angular.service;

import java.util.List;

import angular.model.Sign;

public interface GuestbookService {

	List<Sign> findAll();

	void saveSign(Sign sign);

}
