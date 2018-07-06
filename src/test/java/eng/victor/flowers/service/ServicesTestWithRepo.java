package eng.victor.flowers.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;


@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({ "classpath:test-drop.sql", "classpath:test-schema.sql", "classpath:test-data.sql" })
public class ServicesTestWithRepo {
	private final int KNOWN_FLOWER_COMMENT_ID =1800;

	@Autowired
	private FlowerCommentService flowerCommentService;
	
	@Autowired
	private UserService userService;
	
	private Map<Integer, FlowerComment> sampleFlowerComments;
	
	
	@Before
	public void setup() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("user_flowercomments_feed.json");		
		FlowerComment[] fcs = mapper.readValue(in, FlowerComment[].class);
		
		sampleFlowerComments = new HashMap<>();
		Arrays.asList(fcs).forEach(fc -> sampleFlowerComments.put(fc.getId(), fc));
	}
	
	
	@Test
	public void testSaveFlowerComments_addsData() {
		Long userCount = userService.getUserCount();
		flowerCommentService.saveFlowerComments(sampleFlowerComments.values());
		Long count = flowerCommentService.getFlowerCommentCount();
		
		assertTrue("adding flowerComment should not reduce count", count.longValue() >= sampleFlowerComments.size());		
		assertTrue(userService.getUserCount().longValue() >=userCount.longValue());
				
		FlowerComment result = flowerCommentService.getFlowerComment(KNOWN_FLOWER_COMMENT_ID);
		assertNotNull("known id should not have been erased", result);
	}
	
	
	
	@Test	
	public void testGetFlowerComment() {		
		FlowerComment result = flowerCommentService.getFlowerComment(KNOWN_FLOWER_COMMENT_ID);
		assertNotNull(result);
	}
	
	
	@Test
	public void testUpdateFlowerComment() {
		FlowerComment startWith = flowerCommentService.getFlowerComment(KNOWN_FLOWER_COMMENT_ID);
		String newTitle = startWith.getTitle()+" Change";
		
		List<PatchOperation> ops = Collections.singletonList(PatchOperation.AddOp("title", newTitle));
		FlowerComment result = flowerCommentService.updateFlowerComment(KNOWN_FLOWER_COMMENT_ID, ops);
		assertNotNull(result);
		assertTrue(result.getTitle().equals(newTitle));
		assertTrue(! result.getTitle().equals(startWith.getTitle()));
	}
}
