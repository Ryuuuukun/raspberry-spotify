<script setup>

import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { ScrollArea } from '@/components/ui/scroll-area';
import { UserIcon } from '@lucide/vue';

import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth.js';
import Separator from '@/components/ui/separator/Separator.vue';
import { Field, FieldLabel } from '@/components/ui/field/index.js';
import { Input } from '@/components/ui/input/index.js';

const router = useRouter();
const authStore = useAuthStore();

</script>

<template>
    <div class="w-screen h-screen flex gap-2 p-4 bg-black">
        <div class="w-70 shrink-0">
            <div class="w-full h-16 flex items-center gap-3 p-2">
                <Avatar class="size-12">
                    <AvatarImage/>
                    <AvatarFallback class="bg-background">
                        <UserIcon/>
                    </AvatarFallback>
                </Avatar>
                <div>
                    <p class="text-lg font-bold">{{ authStore.user?.username }}</p>
                    <p class="text-sm text-muted-foreground">{{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</p>
                </div>
            </div>
            <ScrollArea class="w-full h-[calc(100%-4rem)]">
                <div class="flex">

                </div>
            </ScrollArea>
        </div>
        <div class="w-full h-full rounded-lg bg-background">
            <div class="w-full h-16 flex items-center p-4 border-b">
                <p class="text-lg font-medium">Учетная запись</p>
            </div>
            <div>
                <div class="grid grid-cols-2 gap-5 p-5">
                    <div class="flex flex-col gap-2">
                        <Field>
                            <FieldLabel>Имя</FieldLabel>
                            <Input :placeholder="authStore.user?.firstName" readonly/>
                        </Field>
                        <Field>
                            <FieldLabel>Фамилия</FieldLabel>
                            <Input :placeholder="authStore.user?.lastName" readonly/>
                        </Field>
                    </div>
                    <div class="flex flex-col gap-2">
                        <Field>
                            <FieldLabel>Имя пользователя</FieldLabel>
                            <Input :placeholder="authStore.user?.username" readonly/>
                        </Field>
                        <Field>
                            <FieldLabel>Электронная почта</FieldLabel>
                            <Input :placeholder="authStore.user?.email" readonly/>
                        </Field>
                    </div>
                </div>
                <!-- {{ authStore.user }} -->
            </div>
        </div>
    </div>
</template>
