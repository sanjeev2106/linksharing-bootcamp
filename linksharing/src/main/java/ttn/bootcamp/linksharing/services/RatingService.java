package ttn.bootcamp.linksharing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttn.bootcamp.linksharing.entities.Rating;
import ttn.bootcamp.linksharing.entities.Resource;
import ttn.bootcamp.linksharing.entities.Topic;
import ttn.bootcamp.linksharing.entities.User;
import ttn.bootcamp.linksharing.repositories.RatingRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ResourceService resourceService;

    public void saveRating(User user, Integer userRating, Integer resourceId) {

        Resource resource = resourceService.getResourceById(resourceId);
        if (resource != null) {
            Rating rating = ratingRepository.findByUserAndResource(user, resource);
            if (rating == null) {
                System.out.println("insde rating if");
                rating = new Rating();
                rating.setPoints(userRating);
                rating.setResource(resourceService.getResourceById(resourceId));
                rating.setUser(user);
                ratingRepository.save(rating);
            } else {
                System.out.println("insde rating else");
                rating.setPoints(userRating);
                ratingRepository.save(rating);
            }
        }
    }


    @Transactional
    public void deleteRating(Topic topic) {
        List<Resource> resourceList = resourceService.getResourcesByTopic(topic);
        if (!resourceList.isEmpty()) {
            Iterator<Resource> resourceIterator = resourceList.iterator();
            while (resourceIterator.hasNext()) {
                ratingRepository.deleteRatingByResource(resourceIterator.next());
            }
        }
    }

    public List<Rating> getAllRating(){
        Iterable<Rating> ratings = ratingRepository.findAll();
        List<Rating> ratingList = new ArrayList<>();
        ratings.forEach(ratingList::add);
        return ratingList;
    }

    @Transactional
    public void  deleteRatingByResource(Resource resource){
        ratingRepository.deleteAllByResource(resource);
    }

    public List<Rating> getRatingByResource(Resource resource){
        return ratingRepository.findAllByResource(resource);
    }

    public Rating getRatingByUserAndResource(User user, Resource resource){
        return ratingRepository.findByUserAndResource(user, resource);
    }
}
