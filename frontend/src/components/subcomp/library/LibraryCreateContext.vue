<script setup>
import { Button } from '@/components/ui/button';
import { DropdownMenu, DropdownMenuContent, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { Music2Icon, MusicIcon } from '@lucide/vue';
</script>

<template>
    <DropdownMenu>
        <DropdownMenuTrigger>
            <slot/>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start">
            <div class="flex gap-2 p-2 items-center rounded-sm hover:bg-muted/40">
                <div class="size-14 p-4 bg-muted rounded-full">
                    <MusicIcon class="size-6"/>
                </div>
                <div>
                    <p class="text-base font-bold">Композиция</p>
                    <p class="text-sm text-muted-foreground">Загрузить и опубликовать новую композицию.</p>
                </div>
            </div>
            <div class="flex gap-2 p-2 items-center rounded-sm hover:bg-muted/40">
                <div class="size-14 p-4 bg-muted rounded-full">
                    <Music2Icon class="size-6"/>
                </div>
                <div>
                    <p class="text-base font-bold">Композиция</p>
                    <p class="text-sm text-muted-foreground">Загрузить и опубликовать новую композицию.</p>
                </div>
            </div>
        </DropdownMenuContent>
    </DropdownMenu>
</template>
