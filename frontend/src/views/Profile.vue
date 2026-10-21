<script setup>

import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import { DotIcon, PenIcon, Settings2Icon, SettingsIcon, UserIcon } from '@lucide/vue';

import { useAuthStore } from '../stores/auth.js';

import { RecycleScroller } from 'vue-virtual-scroller';
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';
import { Button } from '@/components/ui/button/index.js';

const authStore = useAuthStore();
</script>

<template>
    <ScrollArea class="w-full h-full">
        <!-- banner -->
        <div class="@container w-full flex space-x-5 items-end p-5">
            <Avatar class="w-[20cqw] h-[20cqw] min-w-30 min-h-30 max-w-50 max-h-50">
                <AvatarImage/>
                <AvatarFallback>
                    <UserIcon class="size-[25%]"/>
                </AvatarFallback>
            </Avatar>
            <div>
                <p class="font-black text-4xl sm:text-4xl md:text-6xl lg:text-6xl xl:text-8xl">{{ authStore.user?.username }}</p>
                <div class="flex items-center space-x-1 text-muted-foreground py-2">
                    <p class="">7 открытых плейлистов</p>
                    <p>&bull;</p>
                    <Button variant="link" class="p-0">
                        4 подписчика
                    </Button>
                    <p>&bull;</p>
                    <Button variant="link" class="p-0">
                        36 подписок
                    </Button>
                </div>
            </div>
        </div>
        <div class="flex space-x-2 p-4">
            <Button variant="secondary" class="rounded-full">
                <SettingsIcon/>
                Настройки акканута
            </Button>
            <Button variant="secondary" class="rounded-full">
                <PenIcon/>
                Настроить витрину
            </Button>
        </div>
    </ScrollArea>

    <!-- <div class="w-full h-full flex flex-col">
        <ScrollArea class="w-full h-full">
            <div class="w-full h-60 flex gap-5 p-5 bg-accent">
                <Avatar class="w-50 h-50">
                    <AvatarImage/>
                    <AvatarFallback class="bg-background">
                        <UserIcon class="w-1/3 h-1/3"/>
                    </AvatarFallback>
                </Avatar>
                <div class="w-full h-full min-w-50">
                    {{ authStore.user?.username }}
                </div>
            </div>
        </ScrollArea>
    </div> -->
</template>
