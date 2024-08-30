package security.demo.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import security.demo.domain.entity.Account;

public interface UserManagementRepository extends JpaRepository<Account, Long> { }
