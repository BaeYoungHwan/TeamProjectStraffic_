<template>
  <div class="admin_user_management">
    
    <div id="newuser_list_container" class="mypage_wrapper mb_5">
      <div class="mypage_header">
        <h2 class="title">신규 사용자 승인 대기 목록</h2>
        
        <div class="header_right_group">
          <div class="batch_control_area" v-if="selected_new_user_ids.length > 0">
            <span class="selected_info">선택됨 <b>{{ selected_new_user_ids.length }}</b>명</span>
            <div class="batch_btn_group">
              <template v-if="selected_new_user_ids.length === 1">
                <button class="batch_approve_btn" @click="batch_approve_users">승인</button>
                <button class="batch_reject_btn" @click="batch_reject_users">거절</button>
              </template>
              <template v-else>
                <button class="batch_approve_btn" @click="batch_approve_users">일괄 승인</button>
                <button class="batch_reject_btn" @click="batch_reject_users">일괄 거절</button>
              </template>
            </div>
          </div>
          <div class="user_count">대기 인원: {{ cnt_new }}명</div>
        </div>
      </div>

      <div class="info_section">
        <div class="info_container">
          <table class="user_table">
            <thead>
              <tr>
                <th><input type="checkbox" v-model="is_all_new_selected" @change="select_all_new_users"></th>
                <th>No</th>
                <th>ID</th>
                <th>이름</th>
                <th>소속 호선</th>
                <th>소속역</th>
                <th>상태</th>
                <th>관리</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(user, index) in users_new" :key="'new_' + user.user_id" :class="{ 'row_checked': user.checked }">
                <td><input type="checkbox" v-model="user.checked"></td>
                <td class="value_bold">{{ cnt_new - ((page_new - 1) * 5) - index }}</td>
                <td class="value_id">{{ user.user_id }}</td>
                <td>{{ user.user_name }}</td>
                <td><div class="display_box" :class="'line_' + user.line_name">{{ user.line_name }}</div></td>
                <td><div class="display_box station_box">{{ user.station_name }}</div></td>
                <td><span class="status_wait">승인대기</span></td>
                <td>
                  <div class="btn_group">
                    <button class="approve_btn" @click="approve_user(user.user_id)">승인</button>
                    <button class="reject_btn" @click="reject_user(user.user_id)">거절</button>
                  </div>
                </td>
              </tr>
              <tr v-if="users_new.length === 0">
                <td colspan="8" class="empty_msg">대기 중인 사용자가 없습니다.</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="pagination_container">
          <b-pagination v-model="page_new" :total-rows="cnt_new" :per-page="5" @input="get_newuser_list" align="center"></b-pagination>
        </div>
      </div>
    </div>

    <div id="olduser_list_container" class="mypage_wrapper">
      <div class="mypage_header">
        <h2 class="title">기존 가입 계정</h2>
        <div class="search_area">
          <input type="text" v-model="search_keyword" placeholder="ID 또는 이름 검색" @keyup.enter="search_users" class="search_input">
          <button class="search_btn" @click="search_users">검색</button>
        </div>
        <div class="user_count">가입 인원: {{ cnt_old }}명</div>
      </div>

      <div class="info_section">
        <div class="info_container">
          <table class="user_table">
            <thead>
              <tr>
                <th>No</th>
                <th>ID</th>
                <th>이름</th>
                <th>소속 호선</th>
                <th>소속역</th>
                <th>권한</th>
                <th>수정/삭제</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(user, index) in users_old" :key="'old_' + user.user_id">
                <td class="value_bold">{{ index + 1 }}</td>
                <td class="value_id">{{ user.user_id }}</td>
                <td>{{ user.user_name }}</td>
                <td><div class="display_box" :class="'line_' + user.line_name">{{ user.line_name }}</div></td>
                <td><div class="display_box station_box">{{ user.station_name }}</div></td>
                
                <td>
                  <span v-if="!user.is_edit" class="status_auth">
                    <b v-if="user.auth == 1" style="color: #e6265b;">마스터</b>
                    <b v-else-if="user.auth == 2" style="color: #3f417e;">운영자</b>
                    <b v-else-if="user.auth == 4" style="color: #666;">비활성화</b>
                    <span v-else>일반({{ user.auth }})</span>
                  </span>
                  <select v-else v-model="user.auth" class="auth_select">
                    <option value="1">마스터</option>
                    <option value="2">운영자</option>
                  </select>
                </td>

                <td>
                  <div class="btn_group">
                    <template v-if="!user.is_edit">
                      <button class="edit_btn" @click="toggle_edit(user, true)">수정</button>
                      <button class="delete_btn" @click="delete_user(user.user_id)">삭제</button>
                    </template>
                    <template v-else>
                      <button class="save_btn" @click="update_auth(user)">저장</button>
                      <button class="cancel_btn" @click="toggle_edit(user, false)">취소</button>
                    </template>
                  </div>
                </td>
              </tr>
              <tr v-if="users_old.length === 0">
                <td colspan="7" class="empty_msg">데이터가 없습니다.</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="more_container" v-if="cnt_old > 5 && users_old.length < cnt_old">
          <button class="more_btn" @click="get_olduser_list" :disabled="is_loading">
            {{ is_loading ? '로딩 중...' : '사용자 더보기 ↓' }}
          </button>
        </div>
        <div v-else-if="cnt_old > 0" class="end_msg">모든 사용자를 불러왔습니다.</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserManagement',
  data() {
    return {
      users_new: [], page_new: 1, cnt_new: 0,
      users_old: [], page_old: 1, cnt_old: 0,
      search_keyword: "", is_loading: false,
      is_all_new_selected: false 
    }
  },
  computed: {

    selected_new_user_ids() {
      return this.users_new.filter(u => u.checked).map(u => u.user_id);
    },
   
    selected_user_ids() {
      return this.users_old.filter(u => u.checked).map(u => u.user_id);
    }
  },
  mounted() {
    this.get_newuser_list();
    this.get_olduser_list();
  },
  methods: {
    
     /* 1. 신규 가입 대기자 목록 조회*/
    get_newuser_list() {

      axios.get("http://localhost:9000/get_newuserlist", { params: { pageNumber: this.page_new } })
        .then(resp => {
          this.users_new = resp.data.newuserlist.map(user => ({
            ...user,
            checked: false 
          }));
          this.cnt_new = resp.data.cnt;
          this.is_all_new_selected = false;
        });
    },
 
    select_all_new_users() {
      this.users_new.forEach(user => {
        user.checked = this.is_all_new_selected;
      });
    },
    
    batch_approve_users() {
      const selected_ids = this.selected_new_user_ids; 

      if (selected_ids.length === 0) {
        alert("선택된 사용자가 없습니다.");
        return;
      }

      if (confirm(`선택한 ${selected_ids.length}명을 모두 승인하시겠습니까?`)) {
        axios.post("http://localhost:9000/approve_users_batch", { 
            userIds: selected_ids // 백엔드 DTO 필드명일 확률이 높아 유지
          })
          .then(resp => {
            if (resp.data === "YES") {
              alert("모두 승인되었습니다.");
              this.get_newuser_list();
              this.search_users();
            }
          })
          .catch(err => console.error(err));
      }
    },
    /*3. 여러 명 한꺼번에 거절 (일괄 거절) */
    batch_reject_users() {
      const selected_ids = this.selected_new_user_ids;

      if (selected_ids.length === 0) {
        alert("선택된 사용자가 없습니다.");
        return;
      }

      if (confirm(`선택한 ${selected_ids.length}명의 요청을 거절하시겠습니까?`)) {
        // 백엔드 @PostMapping("reject_usersBatch") 주소와 일치시킴
        axios.post("http://localhost:9000/reject_usersBatch", { 
            userIds: selected_ids 
          })
          .then(resp => {
            if (resp.data === "YES") {
              alert("선택한 요청이 모두 거절(삭제)되었습니다.");
              this.get_newuser_list(); // 신규 목록 새로고침
            } else {
              alert("처리에 실패했습니다.");
            }
          })
          .catch(err => {
            console.error("일괄 거절 에러:", err);
            alert("서버 통신 중 오류가 발생했습니다.");
          });
      }
    },
    search_users() {
      this.users_old = [];
      this.page_old = 1;
      this.get_olduser_list();
    },
    /*4. 기존 사용자 목록 조회 (더보기 방식) */
    get_olduser_list() {
      if (this.is_loading) return;
      this.is_loading = true;
      axios.get("http://localhost:9000/get_olduserlist", { 
        params: { pageNumber: this.page_old, searchKeyword: this.search_keyword } 
      })
      .then(resp => {
        const new_list = resp.data.olduserlist.map(user => ({
          ...user,
          is_edit: false,          // 변경: isEdit -> is_edit
          original_auth: user.auth, // 변경: originalAuth -> original_auth
          checked: false
        }));
        this.cnt_old = resp.data.cnt;
        if (new_list.length > 0) {
          const existing_ids = new Set(this.users_old.map(u => u.user_id));
          const filtered_new_list = new_list.filter(u => !existing_ids.has(u.user_id));
          this.users_old = [...this.users_old, ...filtered_new_list];
          this.page_old++;
        }
      })
      .finally(() => { this.is_loading = false; });
    },
    // 변경: toggleEdit -> toggle_edit
    toggle_edit(user, mode) {
      if (!mode) user.auth = user.original_auth;
      user.is_edit = mode;
    },
    /*5. 사용자 권한 수정 저장*/
    update_auth(user) {
      if(confirm(`${user.user_id}의 권한을 변경하시겠습니까?`)) {
        axios.post("http://localhost:9000/update_olduser_auth", { user_id: user.user_id, auth: user.auth })
        .then(resp => {
          if(resp.data === "YES") {
            alert("변경되었습니다.");
            user.original_auth = user.auth;
            user.is_edit = false;
          }
        });
      }
    },
    approve_user(user_id) {
      if(confirm(`${user_id} 사용자를 승인하시겠습니까?`)) {
        axios.post("http://localhost:9000/approve_user", { user_id: user_id })
          .then(resp => { 
            if(resp.data === "YES") { this.get_newuser_list(); this.search_users(); }
          });
      }
    },
    reject_user(user_id) {
      if(confirm(`${user_id} 요청을 거절하시겠습니까?`)) {
        axios.post("http://localhost:9000/reject_user", { user_id: user_id })
          .then(resp => { 
            if(resp.data === "YES") { this.get_newuser_list(); this.search_users(); }
          });
      }
    },
    delete_user(user_id) {
      if(confirm(`${user_id} 계정을 삭제하시겠습니까?`)) {
        axios.post("http://localhost:9000/reject_user", { user_id: user_id })
          .then(resp => { 
            if(resp.data === "YES") { 
              alert("삭제되었습니다.");
              this.search_users(); 
            }
          });
      }
    }
  }
}
</script>

