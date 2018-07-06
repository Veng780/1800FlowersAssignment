package eng.victor.flowers.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;
import eng.victor.flowers.api.User;
import eng.victor.flowers.dao.FlowerCommentRepository;
import eng.victor.flowers.dao.UserRepository;


@RunWith(SpringJUnit4ClassRunner.class)
public class FlowerCommentServiceImplTest {

	@InjectMocks
	private FlowerCommentServiceImpl flowerCommentService;
	
	@Mock
	private FlowerCommentRepository repository;
	
	@Mock
	private UserRepository userRepository;
	
	
	private Map<Integer, FlowerComment> sampleFlowerComments;
	
	
	@Before
	public void setup() throws Exception {
		//ReflectionTestUtils.setField(flowerCommentService, "repository", repository);
		//ReflectionTestUtils.setField(flowerCommentService, "userRepository", userRepository);
		
		ObjectMapper mapper = new ObjectMapper();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("user_flowercomments_feed.json");		
		FlowerComment[] fcs = mapper.readValue(in, FlowerComment[].class);
		
		sampleFlowerComments = new HashMap<>();
		Arrays.asList(fcs).forEach(fc -> sampleFlowerComments.put(fc.getId(), fc));
	}
	
	@Test
	public void testSaveFlowerComments() {
		
		when(repository.save(any(FlowerComment.class))).then(new Answer<FlowerComment>() {
			@Override
			public FlowerComment answer(InvocationOnMock invocation) throws Throwable {
				FlowerComment fc = invocation.getArgument(0);				
				return fc;
			}			
		});
		when(userRepository.save(any(User.class))).then(new Answer<User>() {
			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				User user = invocation.getArgument(0);				
				return user;
			}			
		});
		
		
		flowerCommentService.saveFlowerComments(sampleFlowerComments.values());		
	}
	
	
	
	@Test
	public void testGetFlowerComment() {
		when(repository.findById(anyInt())).then(new Answer<Optional<FlowerComment>>() {
			@Override
			public Optional<FlowerComment> answer(InvocationOnMock invocation) throws Throwable {
				Integer id = (Integer) invocation.getArguments()[0];
				return Optional.of(sampleFlowerComments.get(id));				
			}
		});
		
		FlowerComment result = flowerCommentService.getFlowerComment(2);
		assertNotNull(result);
	}
	
	
	@Test
	public void testUpdateFlowerComment() {
		when(repository.findById(anyInt())).then(new Answer<Optional<FlowerComment>>() {
			@Override
			public Optional<FlowerComment> answer(InvocationOnMock invocation) throws Throwable {
				Integer id = (Integer) invocation.getArguments()[0];
				return Optional.of(sampleFlowerComments.get(id));				
			}
		});
		when(repository.save(any(FlowerComment.class))).then(new Answer<FlowerComment>() {
			@Override
			public FlowerComment answer(InvocationOnMock invocation) throws Throwable {
				FlowerComment fc = invocation.getArgument(0);				
				return fc;
			}			
		});
		
		List<PatchOperation> ops = Collections.singletonList(PatchOperation.AddOp("title", "change title"));
		FlowerComment result = flowerCommentService.updateFlowerComment(2, ops);
		assertNotNull(result);
		assertTrue(result.getTitle().equals("change title"));
	}
}
