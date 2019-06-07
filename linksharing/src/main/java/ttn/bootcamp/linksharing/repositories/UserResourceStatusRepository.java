package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.entities.UserResourceStatus;

import java.util.List;

public interface UserResourceStatusRepository extends CrudRepository<UserResourceStatus, Integer> {

    void deleteByResourceIn(List<Resource> resources);

    void deleteByUserAndResource(User user, Resource resource);

    void deleteAllByResource(Resource resource);

    List<UserResourceStatus> findAllByUser(User user, Pageable pageable);
}
