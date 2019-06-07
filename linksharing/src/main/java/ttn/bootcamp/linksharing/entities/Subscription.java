package ttn.bootcamp.linksharing.entities;

import ttn.bootcamp.linksharing.enums.Seriousness;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subscriptionId;

    @ManyToOne
    private Topic topic;

    @ManyToOne
    private User user;

    @Temporal(value = TemporalType.DATE)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    Seriousness seriousness;

    public Subscription() {
    }

    public Subscription(Topic topic, User user, Date createdAt, Seriousness seriousness) {
        this.topic = topic;
        this.user = user;
        this.createdAt = createdAt;
        this.seriousness = seriousness;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public Seriousness getSeriousness() {
        return seriousness;
    }

    public void setSeriousness(Seriousness seriousness) {
        this.seriousness = seriousness;
    }
}