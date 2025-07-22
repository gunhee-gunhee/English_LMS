package com.english.lms.repository;

import com.english.lms.entity.SystemSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSettingEntity, Integer> {

    Optional<SystemSettingEntity> findByKey(String key);
}
