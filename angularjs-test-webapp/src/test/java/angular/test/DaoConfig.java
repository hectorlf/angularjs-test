package angular.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import angular.dao.GuestbookDao;
import angular.dao.impl.GuestbookDaoImpl;

@Configuration
public class DaoConfig {

	@Bean
	public GuestbookDao guestbookDao() {
		return new GuestbookDaoImpl();
	}

}
