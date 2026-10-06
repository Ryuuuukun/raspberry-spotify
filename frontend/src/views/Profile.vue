<script setup>
    import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
    import { ScrollArea } from '@/components/ui/scroll-area';
    import { Skeleton } from '@/components/ui/skeleton';
    import { DotIcon, UserIcon } from '@lucide/vue';
</script>

<template>
    <div class="w-full h-full flex flex-col">
        <ScrollArea class="w-full h-full">
            <!-- banner -->
            <div class="w-full h-60 flex gap-5 p-5 bg-accent">
                <Avatar class="w-50 h-50">
                    <AvatarImage/>
                    <AvatarFallback class="bg-background">
                        <UserIcon class="w-1/3 h-1/3"/>
                    </AvatarFallback>
                </Avatar>
                <div class="w-full h-full min-w-50">

                </div>
            </div>
        </ScrollArea>
    </div>
</template>
