<script setup>
defineProps({
    msg: {
        type: String,
        required: true
    }
})

import ApiTest from '@/service/ApiTest.vue';
</script>

<template>

    <ApiTest />

</template>

<style scoped></style>