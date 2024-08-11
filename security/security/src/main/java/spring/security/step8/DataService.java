package spring.security.step8;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

@Service
public class DataService {

	@PreFilter("filterObject.owner == authentication.name") // 로그인한 유저 이름만 필터링
	public List<Account> writeList(List<Account> data){
		return data;
	}


	@PreFilter("filterObject.value.owner == authentication.name")  // 로그인한 유저 이름만 필터링
	public Map<String,Account> writeMap(Map<String,Account> data){
		return data;
	}
}
