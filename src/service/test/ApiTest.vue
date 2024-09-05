<script setup>
defineProps({
    msg: {
        type: String,
        required: true
    }
})

import ServiceTest from '@/service/test/ServiceTest.vue'
import FileTest from '@/service/test/FileTest.vue'
import MaskTest from '@/service/test/MaskTest.vue'
import LoginTest from '@/service/test/LoginTest.vue'
</script>

<template>

    <ServiceTest />
    <FileTest />
    <MaskTest />
    <LoginTest />

</template>

<style scoped></style>
