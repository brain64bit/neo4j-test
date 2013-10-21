package me.minified.test.dao;

import me.minified.entities.User;
import me.minified.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:graphContext.xml"})
public class UserRepositoryTest extends AbstractTestNGSpringContextTests{
	@Autowired
	private UserService userService;
	
	private Long currentId;
	private String currentEmail;
	
	@Test
	public void save(){
		User user = new User("chuck@gmail.com", "chuck norris", "http://cdn.example.com/chuck.jpg");
		User user0 = userService.save(user);
		System.out.println(user0);
		Assert.assertNotNull(user0.getId());
		currentId = user0.getId(); 
		currentEmail = user0.getEmail();
	}
	
	@Test(dependsOnMethods={"save"})
	public void findById(){
		User user = userService.findById(currentId);
		System.out.println(user);
		Assert.assertNotNull(user.getId());
	}
	
	@Test(dependsOnMethods={"findById"})
	public void findByEmail(){
		User user = userService.findByEmail(currentEmail);
		System.out.println(user);
		Assert.assertNotNull(user.getEmail());
	}
	
	@Test(dependsOnMethods={"findByEmail"})
	public void delete(){
		User user = userService.findById(currentId);
		System.out.println(user);
		Assert.assertNotNull(user.getId());
		userService.delete(user.getId());
		Assert.assertNull(userService.findById(currentId));		
	}
	
}
