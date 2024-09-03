package spring.security.step1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/step1")
public class Step1Controller {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
