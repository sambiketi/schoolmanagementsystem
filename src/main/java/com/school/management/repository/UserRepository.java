package com.school.management.repository;

import com.school.management.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByDisplayId(String displayId);
    
    List<User> findByUserType(String userType);
    
    List<User> findByDepartmentId(Long departmentId);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.userType = :userType")
    List<User> findActiveUsersByType(@Param("userType") String userType);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.displayId = :displayId")
    boolean existsByDisplayId(@Param("displayId") String displayId);
}