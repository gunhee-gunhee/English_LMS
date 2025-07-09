package com.english.lms.service;

import com.english.lms.dto.AdminDTO;

public interface AdminService {
	
	void registerCenter(AdminDTO dto);
    boolean existsById(String id);
	
    void registerAdmin(AdminDTO adminDTO);
}
