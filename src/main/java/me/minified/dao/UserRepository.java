package me.minified.dao;

import java.util.Map;
import java.util.Set;

import me.minified.entities.User;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

public interface UserRepository extends GraphRepository<User>, RelationshipOperationsRepository<User>{
	
	@Query("start a=node:User(email={0}), b=node:User(email={1}) match a-[r:FRIEND]-b return count(r) = 1")
	public Boolean isFriend(String sourceEmail, String targetEmail);
	
	@Query("start a=node:User(email={0}) " +
			"match a-[r:FRIEND]-b " +
			"return id(b) as id, b.name as name, b.email as email, b.avatarUrl as avatarUrl " +
			"order by b.name;")
	public Set<Map<String, Object>> findFriends(String email);
}
