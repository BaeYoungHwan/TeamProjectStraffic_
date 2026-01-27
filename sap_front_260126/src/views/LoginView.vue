<template>
  <div class="login-page">
    <div class="login-card">
      <h2>테스트 로그인</h2>
      
      <div class="input-group">
        <label>아이디</label>
        <input v-model="userId" type="text" placeholder="아이디 입력" @keyup.enter="submitLogin" />
      </div>

      <div class="input-group">
        <label>이름</label>
        <input v-model="userName" type="text" placeholder="이름 입력" @keyup.enter="submitLogin" />
      </div>

      <div class="input-group">
        <label>노선 선택 (1~9호선 테마)</label>
        <input 
          v-model.number="line_name" 
          type="number" 
          min="1" 
          max="9" 
          placeholder="1, 2, 3 ... 입력" 
          @keyup.enter="submitLogin"
        />
      </div>

      <button class="login-btn" @click="submitLogin">테마 적용하여 로그인</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const userId = ref('');
const userName = ref('');
const line_name = ref(1); // themeNum에서 line_name으로 변경

const emit = defineEmits(['login-success']);

const submitLogin = () => {
  if (!userId.value || !userName.value) {
    alert('아이디와 이름을 입력해주세요!');
    return;
  }
  
  // 부모 컴포넌트로 데이터 전송
  emit('login-success', {
    id: userId.value,
    name: userName.value,
    line_name: line_name.value // 부모 App.vue의 userData.line_name과 매칭됨
  });
};
</script>

<style scoped>
.login-page { 
  display: flex; 
  justify-content: center; 
  padding-top: 50px; 
}

.login-card { 
  width: 350px; 
  padding: 30px; 
  background: white; 
  border-radius: 12px; 
  box-shadow: 0 10px 25px rgba(0,0,0,0.1); 
  text-align: center;
}

h2 { margin-bottom: 20px; color: #2c3e50; }

.input-group { margin-bottom: 15px; text-align: left; }
.input-group label { 
  display: block; 
  margin-bottom: 5px; 
  font-size: 0.8rem; 
  font-weight: bold; 
  color: #555;
}

.input-group input { 
  width: 100%; 
  padding: 10px; 
  border: 1px solid #ddd; 
  border-radius: 6px; 
  font-size: 14px;
  outline: none;
}

.input-group input:focus {
  border-color: #2c3e50;
}

.login-btn { 
  width: 100%; 
  padding: 12px; 
  background-color: #2c3e50; 
  color: white; 
  border: none; 
  border-radius: 6px; 
  cursor: pointer; 
  font-weight: bold;
  margin-top: 10px;
  transition: opacity 0.2s;
}

.login-btn:hover {
  opacity: 0.9;
}
</style>