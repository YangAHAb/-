<template>
    <div v-if="isLoggedIn" class="logged-in-container"> <!-- 添加 class -->  
      <h2 class="welcome-message">欢迎, {{ user.username }}!</h2> <!-- 添加 class -->  
      <button type="button" @click="logout">退出登录</button>  
    </div>   
    <div v-else class="login-register-container">  
      <h2 v-if="mode === 'login'">登录</h2>  
      <h2 v-else>注册</h2>  
      <form @submit.prevent="submitForm">  
        <div class="form-group">  
          <label for="username">用户名:</label>  
          <input type="text" id="username" v-model="user.username" required>  
        </div>  
        <div class="form-group">  
          <label for="password">密码:</label>  
          <input type="password" id="password" v-model="user.password" required>  
        </div>  
        <button type="submit" style="margin: 10px;">{{ mode === 'login' ? '登录' : '注册' }}</button>  
        <button type="button" @click="toggleMode" style="margin: 10px;">{{ mode === 'login' ? '注册新账号' : '已有账号，登录' }}</button>  
      </form>  
      </div>  
</template>  
  
<script>  
export default {  
  data() {  
    return {  
      mode: 'login',  
      user: {  
        username: '',  
        password: ''  
      },  
      isLoggedIn: false  
    }  
  },  
  created() {  
    this.checkLoginStatus();  
    this.restoreUser();  
  },  
  methods: {  
    submitForm() {  
      if (this.mode === 'login') {  
        this.login();  
      } else {  
        this.register();  
      }  
    },  
    login() {  
      const storedUser = localStorage.getItem(this.user.username);  
      if (storedUser) {  
        const storedPassword = JSON.parse(storedUser).password;  
        if (storedPassword === this.user.password) {  
          alert('登录成功！');  
          this.isLoggedIn = true;  
          localStorage.setItem('isLoggedIn', true);  
          this.restoreUser();  
        } else {  
          alert('用户名或密码错误！');  
        }  
      } else {  
        alert('用户不存在！');  
      }  
    },  
    register() {  
      if (!localStorage.getItem(this.user.username)) {  
        localStorage.setItem(this.user.username, JSON.stringify(this.user));  
        alert('注册成功！');  
        this.mode = 'login';  
      } else {  
        alert('用户名已存在！');  
      }  
    },  
    toggleMode() {  
      this.mode = this.mode === 'login' ? 'register' : 'login';  
    },  
    logout() {  
      this.isLoggedIn = false;  
      localStorage.removeItem('isLoggedIn');  
      this.user.username = '';  
      this.user.password = '';  
      alert('已退出登录！');  
    },  
    checkLoginStatus() {  
      const loggedIn = localStorage.getItem('isLoggedIn');  
      if (loggedIn) {  
        this.isLoggedIn = true;  
      }  
    },  
    restoreUser() {  
      if (this.isLoggedIn) {  
        const username = Object.keys(localStorage).find(key => key !== 'isLoggedIn');  
        if (username) {  
          const storedUser = JSON.parse(localStorage.getItem(username));  
          this.user.username = storedUser.username;  
          this.user.password = storedUser.password;  
        }  
      }  
    }  
  }  
}  
</script>


<style scoped>  
.login-container {  
  max-width: 400px;  
  margin: 50px auto;  
  padding: 20px;  
  border: 1px solid #ddd;  
  border-radius: 5px;  
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);  
}  
  
.form-group {  
  margin-bottom: 15px;  
}  
  
.form-group label {  
  display: block;  
  margin-bottom: 5px;  
}  
  
.form-group input {  
  width: 100%;  
  padding: 8px;  
  border: 1px solid #ddd;  
  border-radius: 4px;  
}  
  
button {  
  width: 100%;  
  padding: 8px; 
  background-color: #007bff;  
  color: white;  
  border: none;  
  border-radius: 10px;  
  cursor: pointer;  
  font-size: 16px;
}  
  
button:hover {  
  background-color: #0056b3;  
}  
  
h2 {  
  text-align: center;  
  margin-bottom: 20px;  
}  
  
.logged-in-container {  
  display: flex;  
  flex-direction: column;  
  align-items: flex-start;  
  max-width: 400px;  
  margin: 50px auto;  
  padding: 60px;  
  border: 1px solid #ddd;  
  border-radius: 5px;  
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);   
}  
  
.welcome-message {  
  margin-bottom: 10px;  
}
.login-register-container {  
  max-width: 400px;  
  margin: 50px auto;  
  padding: 50px;  
  border: 1px solid #ddd;  
  border-radius: 5px;  
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);  
}  
</style>