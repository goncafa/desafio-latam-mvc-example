package cl.desafiolatam.mvc.example.controller;

import cl.desafiolatam.mvc.example.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @PostMapping("/add_user")
    public ModelAndView addUser(@RequestParam("user") String jsonUser) {
        System.out.println(jsonUser);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        final User user = gson.fromJson(jsonUser, User.class);

        System.out.println(user);
        return new ModelAndView("index");
    }
}
