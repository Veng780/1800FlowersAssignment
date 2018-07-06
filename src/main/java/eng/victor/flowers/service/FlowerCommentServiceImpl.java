package eng.victor.flowers.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;
import eng.victor.flowers.api.User;
import eng.victor.flowers.dao.FlowerCommentRepository;
import eng.victor.flowers.dao.UserRepository;

@Service
@Transactional
public class FlowerCommentServiceImpl implements FlowerCommentService {
	private static Logger log = LoggerFactory.getLogger(FlowerCommentServiceImpl.class);
	
	
	@Autowired
	private FlowerCommentRepository repository;

	@Autowired
	private UserRepository userRepository;

	
	@Override
	public void saveFlowerComments(Collection<FlowerComment> flowerComments) {
		flowerComments.forEach(fs ->{
			repository.save(fs);		
			
			User user = new User();
			user.setId(fs.getUserId());
			userRepository.save(user);
		});
	}
	
	
	
	
	@Override
	public Long getFlowerCommentCount() {		
		return repository.count();
	}




	@Override
	public FlowerComment updateFlowerComment(Integer id, List<PatchOperation> patches) {
		Optional<FlowerComment> opt = repository.findById(id);
		if (! opt.isPresent()) {
			log.debug("no flowerComment for {}", id);
			return null;
		}
		
		FlowerComment flowerComment = opt.get();
		for (PatchOperation op: patches) {
			switch (op.getPath()) {
			case "title":
				flowerComment.setTitle(op.getValue());
				break;
			case "body":
				flowerComment.setBody(op.getValue());
				break;
			}
		}
		
		repository.save(flowerComment);
		return repository.findById(id).get();
	}
	
	
	@Override
	public FlowerComment getFlowerComment(Integer flowerStoryId){		
		Optional<FlowerComment> opt = repository.findById(flowerStoryId);
		return opt.orElse(null);
	}
	
	
	
	
	
}
