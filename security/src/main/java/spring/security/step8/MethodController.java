package spring.security.step8;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MethodController {

	private final DataService dataService;
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String admin() {
		return "admin";
	}

	@GetMapping("/user")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public String user() {
		return "user";
	}

	@GetMapping("/isAuthenticated")
	@PreAuthorize("isAuthenticated")
	public String isAuthenticated() {
		return "isAuthenticated";
	}

	@GetMapping("/user/{id}")
	@PreAuthorize("#id == authentication.name")
	public String authentication(@PathVariable(name = "id") String id) {
		return "id";
	}


	@GetMapping("/owner")
	@PostAuthorize("returnObject.owner == authentication.name")
	public Account owner(String name) {
		return new Account(name, false);
	}

	@GetMapping("/isSecure")
	@PostAuthorize("hasAuthority('ROLE_ADMIN') and returnObject.isSecure")
	public Account isSecure(String name, String secure) {
		return new Account(name, "Y".equals(secure));
	}

	// ----------------------------------------------

	@PostMapping("/writeList")
	public List<Account> writeList(@RequestBody List<Account> data){
		return dataService.writeList(data);
	}

	@PostMapping("/writeMap")
	public Map<String,Account> writeMap(@RequestBody List<Account> data){
		Map<String,Account> accountMap = data.stream().collect(Collectors.toMap(account -> account.getOwner(), account -> account));
		return dataService.writeMap(accountMap);
	}

}
