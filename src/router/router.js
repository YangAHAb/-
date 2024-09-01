import { createMemoryHistory, createRouter } from 'vue-router'

import UploadAndView from '../views/UploadAndView.vue';
import Log from '../views/Log.vue';
import IdentificationResults from '../views/IdentificationResults.vue';
import MaskResults from '../views/MaskResults.vue';
import DownloadAndShow from '../views/DownloadAndShow.vue';
import LoginNew from '../views/Login.vue';
import Main from '@/views/Main.vue';
import Login from '../views/Login.vue';

const routes = [
    { path: '/main', component: Main },
    { path: '/mask', component: MaskResults },
    { path: '/identification', component: IdentificationResults },
    { path: '/upload', component: UploadAndView },
    { path: '/log', component: Log },
    { path: '/download', component: DownloadAndShow },
    { path: '/login', component: Login }
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router