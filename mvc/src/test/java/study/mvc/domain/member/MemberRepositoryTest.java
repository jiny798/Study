package study.mvc.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest {
	MemberRepository memberRepository = MemberRepository.getInstance();

	@AfterEach
	void afterEach() {
		memberRepository.clearStore();
	}

	@Test
	void save() {
		//given
		Member member = new Member("hello", 10);

		//when
		Member saveMember = memberRepository.save(member);

		//then
		Member findMember = memberRepository.findById(saveMember.getId());
		assertThat(findMember).isEqualTo(saveMember);
	}
}