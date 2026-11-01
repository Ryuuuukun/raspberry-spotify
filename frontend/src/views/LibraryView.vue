<script setup>
import LibraryPlaylistContext from '@/components/subcomp/library/LibraryPlaylistContext.vue';
import LibrarySelectView from '@/components/subcomp/library/LibrarySelectView.vue';

import PlaylistSplashCard from '@/components/block/playlist/PlaylistSplashCard.vue';
import PlaylistShortCard from '@/components/block/playlist/PlaylistShortCard.vue';
import PlaylistCoverCard from '@/components/block/playlist/PlaylistCoverCard.vue';

import { ExpandIcon, Grid2X2Icon, LibraryIcon, ListIcon, Music2Icon, PlusIcon, SearchCheck, SearchIcon, TextAlignJustifyIcon, XIcon } from '@lucide/vue';
import { InputGroup, InputGroupAddon, InputGroupButton, InputGroupInput } from '@/components/ui/input-group';
import ScrollArea from '@/components/ui/scroll-area/ScrollArea.vue';
import { Button } from '@/components/ui/button';

import { useLibrary } from '@/composable/useLibrary.js';
import { computed, onMounted, ref } from 'vue';
import LibraryCreateContext from '@/components/subcomp/library/LibraryCreateContext.vue';

const library = useLibrary();

const props = defineProps({
    collapsed: {
        type: Boolean,
        required: true
    }
});
const emits = defineEmits(['expand']);

// searching
const searchModel = ref('');
const searchView = computed(() => searchModel.value.slice(0, 10) + (searchModel.value.length > 10 ? '...' : ''));

const isSearch = ref(false);
const searchRef = ref(null);

function enableSearch() {
    isSearch.value = true;
}

function focusSearch() {
    if (searchRef.value) {
        const inputEl = searchRef.value?.$el?.querySelector('input') || searchRef.value?.$el || searchRef.value;
        inputEl?.focus?.();
    }
}

async function disableSearch(event) {
    if (!event.currentTarget.contains(event.relatedTarget)) {
        isSearch.value = false;
    }
}

function clearSearch() {
    searchModel.value = '';
}

// sorting/view
const viewOptions = {
    sorts: [
        { label: 'Недавние' },
        { label: 'По алфавиту' },
        { label: 'Автор' }
    ],
    views: [
        { type: 'short', icon: TextAlignJustifyIcon, component: PlaylistShortCard },
        { type: 'list', icon: ListIcon, component: PlaylistSplashCard },
        { type: 'grid', icon: Grid2X2Icon, component: PlaylistCoverCard }
    ]
};
const viewModel = ref({
    order: 'asc',
    sort: viewOptions.sorts[0],
    view: viewOptions.views[1]
});

// list card
const libraryCard = computed(() => {
    if (props.collapsed) {
        return PlaylistCoverCard;
    } else {
        return viewModel.value.view.component;
    }
});

// mounted/unmounted
onMounted(async () => {
    await library.load();
});
</script>

<template>
    <div class="w-full h-full flex flex-col">
        <div v-if="collapsed" class="w-full h-fit py-4 flex flex-col gap-2 items-center">
            <Button variant="ghost" size="icon" class="rounded-full">
                <LibraryIcon class="size-5"/>
            </Button>
            <LibraryCreateContext>
                <Button variant="secondary" size="icon" class="rounded-full">
                    <PlusIcon class="size-5"/>
                </Button>
            </LibraryCreateContext>
        </div>
        <div v-else class="w-full h-fit space-y-2 px-4 py-4">
            <div class="flex gap-2 items-center">
                <Button variant="ghost" size="icon-lg" class="rounded-full">
                    <LibraryIcon class="size-5"/>
                </Button>
                <p class="w-full font-bold">Моя медиатека</p>
                <Button variant="ghost" size="icon" class="rounded-full" @click="$emit('expand')">
                    <ExpandIcon/>
                </Button>
            </div>
            <Transition
                mode="out-in"
                enter-active-class="transition duration-200 ease-out"
                enter-from-class="opacity-0 blur-sm scale-95"
                enter-to-class="opacity-100 blur-none scale-100"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="opacity-100 blur-none scale-100"
                leave-to-class="opacity-0 blur-sm scale-95"
                @after-enter="focusSearch"
            >
                <div v-if="!isSearch" class="flex gap-2">
                    <Button
                        :variant="searchView ? 'default' : 'secondary'"
                        :size="searchView ? 'default' : 'icon'"
                        class="rounded-full"
                        @click="enableSearch"
                    >
                        <SearchIcon/>
                        <p v-if="searchView">{{ searchView }}</p>
                    </Button>
                    <Button
                        v-if="searchView"
                        size="icon"
                        class="rounded-full"
                        @click="clearSearch"
                    >
                        <XIcon/>
                    </Button>
                    <LibraryCreateContext>
                        <Button
                            variant="secondary"
                            class="rounded-full"
                        >
                            <PlusIcon/>
                            <p>Создать</p>
                        </Button>
                    </LibraryCreateContext>
                    <LibrarySelectView
                        v-model="viewModel"
                        :options="viewOptions"
                    />
                </div>
                <div v-else
                    @keydown.esc="disableSearch($event); clearSearch();"
                    @keydown.enter="disableSearch"
                    @focusout="disableSearch"
                >
                    <InputGroup class="rounded-full">
                        <InputGroupAddon>
                            <SearchIcon/>
                        </InputGroupAddon>
                        <InputGroupInput
                            v-model="searchModel"
                            ref="searchRef"
                        />
                        <InputGroupButton
                            v-if="searchModel"
                            variant="link"
                            size="icon-sm"
                            class="rounded-full"
                            @click.stop="clearSearch"
                        >
                            <XIcon/>
                        </InputGroupButton>
                    </InputGroup>
                </div>
            </Transition>
        </div>
        <ScrollArea class="flex-1 px-2" @update="library.load">
            <div :class="{
                'grid grid-cols-[repeat(auto-fit,minmax(120px,1fr))]': !collapsed && viewModel.view.type === 'grid'
            }">
                <LibraryPlaylistContext
                    v-for="item in library.items.value"
                    :item="item"
                >
                    <component
                        :is="libraryCard"
                        :item="item"
                    />
                </LibraryPlaylistContext>
            </div>
        </ScrollArea>
    </div>
</template>
