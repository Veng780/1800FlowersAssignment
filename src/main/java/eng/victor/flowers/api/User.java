package eng.victor.flowers.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user")
public class User {
	@Id	
	private Integer id;
	
	@OneToMany(
	        cascade = CascadeType.ALL, 
	        orphanRemoval = true
	    )
	@JsonIgnore
	List<FlowerComment> flowerComments;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public List<FlowerComment> getFlowerStories() {
		return flowerComments;
	}
	
	
	
	
}
