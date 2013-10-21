package me.minified.test.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

import me.minified.entities.User;
import me.minified.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:graphContext.xml" })
public class UserFriendTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserService service;
	
	@Autowired
	private Neo4jTemplate template;

	private final int len = 20;
	private DateFormat df = new SimpleDateFormat("HH:mm:ss:SS");

	@Test
	public void createUsers() {
		for (int i = 1; i <= len; i++) {
			User user = new User("mail" + i + "@gmail.com", "name-" + i,
					"http://cdn.example.com/" + i + ".jpg");
			User user0 = service.save(user);
			Assert.assertNotNull(user0.getId());
		}
	}

	@Test
	public void createFriends() {
		for (int i = 1; i <= len; i++) {
			User source = service.findByEmail("mail" + i + "@gmail.com");
			for (int j = i + 1; j <= len; j++) {
				User target = service.findByEmail("mail" + j + "@gmail.com");
				User source0 = service.createFriend(source, target);
				AssertJUnit.assertEquals(source, source0);
			}
		}
	}
	
	@Test
	public void seeFriends(){
		for (int i = 1; i <= 1; i++) {
			User source = service.findByEmail("mail" + i + "@gmail.com");
			System.out.println("----- Friend of : "+source.getName());
			for (User user : source.getFriends()) {
	            System.out.println(user);
            }
		}
	}
	
	@Test
	public void seeFriends2(){
		User source = service.findByEmail("mail" + 15 + "@gmail.com");
		System.out.println("----- Friend of : "+source.getName());
		for (User user : source.getFriends()) {
            System.out.println(user);
        }
	
	}
	
	@Test
	public void isFriend(){
		User source = service.findByEmail("mail19@gmail.com");
		User target = service.findByEmail("mail20@gmail.com");
		
		Assert.assertNotNull(source);
		Assert.assertNotNull(target);
		
		Assert.assertTrue(source.isFriend(target));
		Assert.assertTrue(service.isFriend(source.getEmail(), target.getEmail()));
	}
	
	@Test
	public void findFriends(){
		Set<Map<String, Object>> friends = service.findFriends("mail19@gmail.com");
		for (Map<String, Object> map : friends) {
	        System.out.println(map.values());
        }
	}

	@Test
	public void populateSocial() throws InterruptedException,BrokenBarrierException {
		final int conccurentCount = 1;
		final int maxTxPerThread = 30;
		final AtomicLong victimCounter = new AtomicLong(0);
		final CyclicBarrier barrier = new CyclicBarrier(conccurentCount + 1);
		final CountDownLatch latch = new CountDownLatch(conccurentCount);
		for (int i = 0; i < conccurentCount; i++) {
			final int idx = (maxTxPerThread * i);
			Thread t = new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					try {
						barrier.await();
						for (int j = idx; j < idx + maxTxPerThread; j++) {
							final Long userId = (long) (j + 1);
							User user = new User("mail" + userId + "@gmail.com", "name-" + userId,
									"http://cdn.example.com/" + userId + ".jpg");
							User user0 = service.save(user);
							victimCounter.incrementAndGet();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					latch.countDown();
				}
			});
			t.start();
		}
		barrier.await();
		long startTime = System.currentTimeMillis();
		latch.await();
//		template.getGraphDatabaseService().shutdown();
		System.out.println("Starting test at: "
				+ df.format(new Date(startTime)));
		long finishTime = System.currentTimeMillis();
		System.out.println("Test finished at: "
				+ df.format(new Date(finishTime)));
		System.out.println("Elapsed time: " + (finishTime - startTime)
				+ " ms, victimCount: " + victimCounter);
	}
}
