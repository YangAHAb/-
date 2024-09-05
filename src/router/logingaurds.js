import { store } from "@/main";
import router from "./router";

router.beforeEach((to, from) => {
    if (to.path !== '/login' && !store.getUsername) {
        return '/login';
    }
})
//防止未登录跳转