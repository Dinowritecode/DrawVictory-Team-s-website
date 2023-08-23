<template>
  <div>
    <input type="file" @change="onFileChange">
    <cropper ref="cropper" :src="imageSrc" :aspect-ratio="1" :auto-crop="true"></cropper>
    <button @click="cropImage">Crop Image</button>
  </div>
</template>

<script lang="ts">
import Cropper from 'vue-cropperjs';

export default {
  components: {
    Cropper,
  },
  data() {
    return {
      imageSrc: '',
    };
  },
  methods: {
    onFileChange(event) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imageSrc = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    cropImage() {
      const cropper = this.$refs.cropper.$cropper;
      const canvas = cropper.getCroppedCanvas();
      // 在这里处理裁剪后的图片
    },
  },
};
</script>