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
        class=" px-2 pb-4 scrollbar scrollbar-track-transparent scrollbar-thumb-accent custom-scroll"
        :items="library.items.value"
        :item-size="72"
        key-field="id"
        v-slot="{ item }"
        :emit-update="true"
        @update="library.infinity"
    >
        <PlaylistContext :item="item">
            <RouterLink :to="{ name: 'Playlist', params: { id: item.id } }">
                <div class="group w-full h-18 flex space-x-2 items-center p-2 rounded-lg hover:bg-muted/40">
                    <img v-if="item.cover" :src="item.cover" class="h-full aspect-square rounded-sm"/>
                    <div v-else class="h-full flex items-center justify-center aspect-square rounded-sm bg-muted">
                            <Music2Icon class="size-4"/>
                        </div>
                    <div>
                        <p class="font-medium text-primary">
                            {{ item.title }}
                        </p>
                        <p class="font-normal text-sm text-muted-foreground">
                            {{ item.owner.displayName || item.owner.username }}
                        </p>
                    </div>
                </div>
            </RouterLink>
        </PlaylistContext>
    </RecycleScroller>
</template>

<style>
.custom-scroll {
  scrollbar-width: thin;
  scrollbar-color: transparent transparent;
}

.custom-scroll::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.custom-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scroll::-webkit-scrollbar-thumb {
  background-color: transparent;
  border-radius: 4px;
  transition: background-color 200ms ease-in-out;
}

.custom-scroll:hover {
  scrollbar-color: rgba(255, 255, 255, 0.1) transparent;
}

.custom-scroll:hover::-webkit-scrollbar-track {
  background: rgba(226, 232, 240, 0.4);
}

.custom-scroll:hover::-webkit-scrollbar-thumb {
  background-color: rgba(100, 116, 139, 0.6);
}

.custom-scroll:hover::-webkit-scrollbar-thumb:hover {
  background-color: rgba(71, 85, 105, 0.85);
}
</style>
