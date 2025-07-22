package com.english.lms.service;

import com.english.lms.entity.SystemSettingEntity;
import com.english.lms.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final SystemSettingRepository repository;

    public Map<String, String> getAllSettings() {
        List<SystemSettingEntity> settings = repository.findAll();
        return settings.stream()
                .collect(Collectors.toMap(SystemSettingEntity::getKey, SystemSettingEntity::getValue));
    }

    // 설정값 일괄 업데이트/등록
    public void updateSettings(Map<String, String> settings) {
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            Optional<SystemSettingEntity> optional = repository.findByKey(entry.getKey());
            SystemSettingEntity entity = optional.orElseGet(() -> {
                SystemSettingEntity newEntity = new SystemSettingEntity();
                newEntity.setKey(entry.getKey());
                return newEntity;
            });
            entity.setValue(entry.getValue());
            repository.save(entity);
        }
    }

    // 개별 조회가 필요하면 아래처럼 추가 가능
    public Optional<String> getSettingValue(String key) {
        return repository.findByKey(key).map(SystemSettingEntity::getValue);
    }
}
