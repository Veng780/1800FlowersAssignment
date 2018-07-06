package eng.victor.flowers.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eng.victor.flowers.api.FlowerComment;
import eng.victor.flowers.api.PatchOperation;
import eng.victor.flowers.controller.exception.ResourceNotFoundException;
import eng.victor.flowers.service.FlowerCommentService;

@RestController
@RequestMapping("/flowerComments")
public class FlowerCommentController {
	private static Logger log = LoggerFactory.getLogger(FlowerCommentController.class);
	
	@Autowired
	private FlowerCommentService flowerCommentService;
	
		
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlowerComment> getById(@PathVariable("id") Integer id){
		FlowerComment fc = flowerCommentService.getFlowerComment(id);
		if (fc==null) {
			throw new ResourceNotFoundException("could not find that particular flower-comment");
		}
		return new ResponseEntity<FlowerComment>(fc, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> saveFlowerComments(@RequestBody List<FlowerComment> flowerComments){
		flowerCommentService.saveFlowerComments(flowerComments);
		log.debug("saved stories");
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	@RequestMapping(value="/{id}", method=RequestMethod.PATCH, 
			consumes="application/json-patch+json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlowerComment> patch(@PathVariable("id") Integer id, @RequestBody List<PatchOperation> patches){
		FlowerComment fc = flowerCommentService.updateFlowerComment(id, patches);
		if (fc==null) {
			throw new ResourceNotFoundException("could not find that particular flower-comment");
		}
		return new ResponseEntity<FlowerComment>(fc, HttpStatus.OK);
	}
	
	
	
}
