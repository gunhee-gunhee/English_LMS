package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.english.lms.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByAdminId(String adminId);
    Optional<AdminEntity> findByAdminNum(Integer adminNum);
    void deleteByAdminId(String adminId);
}
