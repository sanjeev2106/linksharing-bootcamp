package ttn.bootcamp.linksharing.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ttn.bootcamp.linksharing.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username,String password);

    Optional<User> findByUserId(Integer userId);

    Optional<User> findUserByEmail(String email);

    List<User> findAll();



    @Modifying
    @Query("UPDATE User u set u.password = :password where u.email = :email")
    int updatePassword(@Param("password") String password, @Param("email") String email);

   //for admin page

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByIsActive(Pageable pageable, Boolean active);
    // List<User> findAll();

   /* List<User> findAllByIsActive(Boolean active);

    List<User> findAllByOrderByUserIdAsc();

    List<User> findAllByOrderByUserIdDesc();*/

//    @Modifying
//    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.userId = :userId")
//    int updateUserActive(@Param("isActive") Boolean isActive, @Param("userId") Integer userId);


}
