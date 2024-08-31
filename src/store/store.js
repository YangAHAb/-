import { defineStore } from "pinia";

// 用户状态存储
export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
    }),
    actions: {
        setToken(token) {
            this.token = token;
            localStorage.setItem('token', token);
        },
        setUserInfo(userInfo) {
            this.userInfo = userInfo;
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
        },
        removeInfo() {
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            this.token = '';
            this.userInfo = null;
        },
    },
    getters: {
        getToken: (state) => state.token,
        getUserInfo: (state) => state.userInfo,
    },
})
