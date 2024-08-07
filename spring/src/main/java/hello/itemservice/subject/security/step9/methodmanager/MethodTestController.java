package hello.itemservice.subject.security.step9.methodmanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.subject.security.step8.Account;
import lombok.RequiredArgsConstructor;

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
