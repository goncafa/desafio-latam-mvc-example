package cl.desafiolatam.mvc.example.database.entity;

import javax.persistence.*;

@Entity
@Table (name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jsonUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonUser() {
        return jsonUser;
    }

    public void setJsonUser(String jsonUser) {
        this.jsonUser = jsonUser;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", jsonUser='" + jsonUser + '\'' +
                '}';
    }
}
