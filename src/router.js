import { createMemoryHistory, createRouter } from 'vue-router'

import HelloWorld from './components/HelloWorld.vue';
import TheWelcome from './components/TheWelcome.vue';
import UploadAndView from './views/UploadAndView.vue';
import Login from './views/Login.vue';
import IdentificationResults from './views/IdentificationResults.vue';

const routes = [
    { path: '/main', component: HelloWorld },
    { path: '/mask', component: TheWelcome },
    { path: '/identification', component: IdentificationResults },
    { path: '/upload', component: UploadAndView },
    { path: '/login', component: Login }
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router