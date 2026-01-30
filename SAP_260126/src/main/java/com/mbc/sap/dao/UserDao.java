package com.mbc.sap.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.sap.dto.UserDto;
import com.mbc.sap.dto.UserParam;

@Mapper
@Repository
public interface UserDao {
	UserDto login(UserDto dto);
	int idcheck(String user_id);
	int adduser(UserDto dto);
	List<String> search_stationid(UserDto dto);
	List<Map<String, Object>> getstationname(String line_name);
	int update_user(UserDto dto);
	List<UserDto> get_newuserlist();
	int count_newuser();
	int reject_user(UserDto dto);
	int approve_user(UserDto dto);
	List<UserDto> get_olduserlist(UserParam param);
	int count_olduser(UserParam param);
	int approveUsersBatch(List<String> userIds);
	int rejectUsersBatch(List<String> userIds);
	int update_olduser_auth(UserDto dto);
	int reject_usersBatch(List<String> userIds);
}
