import { createMemoryHistory, createRouter } from 'vue-router'

import HelloWorld from '../components/HelloWorld.vue';
import TheWelcome from '../components/TheWelcome.vue';
import UploadAndView from '../views/UploadAndView.vue';
import Login from '../views/Login.vue';
import IdentificationResults from '../views/IdentificationResults.vue';
import MaskResults from '../views/MaskResults.vue';
import DownloadAndShow from '../views/DownloadAndShow.vue';
import Main from '../views/Main.vue';
import LoginNew from '../views/LoginNew.vue';

const routes = [
    { path: '/main', component: LoginNew },
    { path: '/mask', component: MaskResults },
    { path: '/identification', component: IdentificationResults },
    { path: '/upload', component: UploadAndView },
    { path: '/login', component: Login },
    { path: '/download', component: DownloadAndShow },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router