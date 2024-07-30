package hello.itemservice.subject.security.step8;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Step8Controller {

	@GetMapping("/user/{name}")
	public String userName(@PathVariable(value = "name") String name) {
		return name;
	}

	@GetMapping("/custom")
	public String custom(){
		return "custom";
	}

}
