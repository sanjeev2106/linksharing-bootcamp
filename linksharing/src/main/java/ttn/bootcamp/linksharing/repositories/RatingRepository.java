package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttn.bootcamp.linksharing.entities.Rating;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.User;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating,Integer> {

    Rating findByUserAndResource(User user, Resource resource);

    void deleteRatingByResource(Resource resource);

    List<Rating> findAllByResource(Resource resource);

    List<Rating> findAllByOrderByPointsDesc();

    void deleteAllByResource(Resource resource);

}
