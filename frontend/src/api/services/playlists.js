import backendClient from '@/api/axios.js';
import { ref } from "vue";

const API_PLAYLISTS_URL = '/api/playlists';

export function usePlaylists() {
    const loading = ref(false);
    const error = ref(null);

    async function execute(request) {
        loading.value = true;
        error.value = null;
        try {
            return await request();
        } catch (e) {
            error.value = e.response?.data || e.message;
            throw e;
        } finally {
            loading.value = false;
        }
    }

    async function create(playlistCreateRequest) {
        return execute(async () => {
            const response = await backendClient.post(API_PLAYLISTS_URL, playlistCreateRequest);
            return response.data;
        });
    }

    async function update(id, playlistUpdateRequest) {
        return execute(async () => {
            const response = await backendClient.patch(`${API_PLAYLISTS_URL}/${id}`, playlistUpdateRequest);
            return response.data;
        });
    }

    async function remove(id) {
        return execute(async () => {
            await backendClient.delete(`${API_PLAYLISTS_URL}/${id}`);
        });
    }

    async function get(id) {
        return execute(async () => {
            const response = await backendClient.get(`${API_PLAYLISTS_URL}/${id}`);
            return response.data;
        });
    }

    return { create, update, remove, get };
}
