<script setup lang="ts">
import {
  ref,
  computed,
  onMounted,
  onBeforeUnmount,
  nextTick,
} from 'vue'

interface Props {
  threshold?: number
  hideOnLeave?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  threshold: 100,
  hideOnLeave: true,
})

const emit = defineEmits<{
  scroll: [event: Event]
  update: []
}>()

const viewport = ref<HTMLElement | null>(null)
const thumb = ref<HTMLElement | null>(null)

const scrollTop = ref(0)
const scrollHeight = ref(0)
const clientHeight = ref(0)

const isHovered = ref(false)
const isDragging = ref(false)

const isScrollable = computed(
  () => scrollHeight.value > clientHeight.value,
)

const thumbHeight = computed(() => {
  if (!scrollHeight.value) return 0

  return Math.max(
    (clientHeight.value / scrollHeight.value) * clientHeight.value,
    24,
  )
})

const maxThumbOffset = computed(
  () => Math.max(0, clientHeight.value - thumbHeight.value),
)

const thumbOffset = computed(() => {
  const maxScroll = scrollHeight.value - clientHeight.value

  if (maxScroll <= 0) return 0

  return (scrollTop.value / maxScroll) * maxThumbOffset.value
})

let resizeObserver: ResizeObserver | null = null
let mutationObserver: MutationObserver | null = null
let updateEmitted = false

let pointerStartY = 0
let scrollStart = 0
let pointerId: number | null = null

function updateMetrics() {
  const el = viewport.value
  if (!el) return

  scrollTop.value = el.scrollTop
  scrollHeight.value = el.scrollHeight
  clientHeight.value = el.clientHeight
}

function handleScroll(event: Event) {
  const el = viewport.value
  if (!el) return

  updateMetrics()
  emit('scroll', event)

  const distanceToEnd =
    el.scrollHeight - el.clientHeight - el.scrollTop

  if (distanceToEnd <= props.threshold) {
    if (!updateEmitted) {
      updateEmitted = true
      emit('update')
    }
  } else {
    updateEmitted = false
  }
}

function handlePointerDown(event: PointerEvent) {
  if (event.pointerType === 'mouse' && event.button !== 0) return

  const el = viewport.value
  const target = thumb.value

  if (!el || !target) return

  event.preventDefault()

  isDragging.value = true
  pointerId = event.pointerId
  pointerStartY = event.clientY
  scrollStart = el.scrollTop

  target.setPointerCapture(event.pointerId)
}

function handlePointerMove(event: PointerEvent) {
  if (!isDragging.value || pointerId !== event.pointerId) return

  const el = viewport.value
  if (!el) return

  const maxScroll = el.scrollHeight - el.clientHeight
  const maxOffset = maxThumbOffset.value

  if (maxOffset <= 0 || maxScroll <= 0) return

  const deltaY = event.clientY - pointerStartY
  const scrollDelta = (deltaY / maxOffset) * maxScroll

  el.scrollTop = Math.min(
    maxScroll,
    Math.max(0, scrollStart + scrollDelta),
  )
}

function stopDragging() {
  isDragging.value = false
  pointerId = null
}

function handlePointerUp(event: PointerEvent) {
  if (pointerId !== event.pointerId) return
  stopDragging()
}

function handlePointerCancel(event: PointerEvent) {
  if (pointerId !== event.pointerId) return
  stopDragging()
}

function handleLostPointerCapture(event: PointerEvent) {
  if (pointerId !== event.pointerId) return
  stopDragging()
}

function handleMouseEnter() {
  isHovered.value = true
}

function handleMouseLeave() {
  if (!isDragging.value) isHovered.value = false
}

onMounted(async () => {
  await nextTick()

  const el = viewport.value
  if (!el) return

  updateMetrics()

  resizeObserver = new ResizeObserver(updateMetrics)
  resizeObserver.observe(el)

  const content = el.firstElementChild
  if (content) resizeObserver.observe(content)

  mutationObserver = new MutationObserver(updateMetrics)
  mutationObserver.observe(el, {
    childList: true,
    subtree: true,
    characterData: true,
  })
})

onBeforeUnmount(() => {
  resizeObserver?.disconnect()
  mutationObserver?.disconnect()
})
</script>

<template>
  <div
    class="scroll-area"
    :class="{
      'scroll-area--hovered': isHovered,
      'scroll-area--hidden': hideOnLeave,
      'scroll-area--dragging': isDragging,
    }"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
  >
    <div
      ref="viewport"
      class="scroll-area__viewport"
      @scroll="handleScroll"
    >
      <div class="scroll-area__content">
        <slot />
      </div>
    </div>

    <div
      v-if="isScrollable"
      class="scroll-area__track"
      aria-hidden="true"
    >
      <div
        ref="thumb"
        class="scroll-area__thumb"
        :class="{
          'scroll-area__thumb--dragging': isDragging,
        }"
        :style="{
          height: `${thumbHeight}px`,
          transform: `translateY(${thumbOffset}px)`,
        }"
        @pointerdown="handlePointerDown"
        @pointermove="handlePointerMove"
        @pointerup="handlePointerUp"
        @pointercancel="handlePointerCancel"
        @lostpointercapture="handleLostPointerCapture"
      />
    </div>
  </div>
</template>

<style scoped>
.scroll-area {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.scroll-area__viewport {
  width: 100%;
  height: 100%;
  overflow: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.scroll-area__viewport::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.scroll-area__content {
  min-width: 0;
}

.scroll-area__track {
  position: absolute;
  top: 4px;
  right: 3px;
  bottom: 4px;
  width: 12px;
  opacity: 0;
  transition: opacity 200ms ease;
}

.scroll-area--hovered .scroll-area__track {
  opacity: 1;
}

.scroll-area--hidden:not(.scroll-area--hovered) .scroll-area__track {
  opacity: 0;
}

.scroll-area__thumb {
  position: absolute;
  top: 0;
  right: 0px;
  width: 6px;
  opacity: 0.4;
  min-height: 24px;
  border-radius: 999px;
  background: var(--color-muted-foreground);
  cursor: grab;
  pointer-events: auto;
  touch-action: none;
  user-select: none;
  transition:
    background 150ms ease,
    width 150ms ease;
}

.scroll-area__thumb:hover {
  width: 10px;
  right: 0px;
  background: var(--color-muted-foreground);
  opacity: 0.5;
}

.scroll-area__thumb--dragging {
  cursor: grabbing;
  width: 8px;
  right: 0px;
  background: rgba(128, 128, 128, 1);
  transition: none;
}

.scroll-area--dragging {
  user-select: none;
}

@media (prefers-reduced-motion: reduce) {
  .scroll-area__track,
  .scroll-area__thumb {
    transition: none;
  }
}
</style>
