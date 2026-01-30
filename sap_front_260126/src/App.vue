<template>
  <div v-if="!isLoggedIn" class="login-fullscreen-container">
      <Login v-if="currentView === 'Login'" 
          @login-success="handleLoginSuccess"
          @go-regi="currentView = 'Regi'" />

      <Regi v-if="currentView === 'Regi'" 
        @go-login="currentView = 'Login'" />
  </div>

  <div v-else class="layout" :class="'line_name-' + userData.line_name">
    <header class="header">
      <div class="header-left-col" @click="currentView = 'Home'">
        <div class="logo-wrapper">
          <img :src="logo" alt="Logo" class="company-logo" />
        </div>
      </div>
      
      <div class="header-right-col">
        <div class="header-status-row">
          <div class="user-profile-box">
            <div class="user-details-row">
              <span class="u-name">{{ userData.name }} 관리자</span>
              <span class="u-sep"> | </span>
              <span class="u-station">{{ userData.station_name }}역</span>
              <span class="u-sep"> | </span>
              <button @click="logout" class="logout-small-btn">로그아웃</button>
              <span class="user-avatar" @click="currentView = 'my'" style="cursor:pointer">
                <img :src="mypageimage" alt="mypageimage" class="header-mypageimage" />
              </span>
            </div>
          </div>
        </div>

        <div class="header-info-row">
          <div class="sub-nav-text">
            <span class="dot">●</span> 실시간 관리 시스템 접속 중 : {{ userData.line_name }}호선 모니터링
          </div>
        </div>
      </div>
    </header>

    <div class="container">
      <aside class="sidebar">
        <nav>
          <div class="nav-item" @click="currentView = 'Home'" :class="{ active: currentView === 'Home' }">▶대시보드</div>
          <div class="nav-item" @click="currentView = 'Dashboard'" :class="{ active: currentView === 'Dashboard' }">▶역별현황</div>
          <div class="nav-item" @click="currentView = 'analysis'" :class="{ active: currentView === 'analysis' }">▶통계 분석</div>
          <div class="nav-item" @click="currentView = 'Issue'" :class="{ active: currentView === 'Issue' }">▶장애/이슈</div>
          <div class="nav-item" @click="currentView = 'management'" :class="{ active: currentView === 'management' }">▶사용자 관리</div>
        </nav>
      </aside>

      <main class="content">
        <component 
          :is="views[currentView]" 
          :key="currentView" 
          @change-view="currentView = $event" />
      </main>
    </div>

    <footer class="footer">
      <div class="footer-content">
        <img :src="logofooter" alt="footer logo" class="footer-inline-logo" />
        <span class="copyright">
          <table>
            <tr><td>배영환 | Project Manger | uee8351773@naver.com</td></tr>
            <tr><td>김소연 | Consultant | www.linkedin.com/in/souyeon-kim-735996394</td></tr>
            <tr><td>송원호 | Developer | dnjsghman@naver.com</td></tr>
            <tr><td>오창석 | Developer | dhckdtjr11@naver.com</td></tr>
          </table>
        </span>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import logo from './assets/로고.png'
import logofooter from './assets/로고 글자.png'
import mypageimage from './assets/마이페이지(W).png'
import Home from './views/HomeView.vue';
import Dashboard from './views/DashboardView.vue';
import management from './views/management.vue';
import Login from './views/LoginView.vue';
import Regi from './views/regi.vue';
import myedit from './views/myedit.vue';
import my from './views/my.vue';

const views = { Home, Dashboard, management, Login, Regi , myedit, my };
const currentView = ref('Login');
const isLoggedIn = ref(false);

const userData = reactive({
  id: '',
  name: '',
  line_name: 'default',
  station_name: ''
});

const applyUserData = (payload) => {
  if (!payload) return;
  userData.id = payload.user_id || payload.id || '';
  userData.name = payload.user_name || payload.name || '';
  userData.line_name = payload.line_name || 'default';
  userData.station_name = payload.station_name || '';
  isLoggedIn.value = true;
  currentView.value = 'Home';
};

onMounted(() => {
  const savedUser = sessionStorage.getItem("user_info");
  if (savedUser) {
    try {
      const payload = JSON.parse(savedUser);
      applyUserData(payload);
    } catch (e) {
      sessionStorage.removeItem("user_info");
    }
  }
});

const handleLoginSuccess = (payload) => {
  applyUserData(payload);
  sessionStorage.setItem("user_info", JSON.stringify(payload));
};

const logout = () => {
  isLoggedIn.value = false;
  userData.id = '';
  userData.name = '';
  userData.line_name = 'default';
  sessionStorage.removeItem("user_info");
  currentView.value = 'Login';
};
</script>

<style>
/* --- 전역 설정 --- */
* { margin: 0; padding: 0; box-sizing: border-box; }
body { overflow-y: auto; overflow-x: hidden; min-height: 100vh; }

