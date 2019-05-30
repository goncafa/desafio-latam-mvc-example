package cl.desafiolatam.mvc.example.database.repository;

import cl.desafiolatam.mvc.example.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
