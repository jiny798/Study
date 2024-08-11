package hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import hello.itemservice.subject.security.step8.Account;

@Service
public class MethodDataService {

	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String getUser(){
		return "user";
	}

	@PostAuthorize("returnObject.owner == authentication.name")
	public Account getAdmin(String name){
		return new Account(name , false);
	}

	public String display(){
		return "display";
	}

}
