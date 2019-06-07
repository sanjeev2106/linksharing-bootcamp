package ttn.bootcamp.linksharing.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @NotEmpty(message = "firstname is empty")
    private String firstName;

    @NotEmpty(message = "lastname is empty")
    private String lastName;

    @NotEmpty(message = "email is empty")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "username is empty")
    @Column(unique = true)
    private String username;

    @Transient
    private String name = firstName + " " + lastName;

    private String password;

    @Transient
    private String confirmPassword;

    private String photo;

    @Temporal(value = TemporalType.DATE)
    private Date createdAt;

    @Temporal(value = TemporalType.DATE)
    private Date updatedAt;

    private boolean isActive;

    private boolean isAdmin;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    @JoinTable(joinColumns = @JoinColumn(name = "userId"),
//    inverseJoinColumns = @JoinColumn(name = "topicId"))
    Collection<Topic> topic = new HashSet<Topic>();

    @OneToMany(mappedBy = "user")
    Collection<Resource> resources = new HashSet<Resource>();

    public User() {
    }

    public User(String firstName, String lastName, String email, String username, String password,
                String photo, Date createdAt, Date updatedAt, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Collection<Topic> getTopic() {
        return topic;
    }

    public void setTopic(Collection<Topic> topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
    }
}