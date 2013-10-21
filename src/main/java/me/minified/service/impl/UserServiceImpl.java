package me.minified.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import me.minified.dao.UserRepository;
import me.minified.entities.Friend;
import me.minified.entities.User;
import me.minified.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repo;
	
	public User save(User user) {
		return repo.save(user);
	}

	public User findById(Long id) {
		return repo.findOne(id);
	}

	public User findByEmail(String email) {
		return repo.findByPropertyValue("email", email);
	}

	public void delete(Long graphId) {
		repo.delete(graphId);
	}

	public User createFriend(User source, User target) {
		source.addFriend(target);
		return repo.save(source);
	}

	public Friend createFriendBetween(User source, User target) {
		return repo.createRelationshipBetween(source, target, Friend.class, "FRIEND");
	}

	@Override
    public Boolean isFriend(String sourceEmail, String targetEmail) {
	    return repo.isFriend(sourceEmail, targetEmail);
    }

	@Override
    public Set<Map<String, Object>> findFriends(String email) {
	    return repo.findFriends(email);
    }
	
}
