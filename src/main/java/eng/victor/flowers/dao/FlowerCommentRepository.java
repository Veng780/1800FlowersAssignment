package eng.victor.flowers.dao;


import org.springframework.data.repository.CrudRepository;

import eng.victor.flowers.api.FlowerComment;


public interface FlowerCommentRepository extends CrudRepository<FlowerComment, Integer> {
	
	
}