/* --- 레이아웃 (Sticky Footer 핵심) --- */
.layout { 
  display: flex; 
  flex-direction: column; 
  min-height: 100vh; /* 화면 높이를 최소한으로 확보 */
  width: 100vw;
  font-family: 'Pretendard', sans-serif; 
}

.container { 
  display: flex; 
  flex: 1; /* 남는 공간을 모두 채워서 푸터를 아래로 밀어냄 */
}

/* --- 헤더 --- */
.header {
  height: 160px;
  display: flex;
  background-color: #2c3e50;
  color: white;
  border-bottom: 2px solid rgba(0,0,0,0.1);
  flex-shrink: 0;
}

.header-left-col {
  width: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px solid rgba(255,255,255,0.1);
  cursor: pointer;
}

.company-logo { width: 180px; height: auto; }

.header-right-col { flex: 1; display: flex; flex-direction: column; }
.header-status-row { flex: 1.2; display: flex; align-items: center; justify-content: flex-end; padding: 0 30px; }
.header-info-row {
  flex: 0.8;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 30px;
  background-color: rgba(0, 0, 0, 0.1);
  position: relative;
}

.sub-nav-text { font-size: 1.2rem; font-weight: 500; z-index: 2; }

/* --- 사이드바 --- */
.sidebar {
  width: 240px;
  background-color: #f8f9fa;
  border-right: 1px solid #ddd;
  padding: 20px;
  flex-shrink: 0;
}

.nav-item { padding: 12px; margin-bottom: 5px; cursor: pointer; border-radius: 8px; color: #555; transition: 0.2s; }
.nav-item.active { color: white !important; font-weight: bold; }

/* --- 컨텐츠 --- */
.content { flex: 1; padding: 30px; background-color: #fcfcfc; }

/* --- 푸터 (컨텐츠가 짧아도 바닥 고정) --- */
.footer {
  width: 100%;
  min-height: 100px;
  background-color: #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: auto; /* 내용이 적을 때 바닥으로 밀착 */
  padding: 20px 0;
}

.footer-content { display: flex; align-items: center; gap: 20px; }
.footer-inline-logo { height: 60px; width: auto; }
.footer .copyright table { color: #666; border-collapse: collapse; font-size: 0.8rem; }

/* --- 호선별 테마 스타일 --- */
.layout.line_name-1호선 .header, .layout.line_name-1호선 .footer { background-color: #2a317c !important; }
.layout.line_name-1호선 .nav-item.active { background-color: #2a317c !important; }

.layout.line_name-2호선 .header, .layout.line_name-2호선 .footer { background-color: #2fae35 !important; }
.layout.line_name-2호선 .nav-item.active { background-color: #2fae35 !important; }

.layout.line_name-3호선 .header, .layout.line_name-3호선 .footer { background-color: #ff6000 !important; }
.layout.line_name-3호선 .nav-item.active { background-color: #ff6000 !important; }

.layout.line_name-4호선 .header, .layout.line_name-4호선 .footer { background-color: #1a97dd !important; }
.layout.line_name-4호선 .nav-item.active { background-color: #1a97dd !important; }

.layout.line_name-5호선 .header, .layout.line_name-5호선 .footer { background-color: #822fe1 !important; }
.layout.line_name-5호선 .nav-item.active { background-color: #822fe1 !important; }

.layout.line_name-6호선 .header, .layout.line_name-6호선 .footer { background-color: #ae4908 !important; }
.layout.line_name-6호선 .nav-item.active { background-color: #ae4908 !important; }

.layout.line_name-7호선 .header, .layout.line_name-7호선 .footer { background-color: #636b10 !important; }
.layout.line_name-7호선 .nav-item.active { background-color: #636b10 !important; }

.layout.line_name-8호선 .header, .layout.line_name-8호선 .footer { background-color: #e6265b !important; }
.layout.line_name-8호선 .nav-item.active { background-color: #e6265b !important; }

.layout.line_name-9호선 .header, .layout.line_name-9호선 .footer { background-color: #bdb092 !important; }
.layout.line_name-9호선 .nav-item.active { background-color: #bdb092 !important; }

/* 테마 적용 시 푸터 글자 흰색 고정 */
.layout[class*="line_name-"] .footer .copyright table { color: white !important; }

/* --- 기타 컴포넌트 스타일 --- */
.login-fullscreen-container {
  width: 100vw; height: 100vh;
  display: flex; justify-content: center; align-items: center;
  background: linear-gradient(135deg, #2c3e50 0%, #000000 100%);
}
.logout-small-btn {
  background: transparent; border: 1px solid rgba(255, 255, 255, 0.5); color: white;
  padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 13px;
}
.header-mypageimage { width: 32px; height: 32px; border-radius: 50%; margin-left: 10px; vertical-align: middle; }
</style>