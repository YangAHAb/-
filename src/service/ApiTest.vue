<script setup>
defineProps({
    msg: {
        type: String,
        required: true
    }
})

import ServiceTest from '@/service/ServiceTest.vue'
import FileTest from '@/service/FileTest.vue'
import MaskTest from '@/service/MaskTest.vue'
</script>

<template>

    <ServiceTest />
    <FileTest />
    <MaskTest />

</template>

<style scoped></style>
