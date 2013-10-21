package me.minified.service;

import java.util.Map;
import java.util.Set;

import me.minified.entities.Friend;
import me.minified.entities.User;

public interface UserService {
	User save(User user);
	User findById(Long id);
	User findByEmail(String email);
	void delete(Long graphId);
	User createFriend(User source, User target);
	Friend createFriendBetween(User source, User target);
	Boolean isFriend(String sourceEmail, String targetEmail);
	Set<Map<String, Object>> findFriends(String email);
}
