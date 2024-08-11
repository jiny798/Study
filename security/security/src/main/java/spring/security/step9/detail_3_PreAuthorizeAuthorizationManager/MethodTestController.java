package spring.security.step9.detail_3_PreAuthorizeAuthorizationManager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.security.step8.Account;

@RestController
@RequiredArgsConstructor
@RequestMapping("/method")
public class MethodTestController {

	private final MethodDataService dataService;

	@GetMapping("/user")
	public String user(){
		return dataService.getUser();
	}

	@GetMapping("/owner")
	public Account owner(String name){
		return dataService.getAdmin(name);
	}

	@GetMapping("/display")
	public String display(){
		return dataService.display();
	}


}
