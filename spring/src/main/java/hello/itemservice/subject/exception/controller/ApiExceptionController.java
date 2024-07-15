package hello.itemservice.subject.exception.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.subject.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiExceptionController {
	@InitBinder
	@GetMapping("/api/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자 500");
		}
		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값 400");
		}
		if (id.equals("user-ex")) {
			throw new UserException("사용자 오류 400");
		}
		return new MemberDto(id, "hello " + id);
	}
	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
