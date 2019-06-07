package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.enums.Visibility;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<Topic,Integer> {

    Topic findByTopicNameAndUser(String topicname, User user);

    Integer countAllByUser(User user);

    List<Topic> findAllByUser(User user);

    List<Topic> findBySubscribers(Integer id);

    List<Topic> findAllByTopicNameLike(String topicName);

    List<Topic> findAllByVisibiltyAndTopicNameLike(Visibility visibility,String topicName);

    List<Topic> findAllByVisibilty(Visibility visibility);

    List<Topic> findAllByVisibiltyAndUser(Visibility visibility,User user);

    Page<Topic> findAll(Pageable pageable);

    Page<Topic> findAllByVisibilty(Visibility visibility,Pageable pageable);

    Page<Topic> findAllByVisibiltyAndUser(Visibility visibility,User user,Pageable pageable);

    Topic findByTopicIdAndUser(Integer id, User user);
}
