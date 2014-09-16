package angular.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import angular.dao.GuestbookDao;
import angular.model.Sign;
import angular.service.GuestbookService;

@Service
public class GuestbookServiceImpl implements GuestbookService {
	
	private GuestbookDao guestbookDao;

	@Inject
	public GuestbookServiceImpl(GuestbookDao guestbookDao) {
		this.guestbookDao = guestbookDao;
	}

	@Override
	public List<Sign> findAll() {
		return guestbookDao.findAll();
	}

	@Override
	public void saveSign(Sign sign) {
		guestbookDao.saveSign(sign);
	}

}
