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
    <MenuBar class="semi-transparent-menu" />        
    <div class="home-container">        
        <div class="image-carousel">        
            <div class="carousel-nav prev" @click="prevImageFunc"></div>    
            <img :src="currentImage" alt="Current Image" class="carousel-image" />        
            <div class="carousel-nav next" @click="nextImageFunc"></div>    
        </div> 
    </div>    
    <div class="side-menu">  
    <a href="../doc/隐私数据脱敏管理平台用户手册.docx" class="menu-button" target="_blank">  
        <i class="fas fa-download"></i> 用户手册  
    </a>  
    <a href="../doc/需求分析报告.docx" class="menu-button" target="_blank">  
        <i class="fas fa-download"></i> 需求分析  
    </a>  
    <a href="../doc/隐私数据脱敏管理平台系统设计文档.docx" class="menu-button" target="_blank">  
        <i class="fas fa-download"></i> 系统设计  
    </a>  
    <a href="../doc/风险管理报告.docx" class="menu-button" target="_blank">  
        <i class="fas fa-download"></i> 风险报告  
    </a>  
    <a href="../doc/预算管理.xlsx" class="menu-button" target="_blank">  
        <i class="fas fa-download"></i> 预算计划  
    </a>  
</div> 
<div class="project-summary">  
            <h1>隐私数据脱敏管理平台项目</h1>  
            <p>  
                本项目旨在构建一个隐私数据脱敏管理平台，以满足当前法律法规对个人信息保护的高要求，并应对企业业务发展中对用户隐私保护的需求。  
                项目通过集成用户管理、数据库管理、数据脱敏操作、UI界面设计、任务列表管理以及操作日志记录等功能模块，  
                确保敏感数据在存储、处理和传输过程中的安全性和合规性。  
            </p>  
  
            <h2>项目背景与需求分析</h2>  
            <p>  
                随着科技的飞速发展和数字化时代的到来，数据隐私保护变得尤为重要。本项目旨在通过数据脱敏技术，  
                实现对敏感数据的自动识别和脱敏处理，保障数据的安全性和合规性。  
                平台的主要用户包括普通用户、相关企业以及软件运维人员，他们分别承担着数据提供、数据使用和平台维护的角色。  
            </p>  
  
            <h2>功能模块设计</h2>  
            <ul>  
                <li>  
                    <strong>用户管理：</strong> 包括用户注册、登录、权限分配和信息修改等功能。  
                    系统支持多角色管理，确保不同用户拥有不同的访问和操作权限，从而保障敏感数据的安全。  
                </li>  
                <li>  
                    <strong>数据库管理：</strong> 允许用户上传、下载和展示数据库文件。  
                    系统使用OpenGauss数据库对隐私数据进行加密处理，确保数据在存储过程中的安全性。  
                    同时，提供数据预览和展示功能，方便用户随时查看数据内容。  
                </li>  
                <li>  
                    <strong>数据脱敏操作：</strong> 系统能够自动识别隐私数据，并提供多种脱敏方法（如替换、加密等）进行处理。  
                    脱敏过程支持用户自定义脱敏规则和策略，满足不同场景下的脱敏需求。  
                </li>  
                <li>  
                    <strong>UI界面设计：</strong> 设计简洁易用的用户界面，提供任务执行结果的实时反馈。  
                    系统首页、任务列表页面和脱敏结果页面等界面均经过精心设计，确保用户能够方便快捷地操作平台。  
                </li>  
                <li>  
                    <strong>任务列表管理：</strong> 管理用户创建的所有脱敏任务，显示任务进度和执行状态。  
                    用户可以随时查看任务的执行情况，并对任务进行暂停、恢复或删除等操作。  
                </li>  
                <li>  
                    <strong>操作日志记录：</strong> 记录用户在平台上的所有操作行为，为安全审计和故障排查提供依据。  
                    日志记录功能有助于追踪用户操作轨迹，确保平台的可追溯性和可审计性。  
                </li>  
            </ul>  
  
            <h2>项目开发与实施</h2>  
            <p>  
                项目开发采用敏捷开发和DevOps思想相结合的方法。通过Scrum模式进行迭代开发，每个迭代周期持续两周左右。  
                项目团队使用CodeArts进行项目规划和需求管理，通过看板模式展示项目进度和任务状态。  
                代码管理采用Git分支策略，确保代码的稳定性和可追溯性。在代码开发过程中，项目团队注重代码质量和规范性。  
                通过CodeArts代码检查服务对源代码进行静态代码扫描和问题采集，确保代码质量符合标准。  
                同时，利用编译构建服务实现应用的Docker镜像打包和推送，提高自动化编译效率。  
            </p>  
  
            <h2>项目测试与部署</h2>  
            <p>  
                项目测试覆盖功能、性能、UI、安全性、兼容性和容量等多个方面。  
                通过黑盒测试和白盒测试相结合的方法，确保软件系统的质量和稳定性。  
                测试数据主要依据需求分析文档和概要设计文档进行设计，确保测试用例的全面性和代表性。  
                项目部署采用持续部署与发布策略。通过CodeArts部署服务将软件项目的发布包一键式部署到服务器主机上，  
                并利用流水线服务实现应用程序的持续交付。部署过程自动化程度高，显著提高了部署效率和准确性。  
            </p>  
  
            <h2>项目成果与展望</h2>  
            <p>  
                经过多轮迭代开发和测试验证，本项目成功构建了一个功能完善、性能稳定的隐私数据脱敏管理平台。  
                平台已在企业内部上线运行并取得良好效果，有效保障了敏感数据的安全性和合规性。  
                未来，项目团队将继续优化平台功能和性能，以满足更多用户和场景的需求。  
                同时，积极探索数据脱敏技术在其他领域的应用前景，推动数据安全保护工作的深入开展。  
            </p>  
        </div>             
