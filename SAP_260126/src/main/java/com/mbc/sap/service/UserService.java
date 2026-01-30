package com.mbc.sap.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.sap.dao.UserDao;
import com.mbc.sap.dto.UserDto;
import com.mbc.sap.dto.UserParam;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserDao dao;
	
	public UserDto login(UserDto dto) {
		return dao.login(dto);
	}
	
	public boolean idcheck(String user_id) {
		return dao.idcheck(user_id)>0;
	}
	
	public boolean adduser(UserDto dto) {
		return dao.adduser(dto)>0;
	}
	
	public List<String> search_stationid(UserDto dto) {
		return dao.search_stationid(dto);
	}
	
	public List<Map<String, Object>> getstationname(String line_name){
		return dao.getstationname(line_name);
	}

	public boolean update_user(UserDto dto) {

		return dao.update_user(dto)>0;
	}

	public List<UserDto> get_newuserlist(UserParam param) {
		return dao.get_newuserlist();}

	public int count_newuser() {
		return dao.count_newuser();}

	public boolean reject_user(UserDto dto) {
		// TODO Auto-generated method stub
		return dao.reject_user(dto)>0;
	}

	public boolean approve_user(UserDto dto) {
		return dao.approve_user(dto)>0;
	}

	public List<UserDto> get_olduserlist(UserParam param) {
		return dao.get_olduserlist(param);
		}

	public int count_olduser(UserParam param) {
		return dao.count_olduser(param);
	}

	// 일괄 승인
    public boolean approveUsersBatch(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) return false;
        return dao.approveUsersBatch(userIds) > 0;
    }

    // 일괄 거절 (나중에 버튼 누를 때 필요함)
    public boolean rejectUsersBatch(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) return false;
        return dao.rejectUsersBatch(userIds) > 0;
    }

	public boolean update_olduser_auth(UserDto dto) {
		return dao.update_olduser_auth(dto)>0;
	}

	public boolean reject_usersBatch(List<String> userIds) {
		  if (userIds == null || userIds.isEmpty()) return false;
	        return dao.reject_usersBatch(userIds) > 0;
	    }

	

}
