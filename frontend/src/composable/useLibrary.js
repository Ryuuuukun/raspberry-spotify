import backendClient from '@/api/axios.js'
import { ref } from "vue";

const items = ref([]);

export function useLibrary() {
    const loading = ref(false);

    const page = ref(0);
    const size = ref(20);
    const last = ref(false);

    async function load(reset = false) {
        if (reset) {
            page.value = 0;
            last.value = false;
        }

        if (last.value || loading.value) return;

        loading.value = true;

        try {
            const response = await backendClient.get('/api/playlists/owner', {
                params: {
                    page: page.value,
                    size: size.value
                }
            });

            const { content, page: _page } = response.data;
            items.value = reset ? content : [...items.value, ...content];
            last.value = _page.totalPages === page.value;

            if (!last.value) ++page.value;
        } catch (e) {
            throw e;
        } finally {
            loading.value = false;
        }
    }

    async function infinity(start, end) {
        if (end >= items.value.length - 2) {
            try {
                await load();
            } catch (e) {
                throw e;
            }
        }
    }

    return { items, loading, last, load, infinity };
}
