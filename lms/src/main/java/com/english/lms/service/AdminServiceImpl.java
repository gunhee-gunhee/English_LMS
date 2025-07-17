package com.english.lms.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.english.lms.dto.AdminDTO;
import com.english.lms.entity.AdminEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerCenter(AdminDTO dto) {
        // Center会員登録機能

        AdminEntity center = new AdminEntity();

        center.setAdminId(dto.getId());
        center.setPassword(passwordEncoder.encode(dto.getPassword()));
        center.setRole(Role.CENTER);

        adminRepository.save(center);
    }

    @Override
    public boolean existsById(String id) {
        // AdminRepositoryでcenterIdとadminIdの重複チェック
        return adminRepository.findByAdminId(id).isPresent();
    }

    @Override
    public void registerAdmin(AdminDTO dto) {
        // Admin会員登録機能

        AdminEntity admin = new AdminEntity();

        admin.setAdminId(dto.getId());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setRole(Role.ADMIN);

        adminRepository.save(admin);
    }

    // ====== adminNum (Integer) CRUD ======

    @Override
    public AdminDTO getAdmin(Integer adminNum) {
        // 管理者情報をadminNumで取得してDTOで返却
        AdminEntity entity = adminRepository.findByAdminNum(adminNum)
                .orElseThrow(() -> new RuntimeException("管理者情報が存在しません"));

        return AdminDTO.builder()
                .adminNum(entity.getAdminNum())
                .id(entity.getAdminId())
                .role(entity.getRole().name().toLowerCase()) // ex: "admin"
                .build();
    }

    @Override
    public void updateAdmin(Integer adminNum, AdminDTO dto) {
        // adminNumでエンティティ取得
        AdminEntity entity = adminRepository.findByAdminNum(adminNum)
                .orElseThrow(() -> new RuntimeException("管理者情報が存在しません"));

        // パスワード入力があれば更新
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 権限(role)もDTOの値で更新
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            entity.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        }

        adminRepository.save(entity);
    }

    @Override
    public void deleteAdmin(Integer adminNum) {
        // adminNumで該当管理者を削除
        adminRepository.deleteById(adminNum);
    }
}

