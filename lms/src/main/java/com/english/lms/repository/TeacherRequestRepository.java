package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.english.lms.entity.TeacherRequestEntity;

public interface TeacherRequestRepository extends JpaRepository<TeacherRequestEntity, Integer>, TeacherRequestRepositoryCustom {
}
