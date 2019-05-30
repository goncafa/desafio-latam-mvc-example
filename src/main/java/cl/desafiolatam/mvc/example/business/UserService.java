package cl.desafiolatam.mvc.example.business;

import cl.desafiolatam.mvc.example.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    List<User> listAllUsers();
}
