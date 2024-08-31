<script setup>
import MenuBar from '../MenuBar.vue';
defineProps({
    msg: {
        type: String,
        required: true
    }
})

import ApiTest from '@/service/ApiTest.vue';
</script>

<template>
<MenuBar />

    <ApiTest />

</template>

<style scoped></style>