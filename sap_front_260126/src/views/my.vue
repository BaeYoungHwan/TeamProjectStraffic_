<template>
  <div class="mypage-wrapper">
    <div class="mypage-header">
      <h2 class="title">마이페이지</h2>
      <button class="edit-btn" @click="user_update">수정</button>
    </div>

    <div class="info-section">
      <h3 class="section-subtitle">직원 정보</h3>
      
      <ul class="user-info-list">
        <li>
          <span class="bullet">●</span>
          <span class="label">이름 :</span>
          <span class="value">{{ user_id }}</span> 
        </li>
        <li>
          <span class="bullet">●</span>
          <span class="label">ID :</span>
          <span class="value">{{ user_id }}</span>
        </li>
        <li>
          <span class="bullet">●</span>
          <span class="label">PW :</span>
          <span class="value">******</span>
        </li>
        <li>
          <span class="bullet">●</span>
          <span class="label">E-mail :</span>
          <span class="value">{{ user_email }}</span>
        </li>
        
        <li class="route-container">
          <div class="route-header">
            <span class="bullet">●</span>
            <span class="label">담당 노선 정보</span>
          </div>
          <div class="route-content">
            <p class="route-line">{{ line_name }}</p>
            <p class="route-station">{{ station_name }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user_id: "",
      user_email: "",
      line_name: "",
      station_name: "",
      station_id: ""
    }
  },
  mounted() {
    let login = JSON.parse(sessionStorage.getItem("login"));  

    if (login === null || login === '') {
      alert("로그인 해주세요");
      sessionStorage.setItem("location", "/my");
      this.$router.push({ name: "login" });
    } else {
      this.user_id = login.id;
      // 예시 데이터 유지를 위해 세션의 나머지 값들도 바인딩만 연결해둡니다.
      this.user_email = login.email || '';
      this.line_name = login.line_name || '';
      this.station_name = login.station_name || '';
    }
  },
  methods: {
    user_update() {
      // 부모 컴포넌트에 뷰 변경 신호를 보냄
      this.$emit('change-view', 'myedit');
    }
  }
}
</script>

<style scoped>
/* 전체 컨테이너 */
.mypage-wrapper {
  padding: 30px;
  background-color: #ffffff;
  font-family: 'Pretendard', -apple-system, sans-serif;
  color: #333;
}

/* 마이페이지 제목 + 수정 버튼 레이아웃 */
.mypage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
  padding: 20px 20px;
  margin-bottom: 25px;
  background-color: #eeeeee;
}

.title {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

/* 수정 버튼: 이미지처럼 둥근 형태와 짙은 색상 */
.edit-btn {
  background-color: #3f417e; 
  color: #fff;
  border: none;
  padding: 5px 22px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s ease;
}

.edit-btn:hover {
  background-color: #2c2e5a;
}

/* 서브 타이틀 (직원 정보) */
.section-subtitle {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
}

/* 리스트 스타일링 */
.user-info-list {
  list-style: none;
  padding: 0;
}

.user-info-list li {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 16px;
}

.bullet {
  font-size: 10px;
  margin-right: 12px;
  color: #333;
}

.label {
  font-weight: 600;
  margin-right: 10px;
  min-width: 110px; /* 라벨 정렬을 위해 너비 고정 */
}

.value {
  color: #444;
}

/* 담당 노선 정보 특수 레이아웃 */
.route-container {
  flex-direction: column;
  align-items: flex-start !important;
  margin-top: 15px;
}

.route-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.route-content {
  padding-left: 22px; /* 불렛만큼 들여쓰기 */
  color: #666;
  line-height: 1.6;
}

.route-line, .route-station {
  margin: 2px 0;
}
</style>