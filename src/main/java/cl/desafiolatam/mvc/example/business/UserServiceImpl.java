package cl.desafiolatam.mvc.example.business;

import cl.desafiolatam.mvc.example.database.entity.UserEntity;
import cl.desafiolatam.mvc.example.database.repository.UserRepository;
import cl.desafiolatam.mvc.example.model.User;
import cl.desafiolatam.mvc.example.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private JsonUtil jsonUtil;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JsonUtil getJsonUtil() {
        return jsonUtil;
    }

    @Autowired
    public void setJsonUtil(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public void saveUser(User user) {
        // serializamos User a JSON
        final String jsonUser = getJsonUtil().fromUser(user);
        System.out.println(String.format("jsonUser : %s", jsonUser));


        // se almacena en la base de datos
        UserEntity userEntity = new UserEntity();
        userEntity.setJsonUser(jsonUser);
        userEntity = getUserRepository().save(userEntity);
        System.out.println(String.format("Usuario almacenado con ID : %s", userEntity.getId()));
    }

    public List<User> listAllUsers() {
        // lista que almacenara los usuarios para retornar a la vista
        List<User> users = new ArrayList<>();

        // se rescatan todos los usuarios de la base de datos
        getUserRepository().findAll().forEach(userEntity -> {
            // por cada usuario deserializamos el json a un objeto
            final User user = getJsonUtil().toUser(userEntity.getJsonUser());
            user.setId(userEntity.getId());
            users.add(user);
        });

        return users;
    }
}
