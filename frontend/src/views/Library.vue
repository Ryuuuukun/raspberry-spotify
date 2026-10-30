<script setup>
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuLabel, DropdownMenuGroup, DropdownMenuItem, DropdownMenuShortcut, DropdownMenuRadioGroup, DropdownMenuRadioItem, DropdownMenuSeparator } from '@/components/ui/dropdown-menu';
import { Grid2X2Icon, GridIcon, ListIcon, Music2Icon, MusicIcon, PlusIcon, Search, SearchIcon, TextAlignJustifyIcon } from '@lucide/vue';
import { InputGroup, InputGroupAddon, InputGroupInput, InputGroupButton } from '@/components/ui/input-group';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { Button } from '@/components/ui/button';
import { Field } from '@/components/ui/field';

import { ShortLibrary, ListLibrary, GridLibrary } from '@/components/block/library-view';

import { usePlaylists } from '@/api/services/playlists.js';
import { useLibrary } from '@/composable/useLibrary.js';
import { onMounted, ref, nextTick } from 'vue';
import { Input } from '@/components/ui/input';

const playlists = usePlaylists();
const library = useLibrary();

const isSearching = ref(false);
const inputSearch = ref(null);

const LibrarySortTypes = [
    { label: 'Недавние' },
    { label: 'По дате' },
    { label: 'По алфавиту' },
    { label: 'Автор' }
];

const LibraryViewTypes = [
    {
        icon: TextAlignJustifyIcon,
        type: 'short',
        component: ShortLibrary
    },
    {
        icon: ListIcon,
        type: 'list',
        component: ListLibrary
    },
    {
        icon: Grid2X2Icon,
        type: 'grid',
        component: GridLibrary
    }
];

const libraryViewType = ref(LibraryViewTypes[1]);
const librarySortType = ref(LibrarySortTypes[0]);

function enableSearch() {
    isSearching.value = true;
}

function focusInput() {
    if (inputSearch.value) {
        const inputEl = inputSearch.value?.$el?.querySelector('input') || inputSearch.value?.$el || inputSearch.value;
        inputEl?.focus?.();
    }
}

async function disableSearch(event) {
    if (!event.currentTarget.contains(event.relatedTarget)) {
        isSearching.value = false;
    }
}

async function handleCreatePlaylist() {
    const playlistCreateRequest = {
        title: 'Новый плейлист'
    };

    try {
        await playlists.create(playlistCreateRequest);
        await library.load(true);
    } catch (e) {
        console.log(e);
    }
}
</script>

<template>
    <div class="w-full h-full">
        <div class="px-4">
            <Transition
                mode="out-in"
                enter-active-class="transition duration-200 ease-out"
                enter-from-class="opacity-0 blur-sm scale-95"
                enter-to-class="opacity-100 blur-none scale-100"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="opacity-100 blur-none scale-100"
                leave-to-class="opacity-0 blur-sm scale-95"
                @after-enter="focusInput"
            >
                <div v-if="!isSearching" class="flex space-x-2">
                    <Button variant="secondary" size="icon" class="rounded-full" @click="enableSearch">
                        <SearchIcon/>
                    </Button>
                    <DropdownMenu>
                        <DropdownMenuTrigger>
                            <Button variant="secondary" class="rounded-full">
                                <PlusIcon/>
                                <p>Создать</p>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent class="w-85" align="start">
                            <DropdownMenuItem class="group h-16 flex">
                                <div class="h-full flex items-center justify-center aspect-square rounded-full bg-muted-foreground/20">
                                    <Music2Icon/>
                                </div>
                                <div>
                                    <p class="font-bold">Композиция</p>
                                    <p class="text-muted-foreground">Загрузить новую композицию</p>
                                </div>
                            </DropdownMenuItem>
                            <DropdownMenuItem class="h-16 flex" @click="handleCreatePlaylist">
                                <div class="h-full flex items-center justify-center aspect-square rounded-full bg-muted-foreground/20">
                                    <MusicIcon/>
                                </div>
                                <div>
                                    <p class="font-bold">Плейлист</p>
                                    <p class="text-muted-foreground">Создать новый плейлист</p>
                                </div>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                    <DropdownMenu>
                        <DropdownMenuTrigger>
                            <Button variant="secondary" class="rounded-full">
                                <component :is="libraryViewType.icon"/>
                                <p>{{ librarySortType.label }}</p>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="start">
                            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">
                                Сортировка
                            </DropdownMenuLabel>
                            <DropdownMenuRadioGroup v-model="librarySortType">
                                <DropdownMenuRadioItem v-for="sort in LibrarySortTypes" :value="sort">
                                    <p>{{ sort.label }}</p>
                                </DropdownMenuRadioItem>
                            </DropdownMenuRadioGroup>
                            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">
                                Формат
                            </DropdownMenuLabel>
                            <ToggleGroup v-model="libraryViewType" type="single">
                                <ToggleGroupItem v-for="type in LibraryViewTypes" :value="type">
                                    <component :is="type.icon"/>
                                </ToggleGroupItem>
                            </ToggleGroup>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
                <div v-else="isSearching" @focusout="disableSearch">
                    <InputGroup>
                        <InputGroupAddon>
                            <SearchIcon/>
                        </InputGroupAddon>
                        <InputGroupInput ref="inputSearch"/>
                    </InputGroup>
                </div>
            </Transition>
        </div>
        <component :is="libraryViewType.component" class="w-full h-[calc(100%-6.25rem)] mt-2"/>
    </div>
</template>
