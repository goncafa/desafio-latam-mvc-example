package cl.desafiolatam.mvc.example.util;

import cl.desafiolatam.mvc.example.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private static JsonUtil instance;

    private Gson gson;

    private Gson getGson() {
        return this.gson;
    }

    public User toUser(String jsonUser) {
        return getGson().fromJson(jsonUser, User.class);
    }

    public String fromUser(User user) {
        return getGson().toJson(user);
    }

    private JsonUtil() {
        super();

        // usamos la libreria de Google Gson para serializar y deserializar JSON.
        // se configura para esperar JSON con propiedades minuscula y snake case.
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static JsonUtil getInstance() {
        synchronized (JsonUtil.class) {
            if (null == instance)
                instance = new JsonUtil();
        }

        return instance;
    }
}
