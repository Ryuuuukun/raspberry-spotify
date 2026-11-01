<script setup>
import { Music2Icon } from '@lucide/vue';

const props = defineProps({
    item: {
        type: Object,
        required: true
    }
});
</script>

<template>
    <div class="group w-full h-18 flex space-x-2 items-center p-2 rounded-lg hover:bg-muted/40">
        <div class="h-full flex items-center justify-center aspect-square rounded-sm bg-muted">
            <Music2Icon class="size-4"/>
        </div>
        <div>
            <p class="font-medium text-primary">{{ item.title }}</p>
            <p class="font-normal text-sm text-muted-foreground">{{ item.owner.displayName || item.owner.username }}</p>
        </div>
    </div>
</template>
