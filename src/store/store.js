import { defineStore } from "pinia";

// 用户状态存储
export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
        username: localStorage.getItem('username') || '',
        identificationResults: [],
    }),
    actions: {
        setUsername(username) {
            this.username = username;
            localStorage.setItem('username', username);
        },
        setToken(token) {
            this.token = token;
            localStorage.setItem('token', token);
        },
        setUserInfo(userInfo) {
            this.userInfo = userInfo;
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
        },
        setIdentificationResults(results) {
            this.identificationResults = results;
        },
        removeInfo() {
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            localStorage.removeItem('username');
            this.token = '';
            this.userInfo = null;
            this.username = ''
        },
    },
    getters: {
        getToken: (state) => state.token,
        getUserInfo: (state) => state.userInfo,
        getUsername: (state) => state.username,
        getIdentificationResults: (state) => state.identificationResults,
    },
})
