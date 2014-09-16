package angular.integration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import angular.dao.GuestbookDao;
import angular.dao.impl.GuestbookDaoImpl;
import angular.service.GuestbookService;
import angular.service.impl.GuestbookServiceImpl;

@Configuration
@EnableTransactionManagement
public class AppConfiguration {

	private static String[] messageSourceBasenames = { "applicationResources" };

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.addScript("classpath:db-ddl.sql")
			.addScript("classpath:db-initial-data.sql")
			.build();
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasenames(messageSourceBasenames);
		ms.setFallbackToSystemLocale(false);
		return ms;
	}


	// dao layer

	@Bean
	public GuestbookDao guestbookDao() {
		return new GuestbookDaoImpl();
	}

	// service layer
	
	@Bean
	public GuestbookService guestbookService() {
		return new GuestbookServiceImpl(guestbookDao());
	}

}
