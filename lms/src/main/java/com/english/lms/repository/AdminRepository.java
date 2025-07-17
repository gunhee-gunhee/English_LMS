package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.english.lms.entity.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
	
	@Query(value = "select * from lms_admin where id = :adminId", nativeQuery = true)
	Optional<AdminEntity> findByAdminId(@Param("adminId") String adminId);
    Optional<AdminEntity> findByAdminNum(Integer adminNum);
    void deleteByAdminId(String adminId);
}
