import backendClient from '@/api/axios.js';
import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useLibraryStore = defineStore('library', () => {
    const pages = ref({});
    const items = computed(() => Object.values(pages.value).flat())

    const pageable = ref({
        page: 0,
        size: 10,
        total: 0,
        sort: {
            field: 'title',
            order: 'asc'
        }
    });
    const state = ref({
        loading: false
    });

    function clearPages() {
        pageable.value.page = 0;
        pages.value = {};
    }

    async function loadPage(page = pageable.value.page) {
        state.value.loading = true;
        try {
            const response = await backendClient.get('/api/playlists/owner', {
                params: {
                    page,
                    size: pageable.value.size,
                    sort: `${pageable.value.sort.field},${pageable.value.sort.order}`
                }
            });

            const { content, page: _page } = response.data;
            pages.value[page] = content;
            pageable.value.total = _page.totalPages;
        } catch (e) {
            throw e;
        } finally {
            state.value.loading = false;
        }
    }

    async function nextPage() {
        if (pageable.value.page < pageable.value.total) {
            await loadPage(++pageable.value.page);
        }
    }

    async function changeSort(field, order) {
        pageable.value.sort.field = field;
        pageable.value.sort.order = order;

        clearPages();
        await loadPage();
    }

    async function changeSize(size) {
        pageable.value.size = size;
        pageable.value.value = 0;

        clearPages();
        await loadPage();
    }

    return {
        pages, items, pageable, state,
        clearPages, loadPage, nextPage, changeSort, changeSize
    };
});
