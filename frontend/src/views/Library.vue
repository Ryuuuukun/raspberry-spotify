<script setup>
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuLabel, DropdownMenuGroup, DropdownMenuItem, DropdownMenuShortcut, DropdownMenuRadioGroup, DropdownMenuRadioItem, DropdownMenuSeparator } from '@/components/ui/dropdown-menu';
import { InputGroup, InputGroupAddon, InputGroupInput, InputGroupButton } from '@/components/ui/input-group';
import { Button } from '@/components/ui/button';
import { Grid2X2Icon, GridIcon, ListIcon, Search, TextAlignJustifyIcon } from '@lucide/vue';
import { ref } from 'vue';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { Field } from '@/components/ui/field';

import { ListLibrary, GridLibrary } from '@/components/block/library-view';

const SortTypes = [
    { label: 'Недавние' },
    { label: 'По дате' },
    { label: 'По алфавиту' },
    { label: 'Автор' }
];
const OrderSort = [
    { label: 'По возрастанию', value: 1 },
    { label: 'По убыванию', value: -1 }
];

const sort = ref({ order: OrderSort[0], type: SortTypes[0] });

const sortType = ref(SortTypes[0]);
const viewType = ref('list');


const LibraryViewTypes = [
    { icon: ListIcon, type: 'list', component: ListLibrary },
    { icon: Grid2X2Icon, type: 'grid', component: GridLibrary }
];
const libraryViewType = ref(LibraryViewTypes[0]);
</script>

<template>
    <div class="w-full h-full">
        <div class="h-11 p-1 pl-4 pr-4">
            <Field orientation="horizontal">
                <InputGroup>
                    <InputGroupAddon>
                        <Search/>
                    </InputGroupAddon>
                    <InputGroupInput placeholder="Поиск..."/>
                </InputGroup>
                <DropdownMenu>
                    <DropdownMenuTrigger>
                        <Button variant="ghost">
                            <p>{{ sort.type.label }}</p>
                            <TextAlignJustifyIcon v-if="viewType === 'short-list'"/>
                            <ListIcon v-else-if="viewType === 'list'"/>
                            <Grid2X2Icon v-else-if="viewType === 'grid'"/>
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                        <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Сортировка</DropdownMenuLabel>
                        <ToggleGroup v-for="type in SortTypes" v-model="sort.type" type="single" class="w-full flex flex-col">
                            <ToggleGroupItem :value="type" class="w-full justify-start">
                                <p class="font-normal text-left">{{ type.label }}</p>
                            </ToggleGroupItem>
                        </ToggleGroup>
                        <DropdownMenuSeparator/>
                        <DropdownMenuLabel class="text-xs font-medium text-muted-foreground">Формат</DropdownMenuLabel>
                        <ToggleGroup v-model="libraryViewType" type="single">
                            <ToggleGroupItem v-for="type in LibraryViewTypes" :value="type">
                                <component :is="type.icon"/>
                            </ToggleGroupItem>
                        </ToggleGroup>
                    </DropdownMenuContent>
                </DropdownMenu>
            </Field>
        </div>
        <component
            :is="libraryViewType.component"
            class="w-full h-[calc(100%-2.75rem-4rem)]"
        />
    </div>
</template>
