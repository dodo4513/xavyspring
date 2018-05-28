package study.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