</template>
  
<style scoped>  
.home-container {  
    max-width: 1200px;  
    margin: 0 auto;  
    padding: 20px;  
    position: relative; 
    font-family: 'Roboto', sans-serif;  
    display: flex;  
    flex-direction: column;  
    align-items: center;  
    height: 100vh; 
    overflow: auto; 
}  
  
  
.menu-bar {  
    background-color: #8B4513;  
    color: white;  
    padding: 10px 20px;  
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  
    border-radius: 8px;  
    margin-bottom: 20px;  
}  
  
.image-carousel {  
    position: absolute; 
    top: 30%; 
    left: 50%; 
    transform: translate(-50%, -50%); 
    width: 68%;  
    max-width: 100%; 
    overflow: hidden;  
    display: flex;  
    justify-content: center;  
    align-items: center;  
    border-radius: 12px;  
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);  
}  
  
.carousel-image {  
    max-width: 100%;  
    max-height: 100%;  
    object-fit: cover;  
    border-radius: 12px;  
    transition: opacity 0.5s, transform 0.5s;  
    transform: scale(1);  
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);  
}  
  
.carousel-image:hover {  
    transform: scale(1.05);  
}  
  
.carousel-nav {  
    position: absolute;  
    top: 50%;  
    transform: translateY(-50%);  
    width: 50px;  
    height: 100%;  
    cursor: pointer;  
    display: flex;  
    justify-content: center;  
    align-items: center;  
    color: white;  
    background-color: rgba(0, 0, 0, 0.1);  
    transition: background-color 0.3s;  
}  
  
.carousel-nav.prev {  
    left: 0;  
}  
  
.carousel-nav.next {  
    right: 0;  
}  
  
.carousel-nav:hover {  
    background-color: rgba(0, 0, 0, 0.7);  
}  
  
  
.project-summary h1,  
.project-summary h2 {  
    color: #333;  
}  
  
.project-summary p {  
    margin: 10px 0;  
    line-height: 1.6;  
    color: #666;  
}  
  
.project-summary ul {  
    margin: 10px 0;  
    padding-left: 20px;  
    list-style-type: disc;  
}  
  
.project-summary ul li {  
    margin: 5px 0;  
    line-height: 1.6;  
    color: #666;  
}  
  
.project-summary ul li strong {  
    color: #333;  
}  
  
.side-menu {  
    position: absolute;  
    top: 50%;  
    right: 20px;  
    transform: translateY(-50%);  
    display: flex;  
    flex-direction: column;  
    align-items: flex-end;  
    background-color: rgba(255, 255, 255, 0.8);  
    padding: 10px;  
    border-radius: 8px;  
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  
}  
  
@media (max-width: 768px) {  
    .side-menu {  
        position: static;  
        transform: none;  
        margin-top: 20px;  
        width: 100%;  
        display: flex;  
        justify-content: center;  
    }  
}  
  
.semi-transparent-menu {  
    opacity: 0.8;  
}  
  
.carousel-nav {  
    width: 30px; /* 缩小宽度 */  
    height: 50px; /* 缩小高度 */  
    font-size: 14px; /* 调整字体大小 */  
}  
  
.carousel-nav i {  
    font-size: 12px;  
}  
  
.menu-button {  
    margin-bottom: 10px;  
    padding: 8px 16px; /* 调整内边距 */  
    font-size: 14px; /* 调整字体大小 */  
    color: white;  
    background-color: #8B4513;  
    border: none;  
    border-radius: 12px; /* 增加圆角 */  
    cursor: pointer;  
    transition: background-color 0.3s, transform 0.3s, box-shadow 0.3s; /* 添加阴影过渡 */  
    display: flex;  
    align-items: center;  
    text-decoration: none;  
    width: auto;  
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影 */  
}  
  
.menu-button:hover {  
    background-color: #6B3E08;  
    transform: scale(1.05); /* 按钮悬浮时放大 */  
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* 悬浮时增加阴影 */  
}  
  
.menu-button i {  
    margin-right: 4px;  
} 

.project-summary {    
    width: 80%;  
    max-width: 800px; 
    margin: 0 auto; 
    margin-bottom: 20px;  
}    
</style>