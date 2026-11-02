<script setup>
import { TooltipProvider } from '@/components/ui/tooltip';
import { RouterView } from 'vue-router';

import { socket } from '@/api/websocket.js';
import { useAuthStore } from '@/stores/auth.js';
import { onMounted, onUnmounted } from 'vue';

const authStore = useAuthStore();

onMounted(async () => {
    socket.authTokenProvider = async () => {
        if (!authStore.isAuthenticated || !authStore.keycloak) {
            return null;
        }

        try {
            const refreshed = await authStore.keycloak.updateToken(30);
            if (refreshed) {
                authStore.token = authStore.keycloak.token;
            }

            return authStore.token;
        } catch (e) {
            console.error('Failed to refresh Keycloak token for WebSocket', e);
            return null;
        }
    };
    await socket.connect();
});

onUnmounted(async () => {
    await socket.disconnect();
});
</script>

<template>
    <TooltipProvider>
        <RouterView>

        </RouterView>
    </TooltipProvider>
</template>
