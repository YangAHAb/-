import { createMemoryHistory, createRouter } from 'vue-router'

import UploadAndView from '../views/UploadAndView.vue';
import Log from '../views/Log.vue';
import IdentificationResults from '../views/IdentificationResults.vue';
import MaskResults from '../views/MaskResults.vue';
import DownloadAndShow from '../views/DownloadAndShow.vue';
import Main from '@/views/Main.vue';
import Login from '../views/Login.vue';

const routes = [
    { path: '/main', component: Main, meta: { showTasklist: true } },
    { path: '/mask', component: MaskResults, meta: { showTasklist: true } },
    { path: '/identification', component: IdentificationResults, meta: { showTasklist: true } },
    { path: '/upload', component: UploadAndView , meta: { showTasklist: true }},
    { path: '/log', component: Log, meta: { showTasklist: true } },
    { path: '/download', component: DownloadAndShow, meta: { showTasklist: true } },
    { path: '/login', component: Login, meta: { showTasklist: false } }
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router