package com.mbc.sap.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.sap.dto.UserDto;
import com.mbc.sap.dto.UserParam;
import com.mbc.sap.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

	@Autowired
	UserService service;
	
	// 유저테이블에서 로그인을 위한 ID, PW 체크 기능
	@PostMapping("check_login") // 기능_기능설명
	public UserDto login(UserDto dto, HttpSession session) {
		// System.out.println("UserController login " + new Date());
		// System.out.println("전달된 데이터: " + dto.toString()); // 데이터 유입 확인용
		
		UserDto user = service.login(dto);
		System.out.println(service.login(dto));
		System.out.println(user);
		if(user != null) {
			session.setAttribute("login", user);
		}
		
		return user;
	}
	
	// 회원가입시 user_id 중복확인 
	@PostMapping("check_userid")
	public String check_id(String user_id) {
		// System.out.println("UserController check_id " + new Date());
		
		boolean isS = service.idcheck(user_id);
		String message = "YES";
		
		if(isS) {
			message = "NO";
		}
		return message;
	}
	
	// 회원가입 성공시
	@PostMapping("add_regi")
	public String add_regi(UserDto dto) {
		// System.out.println("UserController add_regi " + new Date());
		
		boolean isS = service.adduser(dto);
		String message = "YES";
		
		if(!isS) {
			message = "NO";
		}
		return message;
	}
	
	// 회원가입시 station_name을 입력하면 station_id를 불러오기
	@PostMapping("search_stationid")
	public List<String> search_stationid(UserDto dto){
		// System.out.println("UserController search_stationid " + new Date());
	
		return service.search_stationid(dto);
	}
	
	// 몇호선을 선택하면 해당 호선의 역이 출력
	@GetMapping("search_stationname")
	public List<Map<String, Object>> search_stationName(@RequestParam("line_name") String line_name){
		// System.out.println("UserController search_station " + new Date());
		
		return service.getstationname(line_name);
	}
	 //마이페이지 정보 수정
    @PostMapping("update_user") 
    public String update_user(@RequestBody UserDto dto) {
       
        System.out.println("전달받은 데이터 확인: " + dto.toString()); 
        
        boolean isS = service.update_user(dto);
        
        if(isS) {
            return "YES";
        } else {
            return "NO";
        }
    }
	
    @GetMapping("get_newuserlist")
    public Map<String, Object> getNewUserList(UserParam param) {
        System.out.println("요청된 페이지: " + param.getPageNumber());
        
        List<UserDto> list = service.get_newuserlist(param);
        
        // 2. 전체 대기자 수 (전체 행 개수)
        int count = service.count_newuser();
        list.forEach(user -> System.out.println("조회된 유저: " + user.toString()));
        // 3. 응답 맵 구성
        Map<String, Object> map = new HashMap<>();
        map.put("newuserlist", list);  // 데이터 리스트
        map.put("cnt", count);         // 전체 개수 (Total Count)
        map.put("curPage", param.getPageNumber()); // 현재 페이지 번호 저장용
        
        return map;
    }
    //신규유저 승인 거절
    @PostMapping("reject_user") 
    public String reject_user(@RequestBody UserDto dto) {
     
        System.out.println("전달받은 데이터 확인: " + dto.toString()); 
        
        boolean isS = service.reject_user(dto);
        
        if(isS) {
            return "YES";
        } else {
            return "NO";
        }
    }
    //신규 유저 승인 완료
    @PostMapping("approve_user") 
    public String approve_user(@RequestBody UserDto dto) {
      
        System.out.println("전달받은 데이터 확인: " + dto.toString()); 
        
        boolean isS = service.approve_user(dto);
        
        if(isS) {
            return "YES";
        } else {
            return "NO";
        }
    }
    
    @GetMapping("get_olduserlist")
    public Map<String, Object> get_olduserlist(UserParam param) {
        //현재 페이지 번호를 바탕으로 DB 시작점(offset) 계산
        param.setOffset((param.getPageNumber() - 1) * 5); 

        System.out.println("요청된 페이지: " + param.getPageNumber());
        System.out.println("계산된 오프셋: " + param.getOffset()); // 로그로 확인 가능
        System.out.println("검색어: " + param.getSearchKeyword());
        
        // 1. 리스트 조회 (이제 계산된 offset이 적용된 쿼리가 나갑니다)
        List<UserDto> list = service.get_olduserlist(param);
        
        // 2. 검색어가 적용된 전체 결과 개수 조회
        int count = service.count_olduser(param); 
        
        Map<String, Object> map = new HashMap<>();
        map.put("olduserlist", list);
        map.put("cnt", count);
        map.put("curPage", param.getPageNumber());
        
        return map;
    }
    
    @PostMapping("approve_users_batch")
    public String approveUsersBatch(@RequestBody Map<String, Object> params) {
    	  System.out.println("전체 추가 버튼 활성화");
        List<String> userIds = (List<String>) params.get("userIds");
        boolean isSuccess = service.approveUsersBatch(userIds);
        return isSuccess ? "YES" : "NO";
    }
    
    @PostMapping("reject_usersBatch")
    public String reject_usersBatch(@RequestBody Map<String, Object> params) {
    	  System.out.println("전체 삭제 버튼");
        List<String> userIds = (List<String>) params.get("userIds");
        boolean isSuccess = service.reject_usersBatch(userIds);
        return isSuccess ? "YES" : "NO";
    }
    
    @PostMapping("update_olduser_auth") 
    public String  update_olduser_auth(@RequestBody UserDto dto) {
       
        System.out.println("전달받은 데이터 확인: " + dto.toString()); 
        
        boolean isS = service. update_olduser_auth(dto);
        
        if(isS) {
            return "YES";
        } else {
            return "NO";
        }
    }
    
}
