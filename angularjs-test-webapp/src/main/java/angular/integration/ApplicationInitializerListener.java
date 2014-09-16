 package angular.integration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationInitializerListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializerListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("Shutting down application...");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("Starting up application...");
	}

}
