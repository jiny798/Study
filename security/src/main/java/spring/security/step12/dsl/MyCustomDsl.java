package spring.security.step12.dsl;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

    private boolean flag;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MyCustomFilter myCustomFilter = new MyCustomFilter();
        myCustomFilter.setFlag(flag);
        http.addFilterBefore(myCustomFilter , UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }

    public boolean setFlag(boolean value) {
        return flag = value;
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }

}
