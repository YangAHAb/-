<script setup>
defineProps({
    msg: {
        type: String,
        required: true
    }
})

import ServiceTest from '../service/ServiceTest.vue'
import FileService from '../service/FileService.vue'
</script>

<template>

    <ServiceTest />
    <FileService />


</template>

<style scoped></style>
