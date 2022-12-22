package com.example.demo.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "registered_user")
public class User {

    private @Id @GeneratedValue Long id;

    @NotBlank
    @Size(min = 0, max = 20)
    private String fullName;

    @NotNull
    private Position position;

    public User(String username, Position position) {
        this.fullName = username;
        this.position = position;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && fullName.equals(user.fullName) && position == user.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, position);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + fullName + '\'' +
                ", position=" + position +
                '}';
    }
}
