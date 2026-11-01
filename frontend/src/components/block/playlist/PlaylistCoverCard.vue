<script setup>
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';
import { Music2Icon } from '@lucide/vue';
import { TooltipArrow, TooltipPortal } from 'reka-ui';

const props = defineProps({
    item: {
        type: Object,
        required: true
    }
});
</script>

<template>
    <Tooltip :delay-duration="100">
        <TooltipTrigger as-child>
            <div class="w-full aspect-square p-2 rounded-md hover:bg-accent/50 cursor-pointer">
                <div class="w-full h-full flex items-center justify-center rounded-sm bg-muted">
                    <Music2Icon class="size-4"/>
                </div>
            </div>
        </TooltipTrigger>
        <TooltipContent side="right" :side-offset="8" class="bg-muted/50 backdrop-blur-2xl shadow-2xl">
            <p class="font-semibold text-base text-primary">{{ item.title }}</p>
            <p class="font-normal text-base text-muted-foreground">{{ item.owner?.displayName || item.owner?.username }}</p>
        </TooltipContent>
    </Tooltip>
</template>
