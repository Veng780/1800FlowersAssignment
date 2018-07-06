package eng.victor.flowers.controller;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.h2.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock; 
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;
import eng.victor.flowers.service.FlowerCommentService;


public class FlowerCommentControllerTest {
	private static Logger log = LoggerFactory.getLogger(FlowerCommentControllerTest.class);
	
	MockMvc mvc;
	
	@InjectMocks
	private FlowerCommentController controller;
	
	@Mock
	private FlowerCommentService flowerCommentService;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);		
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	
	@Test
	public void testSaveAllFlowerStories() throws Exception{
		doNothing().when(flowerCommentService).saveFlowerComments(any());		
				
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("user_flowercomments_feed.json");
		ByteArrayOutputStream baos =new ByteArrayOutputStream();
		IOUtils.copyAndCloseInput(in, baos);
		String testUsersJson =  baos.toString();
		log.debug(testUsersJson);
		
		mvc.perform(post("/flowerComments").contentType(MediaType.APPLICATION_JSON).content(testUsersJson))
			.andExpect(status().is2xxSuccessful());
	}
	
	
	@Test
	public void testGetFlowerStory() throws Exception{				
		when(flowerCommentService.getFlowerComment(anyInt())).thenAnswer(new Answer<FlowerComment>() {
			@Override
			public FlowerComment answer(InvocationOnMock iom) throws Throwable {
				Integer id = (Integer) iom.getArguments()[0];
				
				FlowerComment result = new FlowerComment();
				result.setId(id);
				result.setUserId(0);
				result.setTitle("some title");
				result.setBody("some body");				
				return result;
			}			
		});		
			
		mvc.perform(get("/flowerComments/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.title").value("some title"))
			;
	}
	
	
	@Test
	public void testGetNonFlowerStory() throws Exception{				
		when(flowerCommentService.getFlowerComment(anyInt())).thenAnswer(new Answer<FlowerComment>() {
			@Override
			public FlowerComment answer(InvocationOnMock iom) throws Throwable {				
				log.info("make non story by null return");
				return null;
			}			
		});		
			
		mvc.perform(get("/flowerComments/999").accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())			
			;
	}
	
	
	@Test
	public void testPatchFlowerStory() throws Exception{
		final List<PatchOperation> ops 
			= Arrays.asList(PatchOperation.AddOp("title", "diff title"), PatchOperation.AddOp("body", "diff body"));
		
		when(flowerCommentService.updateFlowerComment(anyInt(), any())).thenAnswer(new Answer<FlowerComment>() {
			@Override
			public FlowerComment answer(InvocationOnMock iom) throws Throwable {
				Integer id = (Integer) iom.getArguments()[0];				
				
				FlowerComment result = new FlowerComment();
				result.setId(id);
				result.setTitle(ops.get(0).getValue());
				result.setBody(ops.get(1).getValue());				
				return result;
			}			
		});
		
		ObjectMapper om = new ObjectMapper();
		String opsString = om.writeValueAsString(ops);
		log.info("patch {}", opsString);
			
		mvc.perform(patch("/flowerComments/88").contentType(new MediaType("application", "json-patch+json")).content(opsString))			
			.andExpect(status().isOk())			
			;
	}
	
	
}
