package hello.itemservice.subject.security.step2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test/test")
	@ResponseBody
	public String index(){

		return "test";
	}
}
