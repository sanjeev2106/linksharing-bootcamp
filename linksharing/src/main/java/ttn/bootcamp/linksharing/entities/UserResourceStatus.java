package ttn.bootcamp.linksharing.entities;

import javax.persistence.*;

@Entity
public class UserResourceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer statusId;

    @ManyToOne
    private Resource resource;

    @ManyToOne
    private User user;

    //private String status;

    public UserResourceStatus() {
    }

    public UserResourceStatus(Resource resource, User user, String status) {
        this.resource = resource;
        this.user = user;

    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
