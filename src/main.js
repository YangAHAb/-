//import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import router from './router/router'
import App from './App.vue'
import { useUserStore } from './store/store'
import './router/logingaurds'


const pinia = createPinia()

const app = createApp(App)

app.use(pinia)
export const store = useUserStore()

app.use(router)
app.use(ElementPlus)
app.mount('#app')

