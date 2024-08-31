import { createMemoryHistory, createRouter } from 'vue-router'

import UploadAndView from './views/UploadAndView.vue';
import Log from './views/Log.vue';
import IdentificationResults from './views/IdentificationResults.vue';
import MaskResults from './views/MaskResults.vue';
import DownloadAndShow from './views/DownloadAndShow.vue';
import Main from './views/Main.vue';

const routes = [
    { path: '/main', component: Main },
    { path: '/mask', component: MaskResults },
    { path: '/identification', component: IdentificationResults },
    { path: '/upload', component: UploadAndView },
    { path: '/log', component: Log },
    { path: '/download', component: DownloadAndShow },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router