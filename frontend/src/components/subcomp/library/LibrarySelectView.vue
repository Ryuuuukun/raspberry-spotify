<script setup>
import { DropdownMenu, DropdownMenuContent, DropdownMenuLabel, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { ChevronDown, ChevronUp } from '@lucide/vue';
import { Button } from '@/components/ui/button';

const props = defineProps({
    options: {
        type: Object,
        required: true
    }
});
const model = defineModel({
    type: Object,
    required: true
});

function handleSelectSort(item) {
    if (model.value.sort.label !== item.label) {
        model.value.sort = item;
    } else {
        model.value.order = model.value.order === 'asc' ? 'desc' : 'asc';
    }
}
</script>

<template>
    <DropdownMenu>
        <DropdownMenuTrigger>
            <Button
                variant="secondary"
                class="rounded-full"
            >
                <component :is="model.view.icon"/>
                <p>{{ model.sort.label }}</p>
            </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start">
            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Сортировка</DropdownMenuLabel>
            <div class="flex flex-col gap-1">
                <Button v-for="item in options.sorts"
                    :variant="model.sort.label === item.label ? 'secondary' : 'ghost'"
                    size="sm"
                    class="rounded-full"
                    @click="handleSelectSort(item)"
                >
                    <p class="font-normal flex-1 text-left">{{ item.label }}</p>
                    <div v-if="model.sort.label == item.label">
                        <ChevronUp v-if="model.order === 'asc'"/>
                        <ChevronDown v-else/>
                    </div>
                </Button>
            </div>
            <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Вид</DropdownMenuLabel>
            <ToggleGroup v-model="model.view" type="single">
                <ToggleGroupItem v-for="item in options.views" :value="item">
                    <component :is="item.icon"/>
                </ToggleGroupItem>
            </ToggleGroup>
        </DropdownMenuContent>
    </DropdownMenu>
</template>
