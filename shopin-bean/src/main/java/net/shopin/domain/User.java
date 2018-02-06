package net.shopin.domain;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class User implements Serializable {

    private static final long serialVersionUID = -1543463794736830617L;

    private Long id;

    private String username;

    private String password;

    private Integer age;

    private String gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;

        if (!id.equals(user.id)) {
            return false;
        }
        if (!username.equals(user.username)) {
            return false;
        }
        if (!password.equals(user.password)) {
            return false;
        }
        if (!age.equals(user.age)) {
            return false;
        }
        return gender.equals(user.gender);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + gender.hashCode();
        return result;
    }
}