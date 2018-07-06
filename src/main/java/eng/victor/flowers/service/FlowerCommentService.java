package eng.victor.flowers.service;

import java.util.Collection;
import java.util.List;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;

public interface FlowerCommentService {
	
	FlowerComment getFlowerComment(Integer flowerStoryId);
	
	void saveFlowerComments(Collection<FlowerComment> flowerComments);
	
	Long getFlowerCommentCount();
	
	FlowerComment updateFlowerComment(Integer id, List<PatchOperation> patches) ;
}
