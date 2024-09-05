<script setup>    
import MenuBar from '../MenuBar.vue';      
import { ref, computed } from 'vue';    
    
defineProps({    
    msg: {    
        type: String,    
        required: true    
    }    
});    
    
const currentImageIndex = ref(0);    
const images = [    
    '/images/image1.png',    
    '/images/image2.png',    
    '/images/image3.png',    
    '/images/image4.png',    
    '/images/image5.png'    
];    
    
const currentImage = computed(() => images[currentImageIndex.value]);    
    
function nextImageFunc() {    
    currentImageIndex.value = (currentImageIndex.value + 1) % images.length;    
}    
    
function prevImageFunc() {    
    currentImageIndex.value = (currentImageIndex.value - 1 + images.length) % images.length;    
}    
    
function navigateToDoc(doc) {    
    console.log(`Navigating to ${doc}`);    
}    
</script>  
  
<template>    
    <MenuBar />    
    
    <div class="home-container">    
        <div class="image-carousel">    
            <button class="carousel-button prev" @click="prevImageFunc">上一张</button>    
            <img :src="currentImage" alt="Current Image" class="carousel-image" />    
            <button class="carousel-button next" @click="nextImageFunc">下一张</button>    
        </div>      
    </div>
    <div class="side-menu">    
        <a href="../doc/隐私数据脱敏管理平台用户手册.docx" class="menu-button" target="_blank">用户手册</a>
        <a href="../doc/隐私数据脱敏管理平台用户手册.docx" class="menu-button" target="_blank">用户手册</a>
        <a href="../doc/隐私数据脱敏管理平台用户手册.docx" class="menu-button" target="_blank">用户手册</a>
        </div>      
</template>
  
<style scoped>  
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap');  
  
.home-container {  
    max-width: 1200px;  
    margin: 0 auto;  
    padding: 20px;  
    position: relative;  
    font-family: 'Roboto', sans-serif;  
}  
  
.menu-bar {  
    background-color: #2c3e50;  
    color: white;  
    padding: 10px 20px;  
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  
    border-radius: 8px;  
    margin-bottom: 20px;  
}  
  
.image-carousel {  
    position: relative;  
    width: 100%;  
    height: 500px; /* 增加高度以更好地展示图片 */  
    overflow: hidden;  
    display: flex;  
    justify-content: center;  
    align-items: center;  
    margin-bottom: 20px;  
    border-radius: 12px;  
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);  
}  
  
.carousel-image {  
    max-width: 100%;  
    max-height: 100%;  
    border-radius: 12px;  
    transition: opacity 0.5s, transform 0.5s;  
}  
  
.carousel-button {  
    position: absolute;  
    top: 50%;  
    transform: translateY(-50%);  
    padding: 12px 24px;  
    font-size: 18px;  
    color: #fff;  
    background-color: rgba(0, 0, 0, 0.6);  
    border: none;  
    border-radius: 24px;  
    cursor: pointer;  
    transition: background-color 0.3s;  
}  
  
.carousel-button.prev {  
    left: 20px;  
}  
  
.carousel-button.next {  
    right: 20px;  
}  
  
.carousel-button:hover {  
    background-color: rgba(0, 0, 0, 0.8);  
}  
  
.side-menu {  
    position: absolute;  
    top: 50%;  
    right: 20px;  
    transform: translateY(-50%);  
    display: flex;  
    flex-direction: column;  
    align-items: flex-end;  
}  
  
.menu-button {  
    margin-bottom: 10px;  
    padding: 12px 24px;  
    font-size: 16px;  
    color: #fff;  
    background-color: #3498db;  
    border: none;  
    border-radius: 24px 0 0 24px;  
    cursor: pointer;  
    transition: background-color 0.3s, transform 0.3s;  
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);  
}  
  
.menu-button:hover {  
    background-color: #2980b9;  
    transform: translateX(5px);  
}  
.carousel-image {  
    font-family: 'Roboto', sans-serif;  
    font-weight: 300;  
    max-width: 100%;  
    max-height: 100%;  
    border-radius: 12px;  
    transition: opacity 0.5s, transform 0.5s;  
}
</style>