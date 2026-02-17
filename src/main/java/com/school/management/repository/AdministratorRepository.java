package com.school.management.repository;

import com.school.management.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    
    Optional<Administrator> findByUserId(Long userId);
    
    boolean existsByUserId(Long userId);
}