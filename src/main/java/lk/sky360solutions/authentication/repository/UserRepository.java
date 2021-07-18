package lk.sky360solutions.authentication.repository;

import lk.sky360solutions.authentication.model.persitent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User getByUsername(String username);
}
