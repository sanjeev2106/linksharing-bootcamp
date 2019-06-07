package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.enums.ResourceType;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource,Integer> {

    List<Resource> findAllByTopicIn(List<Topic> topicList);

    Page<Resource> findAllByTopicIn(Pageable pageable, List<Topic> topicList);

    List<Resource> findAllByTopic(Topic topic);

    List<Resource> findAllByUser(User user);

    void deleteAllByTopic(Topic topic);

    List<Resource> findAllByDescriptionLike(String desc);

    List<Resource> findAllByDescriptionLikeAndTopic(String desc,Topic topic);

    List<Resource> findAllByResourceTypeAndTopic(ResourceType resourceType,Topic topic);

    List<Resource> findAllByResourceIdIn(List<Integer> ids);

    void deleteByResourceId(Integer id);

    Resource findByResourceId(Integer id);
}