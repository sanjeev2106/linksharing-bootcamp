package ttn.bootcamp.linksharing.entities;

import javax.persistence.*;
import ttn.bootcamp.linksharing.enums.Visibility;
import java.util.Date;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer topicId;

    private String topicName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(value = TemporalType.DATE)
    private Date createdAt;

    @Temporal(value = TemporalType.DATE)
    private Date updatedAt;

    @OneToMany(mappedBy = "topic")
    List<Resource> resources;

    //private String description;

    @ElementCollection
    private List<Integer> subscribers;

    @Enumerated(EnumType.STRING)
    private Visibility visibilty;

    public Topic() {
    }

    public Topic(String topicName, User user, Date createdAt, Date updatedAt, List<Resource> resources,
                 String description, List<Integer> subscribers, Visibility visibilty) {
        this.topicName = topicName;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resources = resources;
       // this.description = description;
        this.subscribers = subscribers;
        this.visibilty = visibilty;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public List<Integer> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Integer> subscribers) {
        this.subscribers = subscribers;
    }

    public Visibility getVisibilty() {
        return visibilty;
    }

    public void setVisibilty(Visibility visibilty) {
        this.visibilty = visibilty;
    }
}
