package eng.victor.flowers.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eng.victor.flowers.dao.UserRepository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	 @InjectMocks
	 private UserServiceImpl userService;
	 
	 @Mock
	 private UserRepository repository;
	 
	 
	 @Test
	 public void testCount() {
		 Long mockCount = new Long(5);
		 when(repository.count()).thenReturn(mockCount);
		 
		 Long count = userService.getUserCount();
		 assertTrue(mockCount.equals(count));
	 }
}
