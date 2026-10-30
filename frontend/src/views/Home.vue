<script setup>
import {
    BellIcon, LayoutGridIcon,
    LibraryIcon,
    LogOutIcon, Maximize2Icon,
    Minimize2Icon,
    MinusIcon, Music2Icon, Music3Icon, MusicIcon, PlayIcon,
    PlusIcon, RepeatIcon,
    SearchIcon, SettingsIcon,
    ShirtIcon, ShuffleIcon,
    SkipBackIcon, SkipForwardIcon,
    UserIcon, UserRoundIcon, UsersIcon
} from '@lucide/vue';

import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuLabel, DropdownMenuGroup, DropdownMenuItem, DropdownMenuShortcut } from '@/components/ui/dropdown-menu';
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable';
import { InputGroup, InputGroupAddon, InputGroupInput } from '@/components/ui/input-group'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import { Slider } from '@/components/ui/slider';
import { Toggle } from '@/components/ui/toggle';
import { Button } from '@/components/ui/button';

import Library from './Library.vue';

import { useAuthStore } from '../stores/auth.js';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import { useMounted } from '@vueuse/core';
import backendClient from '@/api/axios.js';
import { ContextMenu, ContextMenuContent, ContextMenuTrigger } from '@/components/ui/context-menu/index.js';

import { usePlaylists } from '@/api/services/playlists.js';
import { useLibrary } from '@/composable/useLibrary.js';

const router = useRouter();
const authStore = useAuthStore();
const playlists = usePlaylists();
const library = useLibrary();

const isLibraryCollapsed = ref(false);
const expandedLibrary = ref(false);

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
    <div class="flex flex-col w-full h-screen p-2 bg-black">
        <!-- header -->
        <div class="grid grid-cols-3 min-h-15 mb-2">
            <div class="flex items-center">
                <div class="w-20 pl-4 pr-4">
                    <DropdownMenu>
                        <DropdownMenuTrigger>
                            <Avatar class="size-12">
                                <AvatarImage src="1https://i.pinimg.com/736x/28/49/c3/2849c37ce9d1dfa3984057bb6d948562.jpg"></AvatarImage>
                                <AvatarFallback class="bg-background">
                                    <UserIcon/>
                                </AvatarFallback>
                            </Avatar>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent class="w-50" align="start">
                            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Основное</DropdownMenuLabel>
                            <DropdownMenuGroup>
                                <RouterLink to="/settings">
                                    <DropdownMenuItem><UserIcon/>Аккаунт</DropdownMenuItem>
                                </RouterLink>
                                <RouterLink to="/profile">
                                    <DropdownMenuItem><ShirtIcon/>Профиль</DropdownMenuItem>
                                </RouterLink>
                                <DropdownMenuItem><SettingsIcon/>Настройки</DropdownMenuItem>
                            </DropdownMenuGroup>
                            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Профиль</DropdownMenuLabel>
                            <DropdownMenuGroup>
                                <DropdownMenuItem variant="destructive" @click="authStore.logout()"><LogOutIcon/>Выйти</DropdownMenuItem>
                            </DropdownMenuGroup>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
                <div class="w-3"></div>
                <div class="flex gap-1">
                    <Button variant="ghost" class="w-10 h-10">
                        <BellIcon/>
                    </Button>
                    <Button variant="ghost" class="w-10 h-10 ">
                        <UsersIcon/>
                    </Button>
                </div>
            </div>
        </div>
        <!-- content -->
        <ResizablePanelGroup direction="horizontal" class="gap-1">
            <ResizablePanel
                :default-size="30"
                :min-size="25"
                :max-size="40"
                :collapsed-size="6"
                collapsible
                class="rounded-lg bg-background"
                :class="{ 'min-w-20': isLibraryCollapsed, 'max-w-20': isLibraryCollapsed }"
                @collapse="isLibraryCollapsed = true"
                @expand="isLibraryCollapsed = false"
            >
                <!-- Library -->
                <div v-if="isLibraryCollapsed" class="w-full h-full">
                    <ScrollArea class="w-full flex-1 min-h-0">
                        <div class="w-20 h-16 px-5 flex items-center">
                            <Button
                                variant="ghost"
                                size="icon-lg"
                                class="rounded-full pl-0.5"
                            >
                                <LibraryIcon class="size-6"/>
                            </Button>
                        </div>
                        <div class="flex flex-col gap-2 p-3">
                            <Skeleton
                                v-for="n in 14"
                                class="aspect-square w-full"
                            />
                        </div>
                    </ScrollArea>
                </div>
                <div v-else class="w-full h-full">
                    <div class="w-full h-16 px-4 flex items-center justify-between">
                        <div class="space-x-2 flex items-center">
                            <Button
                                variant="ghost"
                                size="icon-lg"
                                class="rounded-full pl-0.5"
                            >
                                <LibraryIcon class="size-6"/>
                            </Button>
                            <p class="font-bold">Моя медиатека</p>
                        </div>
                        <Button
                            variant="ghost"
                            size="icon"
                            class="rounded-full"
                            @click="expandedLibrary = !expandedLibrary"
                        >
                            <Maximize2Icon v-if="!expandedLibrary"/>
                            <Minimize2Icon v-else/>
                        </Button>
                    </div>
                    <Library :expanded="expandedLibrary"/>
                </div>
            </ResizablePanel>
            <ResizableHandle class="opacity-0"></ResizableHandle>
            <ResizablePanel v-if="!expandedLibrary" class="rounded-lg bg-background">
                <RouterView/>
            </ResizablePanel>
            <!-- <ResizableHandle class="opacity-0"></ResizableHandle> -->
            <!-- <ResizablePanel
                :default-size="0"
                :max-size="30"
                class="rounded-lg bg-background"
            >

            </ResizablePanel> -->
        </ResizablePanelGroup>
        <!-- player -->
        <div class="w-full min-h-20 h-20 grid grid-cols-3">
            <!-- composition -->
            <div class="flex items-center gap-2 p-3">
                <Skeleton class="aspect-square h-full"/>
                <div class="flex flex-col justify-center gap-2">
                    <Skeleton class="w-25 h-3"/>
                    <Skeleton class="w-20 h-3"/>
                </div>
                <Button variant="ghost" size="icon">
                    <PlusIcon/>
                </Button>
            </div>
            <!-- controller -->
            <div class="flex flex-col justify-center gap-3">
                <div class="flex justify-center gap-3">
                    <Button variant="ghost" size="icon">
                        <ShuffleIcon/>
                    </Button>
                    <Button variant="ghost" size="icon">
                        <SkipBackIcon/>
                    </Button>
                    <Button size="icon" class="rounded-full">
                        <PlayIcon fill/>
                    </Button>
                    <Button variant="ghost" size="icon">
                        <SkipForwardIcon/>
                    </Button>
                    <Button variant="ghost" size="icon">
                        <RepeatIcon/>
                    </Button>
                </div>
                <div>
                    <Slider/>
                </div>
            </div>
        </div>
    </div>
</template>
