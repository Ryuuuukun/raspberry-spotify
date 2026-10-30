<script setup>
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuSeparator, ContextMenuTrigger } from '@/components/ui/context-menu';
import { GitPullRequestCreateArrowIcon, LockIcon, Music2Icon, PenIcon, ShareIcon, TrashIcon } from '@lucide/vue';
import PlaylistContext from '../context/PlaylistContext.vue';

import { RecycleScroller } from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';

import { useLibrary } from '@/composable/useLibrary.js';
import { onMounted } from 'vue';

const library = useLibrary();

onMounted(async () => {
    await library.load(true);
});
</script>

<template>
    <RecycleScroller
        class="px-2 pb-2 scrollbar scrollbar-track-transparent scrollbar-thumb-accent"
        :items="library.items.value"
        :item-size="32"
        key-field="id"
        v-slot="{ item }"
        :emit-update="true"
        @update="library.infinity"
    >
        <PlaylistContext :item="item">
            <RouterLink :to="{ name: 'Playlist', params: { id: item.id } }">
                <div class="group w-full h-8 px-2 space-x-2 flex items-center rounded-lg hover:bg-muted">
                    <p class="font-bold">{{ item.title }}</p>
                    <p class="text-muted-foreground">&bull;</p>
                    <p class="text-muted-foreground">{{ item.owner.displayName || item.owner.username }}</p>
                </div>
            </RouterLink>
        </PlaylistContext>
    </RecycleScroller>
</template>
