<script setup>
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuSeparator, ContextMenuTrigger } from '@/components/ui/context-menu';
import { GitPullRequestCreateArrowIcon, LockIcon, Music2Icon, PenIcon, ShareIcon, TrashIcon } from '@lucide/vue';

import { useLibrary } from '@/composable/useLibrary.js';
import { usePlaylists } from '@/api/services/playlists.js';

const playlists = usePlaylists();
const library = useLibrary();

const props = defineProps({
    item: {
        type: Object
    }
});

async function handleDeletePlaylist() {
    try {
        await playlists.remove(props.item.id);
        await library.load(true);
    } catch (e) {
        console.log(e);
    }
}
</script>

<template>
    <ContextMenu>
        <ContextMenuTrigger>
            <RouterLink to="/playlist">
                <slot/>
            </RouterLink>
        </ContextMenuTrigger>
        <ContextMenuContent class="w-60">
            <ContextMenuItem>
                <GitPullRequestCreateArrowIcon/>
                Добавить в очередь
            </ContextMenuItem>
            <ContextMenuSeparator/>
            <ContextMenuItem>
                <ShareIcon/>
                Поделится
            </ContextMenuItem>
            <ContextMenuItem>
                <LockIcon/>
                Закрыть доступ
            </ContextMenuItem>
            <ContextMenuSeparator/>
            <ContextMenuItem>
                <PenIcon/>
                Изменить сведения
            </ContextMenuItem>
            <ContextMenuItem variant="destructive" @click="handleDeletePlaylist">
                <TrashIcon/>
                Удалить
            </ContextMenuItem>
        </ContextMenuContent>
    </ContextMenu>
</template>
