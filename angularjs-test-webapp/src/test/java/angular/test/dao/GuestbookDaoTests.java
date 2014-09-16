package angular.test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import angular.dao.GuestbookDao;
import angular.model.Sign;
import angular.test.BaseDaoTest;

public class GuestbookDaoTests extends BaseDaoTest {

	@Autowired
	private GuestbookDao guestbookDao;

	@Test
	public void testFindAll() {
		List<Sign> signings = guestbookDao.findAll();
		Assert.notEmpty(signings);
	}

	@Test
	public void testSaveSign() {
		List<Sign> signings = guestbookDao.findAll();
		int count = signings == null ? 0 : signings.size();
		Sign sign = new Sign();
		sign.setName("test");
		sign.setText("test");
		guestbookDao.saveSign(sign);
		List<Sign> newSignings = guestbookDao.findAll();
		Assert.isTrue(newSignings.size() == count + 1);
	}

}
