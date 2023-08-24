<template>
  <div>
    <el-button @click="dialogVisible = true">上传头像</el-button>
    <CustomCropper :visible.sync="dialogVisible" @closeAvatarDialog="closeAvatarDialog" @cancel="dialogVisible=false"></CustomCropper>
  </div>
</template>

<script setup lang="ts">
import CustomCropper from '../components/CustomCropper.vue'

const dialogVisible = ref(false);

function closeAvatarDialog(data) {
  console.log(data)
  dialogVisible.value = false;
}
</script>