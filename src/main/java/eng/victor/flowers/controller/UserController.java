package eng.victor.flowers.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eng.victor.flowers.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/count")
	public ResponseEntity<Long> count(){
		return ResponseEntity.ok(userService.getUserCount());		
	}
}
