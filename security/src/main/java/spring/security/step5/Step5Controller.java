package spring.security.step5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/step5/")
@RestController
public class Step5Controller {


    @GetMapping("/invalidSessionUrl")
    public String invalidSessionUrl(){
        return "invalidSessionUrl";
    }

    @GetMapping("/expired")
    public String expired(){
        return "expired";
    }
}