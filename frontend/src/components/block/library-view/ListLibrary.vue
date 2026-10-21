<script setup>
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuTrigger } from '@/components/ui/context-menu';
import { DropdownMenu } from '@/components/ui/dropdown-menu';
import { ScrollArea } from '@/components/ui/scroll-area';
import { ShareIcon, TrashIcon } from '@lucide/vue';
import { ref } from 'vue';

import { RecycleScroller } from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';

const props = defineProps({
    data: {
        type: Array,
        default: [
            {
                id: 1,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 2,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 3,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 4,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 5,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 6,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 7,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 8,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 9,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 10,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 11,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 12,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 13,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            },
            {
                id: 14,
                title: 'Любимые треки',
                type: 'Плейлист',
                cover: 'https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84587ecba4a27774b2f6f07174',
                author: 'Shouryuuko'
            }
        ]
    }
});
</script>

<template>
    <RecycleScroller
        class="h-100 pl-2 pb-2 scrollbar scrollbar-track-transparent scrollbar-thumb-accent"
        :items="props.data"
        :item-size="72"
        key-field="id"
        v-slot="{ item }"
    >
        <ContextMenu>
            <ContextMenuTrigger>
                <RouterLink to="/playlist">
                    <div class="w-full h-18 flex space-x-2 items-center p-2 rounded-lg hover:bg-muted">
                        <img :src="item.cover" class="h-full aspect-square rounded-sm"/>
                        <div>
                            <p class="font-medium text-primary">{{ item.title }}</p>
                            <p class="font-normal text-sm text-muted-foreground">{{ item.type }} &bull; {{ item.author }}</p>
                        </div>
                    </div>
                </RouterLink>
            </ContextMenuTrigger>
            <ContextMenuContent class="w-60">
                <ContextMenuItem>
                    <ShareIcon/>
                    Поделится
                </ContextMenuItem>
                <ContextMenuItem>
                    <TrashIcon/>
                    Удалить
                </ContextMenuItem>
            </ContextMenuContent>
        </ContextMenu>
    </RecycleScroller>
</template>
