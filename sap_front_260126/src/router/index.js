import { createRouter, createWebHistory } from 'vue-router'

// 1. 컴포넌트들을 상단에서 모두 import (정적 임포트 방식)
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import MyView from '../views/my.vue'
import MyEditView from '../views/myedit.vue'
import DashboardView from '../views/DashboardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { 
      path: '/', 
      name: 'home', 
      component: HomeView 
    },
    { 
      path: '/user/login',
      name: 'login', 
      component: LoginView 
    },
    { 
      path: '/main/dashboard', 
      name: 'dashboard', 
      component: DashboardView 
    },
    { 
      path: '/user/my', 
      name: 'mypage', 
      component: MyView 
    },
    { 
      path: '/user/my/edit', 
      name: 'myedit', 
      component: MyEditView 
    }
  ]
})

export default router