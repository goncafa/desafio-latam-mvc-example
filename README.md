# EJEMPLO SERIALIZACION/DESEARIALIZACION JSON
A continuación haremos una pequeña guía sobre como serializar y deserializar objetos a JSON para lo cual crearemos un proyecto de ejemplo usando `Spring Boot`, `Spring MVC` y `JPA` usando como persistencia una base de datos `H2` en archivo. Debido a que en esta guía nos vamos a centrar específicamente en la serialización JSON no entraremos en mayores detalles sobre el resto de las tecnologías aquí utilizadas, quedando a tarea de cada lector el poder profundizar en dichos temas si le son de interés.

## Crear nuestro proyecto
Lo primero será crear un directorio donde poder alojar nuestro proyecto. Puedes crearlo donde mas te guste, en nuestro caso hemos creado el directorio `desafio-latam-mvc-example`. 

El siguiente paso será entrar al directorio que hemos creado y crear un nuevo archiv xml llamado `pom.xml`, puedes usar tu editor de texto preferido, como por ejemplo: [Visual Studio Code](https://code.visualstudio.com/) el cual es gratuito y tiene pocas que envidiarle a cualquier editor comercial.

### pom.xml
Para efectos de esta guía vamos a asumir que ya sabemos como funciona `Maven` y que sabes como crear un `POM` por lo que
lo que restará es agregar las dependencias que usaremos:

Agregamos las dependencias de Spring necesarias para hacer funcionar el ejemplo:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Luego debemos agregar las dependencias para poder hacer la serialización con JSON, para esto puedes usar alguna de las librerías disponibles en forma libre como lo son las de `Apache`. En nuestro caso hemos preferido utilizar las librería [GSON](https://github.com/google/gson) de `Google` por lo que agregaremos la siguiente dependencia en nuestro pom:
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.6.2</version>
</dependency>
```

Finalmente como utilizaremos una base de datos H2 para nuestra persistencia debemos agregar dicha dependencia:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Con todo lo anterior nuestro POM se ve así ya finalizado:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>cl.desafiolatam.mvc.example</groupId>
    <artifactId>desafiolatam-mvc-example</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>desafiolatam-mvc-example</name>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.6.2</version>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### application.properties
Para que todo funcione correctamente debemos poner algunas configuraciones para Spring en nuestro archivo 
`src/main/resources/application.properties`
```properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.name=desafiolatam_example
spring.datasource.url=jdbc:h2:file:~/desafiolatam_example;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=sa
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Creando el modelo
Para este ejemplo vamos a crear un formulario el cual solicitara `Nombre`, `Apellido` y `E-mail` para ser guardado en
nuestra base de datos como un JSON y luego rescataremos todos los usuarios almacenados, los convertiremos en objecto y los
listaremos en una tabla HTML.

Lo primero será crear nuestro modelo:
```java
package cl.desafiolatam.mvc.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public User() {
        super();
    }

    public User(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
```

## Serializa/Deserializar
El siguiente es el componente estrella de nuestra guía, pues será quien se encargue de Serializar/Deserializar objectos a JSON y viceversa:
```java
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
```

Como se puede apreciar esta es una tarea bastante sencilla utilizando la librería de Google. Tan solo debemos tener una instancia de `Gson` la cual hemos construido con un builder especial incluido en la librería para poder transformar correctamente JSON con propiedades en minuscula con estilo snake case (separados por guion bajo). Esto lo hemos logrado así:
```java
this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
```

Con esto podemos hacer funcionar nuestros siguientes metodos para convertir un objeto de modelo `User` a JSON y de JSON 
a `User`:
```java
    public User toUser(String jsonUser) {
        return getGson().fromJson(jsonUser, User.class);
    }

    public String fromUser(User user) {
        return getGson().toJson(user);
    }
```

**ATENTO** como le hemos dicho a `GSON` que el JSON entrante lo queremos serializar en un objecto de tipo `User`:
```java
fromJson(jsonUser, User.class);
```

Para convertir de Objecto a JSON no es necesario indicar el tipo :)

## Creando el servicio de Spring
Quien se encargara de invocar a nuestro serializador será el servicio de negocio que almacenara y obtendrá los usuarios de la base de datos:
```java
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
```

## Creando el controlador
Para poder conectar la vista con el servicio usaremos un controlador de Spring el cual se comunicara con su servicio
usando el modelo previamente creado.

```java
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
```

## La vista
Finalmente para conectar todo crearemos la vista la cual contiene un formulario HTML con el que podremos ingresar nuevos usuarios a la base de datos:
```html
<div align="center">
    <form method="post" action="/add_user">
        <table>
            <tr>
                <td>Nombre : </td><td><input type="text" name="first_name"/></td>
            </tr>
            <tr>
                <td>Apellido : </td><td><input type="text" name="last_name"/></td>
            </tr>
            <tr>
                <td>E-mail : </td><td><input type="text" name="email"/></td>
            </tr>
            <tr>
                <td colspan="2" align="left"><input type="submit" value="Guardar Usuario"/></td>
            </tr>
        </table>
    </form>
</div>
```

Para completar la experiencia listaremos los usuarios en la base de datos con ayuda de JSTL:
```html
<p align="center">
    <c:if test="${users.size() > 0}">
        <table>
            <thead>
                <tr>
                    <th style="border: solid black 1px">id</th>
                    <th style="border: solid black 1px">Nombre</th>
                    <th style="border: solid black 1px">Apellido</th>
                    <th style="border: solid black 1px">E-mail</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td style="border: solid black 1px"><c:out value="${user.id}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.firstName}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.lastName}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.email}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</p>
```

# COMO EJECUTAR EJEMPLO
Podrás encontrar el proyecto completo en este repositorio, si lo deseas ejecutar debes primero clonar el repositorio:
```bash
git clone https://github.com/goncafa/desafio-latam-mvc-example.git
```

Luego debes entrar al directorio `desafio-latam-mvc-example`
```bash
cd desafio-latam-mvc-example
```

Ejecutar el proyecto
```bash
mvn spring-boot:run
```

Si todo ha salido bien podrás acceder a la palicación en la siguiente URL: [http://localhost:8080](http://localhost:8080)