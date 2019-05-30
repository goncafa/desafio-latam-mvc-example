package cl.desafiolatam.mvc.example.controller;

import cl.desafiolatam.mvc.example.business.UserService;
import cl.desafiolatam.mvc.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    @RequestMapping("/")
    public ModelAndView index() {
        // se rescatan todos los usuarios de la base de datos
        final List<User> users = getUserService().listAllUsers();
        System.out.println(String.format("Usuarios en base de datos : %s", users));
        return new ModelAndView("index", "users", users);
    }

    @PostMapping("/add_user")
    public ModelAndView addUser(@RequestParam("first_name") String firstName,
                                @RequestParam("last_name") String lastName,
                                @RequestParam("email") String email) {
        System.out.println(String.format("first_name : %s | last_name : %s | email : %s", firstName, lastName, email));

        // se crea el objecto de modelo para almacenar el usuario
        User user = new User(firstName, lastName, email);
        getUserService().saveUser(user);

        return index();
    }
}
