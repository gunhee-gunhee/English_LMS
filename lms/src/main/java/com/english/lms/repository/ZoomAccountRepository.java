package com.english.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.ZoomAccountEntity;

public interface ZoomAccountRepository extends JpaRepository<ZoomAccountEntity, Integer> {
    @Query(value = "SELECT * FROM lms_zoom_account WHERE linked = 0", nativeQuery = true)
    List<ZoomAccountEntity> findByLinkedFalse();

    Optional<ZoomAccountEntity> findByZoomId(String zoomId);

    // === 추가: zoomNum으로 조회하는 기능 ===
    Optional<ZoomAccountEntity> findByZoomNum(Integer zoomNum);

    @Query(value = "SELECT zoom_id FROM lms_zoom_account WHERE zoom_num = :zoomNum", nativeQuery = true)
	String findZoomIdByZoomNum(@Param("zoomNum") Integer zoomNum);

    //lms_zoom_accountの全てのzoomIdを取得
    @Query(value = "SELECT zoom_id FROM lms_zoom_account WHERE status = '正常'", nativeQuery = true)
	List<String> findAllZoomIds();
}
