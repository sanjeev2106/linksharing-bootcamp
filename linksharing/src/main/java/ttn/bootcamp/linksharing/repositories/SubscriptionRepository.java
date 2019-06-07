package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttn.bootcamp.linksharing.entities.Subscription;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription,Integer> {

    Integer countAllByUser(User user);

    Subscription findByTopicAndUser(Topic topic,User user);

    ArrayList<Subscription> findAllByUser(User user);

    ArrayList<Subscription> findAllByTopic(Topic topic);

    void deleteAllByTopic(Topic topic);


    //Boolean findByTopicAndUser(Topic topic, User user);

}
