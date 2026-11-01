<script setup>
const props = defineProps({
    item: {
        type: Object,
        required: true
    }
});
</script>

<template>
    <div class="px-2 py-1 flex items-center gap-2 rounded-lg hover:bg-muted/40">
        <p class="font-medium ">{{ item.title }}</p>
        <p class="font-normal text-muted-foreground">&bull;</p>
        <p class="font-normal text-muted-foreground">{{ item.owner?.displayName || item.owner?.username }}</p>
    </div>
</template>
