package eng.victor.flowers.dao;

import org.springframework.data.repository.CrudRepository;


import eng.victor.flowers.api.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
}
