package me.minified;

import me.minified.entities.User;
import me.minified.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("graphContext.xml");
		System.out.println("Startup ... ");
		User u = new User("chuck@xxx.com", "chuck norris", "http://example.com/foobar.jpg");
		UserService service = (UserService) ctx.getBean("userService");
		service.save(u);
	}

}
