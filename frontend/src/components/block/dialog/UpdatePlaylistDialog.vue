<script setup>
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

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

import { ref, watch } from 'vue';
import { useForm } from 'vee-validate';
import { toTypedSchema } from '@vee-validate/zod';

import * as z from 'zod';

import { usePlaylists } from '@/api/services/playlists.js';
import { useLibrary } from '@/composable/useLibrary.js';

const isOpen = ref(false);

const library = useLibrary();
const playlists = usePlaylists();
const props = defineProps({
    item: {
        type: Object
    }
});

const form = useForm({
    validationSchema: toTypedSchema(z.object({
        title: z.string().min(0, 'Название плейлиста не может быть пустым').max(255),
        description: z.string().max(255).optional().or(z.literal(''))
    })),
    defaultValues: {
        title: props.item?.title || '',
        description: props.item?.description || ''
    }
});

watch(isOpen, (value) => {
    if (value) {
        form.setValues({
            title: props.item?.title || '',
            description: props.item?.description || ''
        });
    }
});

watch(() => props.item, (newitem) => {
    if (newitem) {
        form.setValues({
            title: newitem.title || '',
            description: newitem.description || ''
        });
    }
}, { immediate: true });

const onSumbit = form.handleSubmit(async (values) => {
    try {
        await playlists.update(props.item?.id, values);
        await library.load(true);
        isOpen.value = false;
    } catch (e) {
        console.error(e);
    }
});
</script>

<template>
    <Dialog v-model:open="isOpen">
        <DialogTrigger as-child>
            <slot/>
        </DialogTrigger>
        <DialogContent>
            <DialogHeader>
                <DialogTitle>
                    Изменение сведений
                </DialogTitle>
            </DialogHeader>
            <form @submit="onSumbit" class="flex flex-col space-y-5">
                <FormField name="title" v-slot="{ componentField }">
                    <FormItem>
                        <FormLabel>Название</FormLabel>
                        <FormControl>
                            <Input v-bind="componentField" />
                        </FormControl>
                        <!-- <FormMessage/> -->
                    </FormItem>
                </FormField>
                <FormField name="description" v-slot="{ componentField }">
                    <FormItem>
                        <FormLabel>Описание</FormLabel>
                        <FormControl>
                            <Input v-bind="componentField"/>
                        </FormControl>
                        <!-- <FormMessage /> -->
                    </FormItem>
                </FormField>
                <DialogFooter>
                    <Button type="submit">
                        Сохранить изменения
                    </Button>
                </DialogFooter>
            </form>
        </DialogContent>
    </Dialog>
</template>
