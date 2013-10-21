package me.minified.entities;

import java.util.Date;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class Friend {
	private Date createdAt;
	
	@StartNode private User source;
	@EndNode private User target;
	
	public Friend() {
		this.createdAt = new Date();
	}
}
