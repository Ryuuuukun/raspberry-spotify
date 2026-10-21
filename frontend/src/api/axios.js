import { useAuthStore } from '@/stores/auth';
import axios from 'axios';

const backendClient = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000
});

backendClient.interceptors.request.use(
    async config => {
        const authStore = useAuthStore();
        if (authStore.isAuthenticated && authStore.keycloak) {
            try {
                const token = await authStore.keycloak.updateToken(30);
                if (token) {
                    authStore.token = authStore.keycloak.token;
                }

                config.headers.Authorization = `Bearer ${authStore.token}`;
            } catch (error) {
                authStore.logout();
            }
        }

        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

backendClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            useAuthStore().logout();
        }

        return Promise.reject(error);
    }
);

export default backendClient;
