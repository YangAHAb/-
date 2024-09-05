<script setup>
import { ref } from 'vue';
import { get, post } from '@/service/request';

// 定义表单数据
const username = ref('');
const password = ref('');

// 注册方法
const register = async () => {
    try {
        const response = await post('/register', {
            username: username.value,
            password: password.value
        });
        console.log('Registration success' + response.data); // 显示成功信息
    } catch (error) {
        if (error.response) {
            console.log('Registration failed: ' + error.response.data); // 显示错误信息
        } else {
            console.log('An error occurred when registering');
        }
    }
};

// 登录方法
const login = async () => {
    try {
        const response = await post('/login', {
            username: username.value,
            password: password.value
        });
        console.log(response.data); // 显示成功信息
    } catch (error) {
        if (error.response) {
            console.log('Login failed: ' + error.response.data); // 显示错误信息
        } else {
            console.log('An error occurred when logging.');
        }
    }
};
</script>

<template>
    <div>
        <h2>Register</h2>
        <input v-model="username" placeholder="Username" />
        <input v-model="password" placeholder="Password" />
        <!-- <input v-model="password" type="password" placeholder="Password" /> -->
        <button @click="register">Register</button>

        <h2>Login</h2>
        <input v-model="username" placeholder="Username" />
        <input v-model="password" placeholder="Password" />
        <!-- <input v-model="password" type="password" placeholder="Password" /> -->
        <button @click="login">Login</button>
    </div>
</template>
