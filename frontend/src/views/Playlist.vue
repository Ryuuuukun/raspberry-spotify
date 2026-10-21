<script setup>
import { EllipsisIcon, PenIcon, PlayIcon, PlusIcon, ShuffleIcon, TextAlignJustifyIcon, TimerIcon, UserIcon } from '@lucide/vue';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { DropdownMenu, DropdownMenuContent, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import { Button } from '@/components/ui/button';

import { ref } from 'vue';
import { ScrollAreaScrollbar } from 'reka-ui';

const playlist = ref({
    id: 0,
    title: 'Мой плейлист',
    type: 'public',
    owner: {
        nickname: 'Shouryuuko'
    },
    tracks: Array.from({ length: 100 }, (_, k) => ({ id: k, duration: k ** 2 }))
});


const SortType = [
    { label: 'Название', value: 'title' },
    { label: 'По дате добавления', value: 'date' },
];

const currentSortType = ref(SortType[0]);

</script>

<template>
    <ScrollArea class="@container w-full h-full">
        <!-- banner -->
        <div class="w-full flex items-end space-x-5 p-10">
            <div class="z-10">
                <Skeleton class="w-[20cqw] min-w-30 max-w-60 aspect-square"/>
            </div>
            <div class="z-10">
                <p class="text-muted-foreground text-sm">
                    {{
                        {
                            'public': 'Открытый плейлист',
                            'private': 'Приватный плейлист'
                        }[playlist?.type]
                    }}
                </p>
                <p class="font-black text-4xl sm:text-4xl md:text-6xl lg:text-6xl xl:text-8xl">Мой плейлист</p>
                <div class="flex items-center space-x-1 text-sm font-medium">
                    <Avatar class="size-6">
                        <AvatarFallback><UserIcon class="size-4"/></AvatarFallback>
                    </Avatar>
                    <Button variant="link" class="p-0">
                        <p class="font-bold">{{ playlist?.owner?.nickname }}</p>
                    </Button>
                    <p class="text-muted-foreground font-normal">&bull;</p>
                    <p class="text-muted-foreground font-normal">
                        {{ playlist?.tracks.length }} треков,
                        {{
                            ((tracks, time = tracks.reduce((s, e) => s + e.duration, 0)) => {
                                let hours = Math.floor(time / 3600);
                                let minutes = Math.floor((time % 3600) / 60);
                                let seconds = Math.floor(time % 60);

                                return (hours > 0 ? `${hours} ч. ` : '') + (minutes > 0 ? `${minutes} мин. ` : '') + `${seconds} сек.`
                            })(playlist.tracks)
                        }}
                    </p>
                </div>
            </div>
        </div>
        <!-- actions -->
        <div class="flex flex-col px-10 py-5 pt-0 space-y-5">
            <div class="flex items-center space-x-2">
                <Button variant="default" class="size-14 rounded-full">
                    <PlayIcon fill class="size-4"/>
                </Button>
                <Button variant="ghost" size="icon-lg" class="rounded-full">
                    <ShuffleIcon/>
                </Button>
                <Button variant="ghost" size="icon-lg" class="rounded-full">
                    <EllipsisIcon/>
                </Button>
            </div>
            <div class="flex space-x-2">
                <Button variant="secondary" class="rounded-full">
                    <PlusIcon/>
                    <p>Добавить</p>
                </Button>
                <Button variant="secondary" class="rounded-full">
                    <PenIcon/>
                    <p>Название и сведения</p>
                </Button>
                <div class="w-full"></div>
                <DropdownMenu>
                    <DropdownMenuTrigger>
                        <Button variant="ghost" class="text-muted-foreground rounded-full">
                            <p>{{ currentSortType.label }}</p>
                            <!-- <TextAlignJustifyIcon/> -->
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>

                    </DropdownMenuContent>
                </DropdownMenu>
            </div>
        </div>
        <!-- content -->
        <div class="px-10">
            <div v-for="n in 20" class="w-full h-10"></div>
        </div>
        <!-- footer -->
        <div class="w-full h-20">

        </div>
    </ScrollArea>
</template>
