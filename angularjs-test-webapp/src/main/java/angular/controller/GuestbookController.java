package angular.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import angular.model.Sign;
import angular.service.GuestbookService;

@Controller
public class GuestbookController {

	private static final Logger logger = LoggerFactory.getLogger(GuestbookController.class);

	@Autowired
	private GuestbookService guestbookService;
	
	@RequestMapping(value="/guestbook/test")
	public String fail() {
		logger.debug("Going into GuestbookController.test()");
		return "/test.html";
	}
	
	@RequestMapping(value="/guestbook/list", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Sign> list() {
		logger.debug("Going into GuestbookController.list()");
		List<Sign> results = guestbookService.findAll();
		return results;
	}

	@RequestMapping(value="/guestbook/save", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody Sign save(@RequestBody Sign sign) {
		logger.debug("Going into GuestbookController.save()");
		if (sign == null || sign.getName() == null || sign.getText() == null) throw new IllegalArgumentException("Sign object or its properties are null");
		guestbookService.saveSign(sign);
		return sign;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleIllegalArgumentException(IllegalArgumentException iae) {
		logger.debug("Caught IllegalArgumentException: {}", iae.getMessage());
		return iae.getMessage();
	}

	public void setGuestbookService(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}

}
