package eng.victor.flowers.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eng.victor.flowers.service.UserService;


public class UserControllerTest {
private static Logger log = LoggerFactory.getLogger(UserControllerTest.class);
	
	MockMvc mvc;
	
	@InjectMocks
	private UserController controller;
	
	@Mock
	private UserService userService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);		
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	
	@Test
	public void testGetCount() throws Exception{
		Long answer = 5l;
		when(userService.getUserCount()).thenReturn(answer);
			
		mvc.perform(get("/flowerComments/999").accept(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())			
			;
	}
	

}
