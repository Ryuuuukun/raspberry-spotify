<script setup>
import { DynamicScroller, DynamicScrollerItem } from 'vue-virtual-scroller';
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
    <DynamicScroller
        :items="props.data"
        key-field="id"
        class="w-full h-full"
    >
        <template v-slot="{ item, index, active }">

        </template>
    </DynamicScroller>
</template>
