
package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.english.lms.entity.ClassApplyEntity;

public interface ClassApplyRepository extends JpaRepository<ClassApplyEntity, Integer> {
}
