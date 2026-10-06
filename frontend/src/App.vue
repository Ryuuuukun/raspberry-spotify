<script setup>
import { TooltipProvider } from '@/components/ui/tooltip';
import { RouterView } from 'vue-router';


</script>

<template>
    <TooltipProvider>
        <RouterView>

        </RouterView>
    </TooltipProvider>
</template>
