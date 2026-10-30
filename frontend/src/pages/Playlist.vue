<script setup>
import { EllipsisIcon, PenIcon, PlayIcon, PlusIcon, ShuffleIcon, TextAlignJustifyIcon, TimerIcon, UserIcon } from '@lucide/vue';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { DropdownMenu, DropdownMenuContent, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import { Button } from '@/components/ui/button';

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog';

import {
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/components/ui/form';

import { Input } from '@/components/ui/input';

import UpdatePlaylistDialog from '@/components/block/dialog/UpdatePlaylistDialog.vue';

import { onMounted, ref } from 'vue';
import { ScrollAreaScrollbar } from 'reka-ui';

import { usePlaylists } from '@/api/services/playlists.js';
import { useRoute } from 'vue-router';

const route = useRoute();
const playlists = usePlaylists();

const playlist = ref({});

onMounted(async () => {
    console.log(route.params.id);
    try {
        playlist.value = await playlists.get(route.params.id);
        console.log(playlist.value);
    } catch (e) {
        console.log(e);
    }
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
                <Skeleton class="w-[20cqw] min-w-30 max-w-56 aspect-square"/>
            </div>
            <div class="z-10">
                <p class="text-muted-foreground text-sm">
                    {{
                        playlist.visibility ? 'Открытый плейлист' : 'Закрытый плейлист'
                    }}
                </p>
                <p class="font-black text-4xl sm:text-4xl md:text-6xl lg:text-6xl xl:text-8xl">{{ playlist.title }}</p>
                <div class="flex items-center space-x-1 text-sm font-medium">
                    <Avatar class="size-6">
                        <AvatarFallback><UserIcon class="size-4"/></AvatarFallback>
                    </Avatar>
                    <Button variant="link" class="p-0">
                        <p class="font-bold">{{ playlist.owner?.displayName || playlist.owner?.username }}</p>
                    </Button>
                    <!-- <p class="text-muted-foreground font-normal">&bull;</p> -->
                    <!-- <p class="text-muted-foreground font-normal">
                        {{ playlist?.tracks.length }} треков,
                        {{
                            ((tracks, time = tracks.reduce((s, e) => s + e.duration, 0)) => {
                                let hours = Math.floor(time / 3600);
                                let minutes = Math.floor((time % 3600) / 60);
                                let seconds = Math.floor(time % 60);

                                return (hours > 0 ? `${hours} ч. ` : '') + (minutes > 0 ? `${minutes} мин. ` : '') + `${seconds} сек.`
                            })(playlist.tracks)
                        }}
                    </p> -->
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
                <UpdatePlaylistDialog :item="playlist">
                    <Button variant="secondary" class="rounded-full">
                        <PenIcon/>
                        <p>Название и сведения</p>
                    </Button>
                </UpdatePlaylistDialog>
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
