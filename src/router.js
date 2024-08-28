import { createMemoryHistory, createRouter } from 'vue-router'

import HelloWorld from './components/HelloWorld.vue';
import TheWelcome from './components/TheWelcome.vue';
import UploadAndView from './components/UploadAndView.vue';
import Login from './components/Login.vue';

const routes = [
    { path: '/main', component: HelloWorld },
    { path: '/mask', component: TheWelcome },
    { path: '/upload', component: UploadAndView },
    { path: '/login',  component: Login }  
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router