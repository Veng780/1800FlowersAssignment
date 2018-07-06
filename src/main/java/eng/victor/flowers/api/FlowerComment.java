package eng.victor.flowers.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Representing the elements found in  http://jsonplaceholder.typicode.com/posts
 * @author Victor
 *
 */
@Entity
@Table(name="flower_comment")
public class FlowerComment {
	@Id	
	private Integer id;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column
	private String title;
	@Column
	private String body;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	/**
	 * Just a way to print out what the object looks like.
	 * The return value should never be used or parsed as actual processing of any app. 
	 */
	@Override
	public String toString() {		
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// I still want to see how it looks
			return "FlowerComment [id=" + id + ", userId=" + userId + ", title=" + title + ", body=" + body + "]";
		} 
		
	}
	
	
	
}
