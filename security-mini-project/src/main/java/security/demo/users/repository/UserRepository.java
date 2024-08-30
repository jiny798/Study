

package security.demo.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.demo.domain.entity.Account;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}