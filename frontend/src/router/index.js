import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../stores/auth";

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: {
            requiresAuth: true
        },
        children: [
            {
                path: '/profile',
                name: 'profile',
                component: () => import('@/views/Profile.vue'),
                meta: {
                    requiresAuth: true
                }
            },
            {
                path: '/playlist',
                name: 'Playlist',
                component: () => import('@/views/Playlist.vue'),
                meta: {
                    requiresAuth: true
                }
            }
        ]
    },
    {
        path: '/settings',
        name: 'Settings',
        component: () => import('@/views/Settings.vue'),
        meta: {
            requiresAuth: true
        }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();

    if (to.meta.requiresAuth) {
        if (!authStore.isAuthenticated) {
            authStore.login({ redirectUri: window.location.origin + to.fullPath });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
