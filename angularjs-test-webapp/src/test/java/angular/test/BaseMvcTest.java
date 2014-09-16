package angular.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("file:.")
@ContextConfiguration(classes={MvcConfig.class})
public abstract class BaseMvcTest {

	@Autowired
	private WebApplicationContext wac;

	protected MockMvc mockMvc;

	@Autowired
	protected MockServletContext servletContext;

	@Autowired
	protected MockHttpSession session;

	@Autowired
	protected MockHttpServletRequest request;

	@Autowired
	protected MockHttpServletResponse response;

	@Autowired
	protected ServletWebRequest webRequest;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

}