<style scoped>
/* 헤더 우측 그룹 정렬 */
.header_right_group { display: flex; align-items: center; gap: 12px; }

/* 일괄 관리 컨트롤 바 디자인 */
.batch_control_area {
  display: flex; align-items: center; background-color: #fff; padding: 4px 12px;
  border-radius: 30px; border: 1px solid #3f417e; box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  animation: fadeInRight 0.3s ease-out;
}

@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(10px); }
  to { opacity: 1; transform: translateX(0); }
}

.selected_info { font-size: 12px; margin-right: 10px; color: #3f417e; white-space: nowrap; }
.batch_btn_group { display: flex; gap: 5px; }

.batch_approve_btn { background-color: #3f417e; color: white; border: none; padding: 4px 10px; border-radius: 15px; font-size: 11px; cursor: pointer; }
.batch_reject_btn { background-color: #fff; color: #d9534f; border: 1px solid #d9534f; padding: 3px 10px; border-radius: 15px; font-size: 11px; cursor: pointer; }

/* 기존 스타일 */
.admin_user_management { padding: 20px; }
.mypage_wrapper { width: 100%; background-color: #ffffff; font-family: 'Pretendard', sans-serif; color: #333; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; }
.mb_5 { margin-bottom: 50px; }
.mypage_header { display: flex; justify-content: space-between; align-items: center; padding: 15px 25px; background-color: #eeeeee; border-bottom: 1px solid #ddd; }
.title { font-size: 18px; font-weight: 700; margin: 0; white-space: nowrap; }
.search_area { display: flex; gap: 8px; margin: 0 20px; flex: 1; max-width: 350px; }
.search_input { flex: 1; padding: 6px 15px; border-radius: 20px; border: 1px solid #ccc; font-size: 13px; outline: none; }
.search_btn { background-color: #3f417e; color: #fff; border: none; padding: 6px 18px; border-radius: 20px; font-size: 13px; cursor: pointer; }
.user_count { background-color: #3f417e; color: #fff; padding: 5px 18px; border-radius: 20px; font-size: 12px; }
.info_section { padding: 20px; }
.user_table { width: 100%; border-collapse: collapse; }
.user_table th { text-align: center; padding: 12px; border-bottom: 2px solid #eee; font-weight: 700; color: #666; font-size: 14px; }
.user_table td { padding: 12px 10px; border-bottom: 1px solid #f5f5f5; vertical-align: middle; text-align: center; font-size: 14px; }
.row_checked { background-color: #f4f6ff !important; }
.value_bold { font-weight: 700; color: #999; }
.value_id { font-weight: 700; color: #3f417e; }
.display_box { padding: 4px 10px; border-radius: 6px; font-size: 12px; color: #fff; display: inline-block; min-width: 60px; }
.station_box { background-color: gray; color: white; border: 1px solid #ddd; }
.status_wait { color: #ff6000; font-weight: 700; }
.approve_btn, .edit_btn, .save_btn { background-color: #3f417e; color: #fff; border: none; padding: 5px 15px; border-radius: 20px; font-size: 12px; cursor: pointer; }
.reject_btn, .delete_btn, .cancel_btn { background-color: #fff; color: #d9534f; border: 1px solid #d9534f; padding: 4px 14px; border-radius: 20px; font-size: 12px; cursor: pointer; margin-left: 5px; }

.line_1호선 { background-color: #2a317c; } .line_2호선 { background-color: #2fae35; } 
.line_3호선 { background-color: #ff6000; } .line_4호선 { background-color: #1a97dd; }
.line_5호선 { background-color: #822fe1; } .line_6호선 { background-color: #ae4908; }
.line_7호선 { background-color: #636b10; } .line_8호선 { background-color: #e6265b; }
.line_9호선 { background-color: #bdb092; }

/* 더보기 버튼 컨테이너 */
.more_container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding-bottom: 10px;
}

/* 더보기 버튼 스타일 */
.more_btn {
  background-color: #fff;
  color: #3f417e;
  border: 1px solid #3f417e;
  padding: 10px 40px;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
  max-width: 300px;
}

.more_btn:hover {
  background-color: #3f417e;
  color: #fff;
  box-shadow: 0 4px 8px rgba(63, 65, 126, 0.2);
}

.more_btn:disabled {
  background-color: #eee;
  border-color: #ddd;
  color: #999;
  cursor: not-allowed;
}

/* 모든 데이터를 불러왔을 때 메시지 */
.end_msg {
  text-align: center;
  color: #999;
  font-size: 13px;
  margin-top: 20px;
  padding: 10px;
  border-top: 1px dashed #eee;
}

/* 페이지네이션 컨테이너 */
.pagination_container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 비어있는 목록 메시지 */
.empty_msg {
  padding: 50px !important;
  color: #999;
  font-style: italic;
}

::v-deep .page-item.active .page-link { background-color: #3f417e !important; border-color: #3f417e !important; color: #fff !important; }
</style>