<template>
  <div class="custom-cropper">
    <el-dialog
      v-model="this.localVisible"
      title="裁剪头像"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      @close="closeDialog"
      width="600px"
    >
      <div class="avatar-container">
        <!-- 待上传图片 -->
        <div v-show="!options.img">
          <el-upload
            class="upload"
            ref="upload"
            action=""
            :on-change="upload"
            accept="image/png, image/jpeg, image/jpg"
            :show-file-list="false"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary" ref="uploadBtn">
              选择图片
            </el-button>
          </el-upload>
          <div>支持jpg、png格式的图片，大小不超过2M</div>
        </div>
        <!-- 已上传图片 -->
        <div v-show="options.img" class="avatar-crop">
          <vue-cropper
            class="crop-box"
            ref="cropper"
            :img="options.img"
            :autoCrop="options.autoCrop"
            :fixedBox="options.fixedBox"
            :canMoveBox="options.canMoveBox"
            :autoCropWidth="options.autoCropWidth"
            :autoCropHeight="options.autoCropHeight"
            :centerBox="options.centerBox"
            :fixed="options.fixed"
            :fixedNumber="options.fixedNumber"
            :canMove="options.canMove"
            :canScale="options.canScale"
          ></vue-cropper>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <div class="reUpload" @click="reUpload">
          <span v-show="options.img">重新上传</span>
        </div>
        <div>
          <el-button @click="closeDialog">取 消</el-button>
          <el-button type="primary" @click="getCrop">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {VueCropper} from 'vue-cropper'
import {ElMessage} from "element-plus";
import 'vue-cropper/dist/index.css'

export default {
  components: {
    VueCropper
  },
  name: 'CustomCropper',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      localVisible: this.visible,
      // vueCropper组件 裁剪配置信息
      options: {
        img: '', // 原图文件
        autoCrop: true, // 默认生成截图框
        fixedBox: false, // 固定截图框大小
        canMoveBox: true, // 截图框可以拖动
        autoCropWidth: 200, // 截图框宽度
        autoCropHeight: 200, // 截图框高度
        fixed: true, // 截图框宽高固定比例
        fixedNumber: [1, 1], // 截图框的宽高比例
        centerBox: false, // 截图框被限制在图片里面
        canMove: true, // 上传图片不允许拖动
        canScale: true // 上传图片不允许滚轮缩放
      }
    }
  },

  methods: {
    // 读取原图
    upload(file) {
      const isIMAGE = file.raw.type === 'image/jpeg' || file.raw.type === 'image/png'
      const isLt2M = file.raw.size >> 20 < 2
      if (!isIMAGE) {
        ElMessage({
          showClose: true,
          message: '上传文件必须是 JPEG 或 PNG 图片',
          type: 'warning'
        })
        return false
      }
      if (!isLt2M) {
        ElMessage({
          showClose: true,
          message: '图片大小不能超过 2MB',
          type: 'warning'
        })
        return false
      }
      let reader = new FileReader()
      reader.readAsDataURL(file.raw)
      reader.onload = e => {
        this.options.img = e.target.result// base64
      }
    },
    // 获取截图信息
    getCrop() {
      if (this.options.img.length === 0) {
        this.closeDialog()
        return;
      }
      // 获取截图的 base64 数据
      // this.$refs.cropper.getCropData((data) => {
      //   this.$emit('closeAvatarDialog', data)
      //   this.closeDialog()
      // });
      // 获取截图的 blob 数据
      this.$refs.cropper.getCropBlob(data => {
        this.$emit('closeAvatarDialog', data)
        this.closeDialog()
      })
    },
    // 重新上传
    reUpload() {
      this.$refs.uploadBtn.$el.click()
    },
    // 关闭弹框
    closeDialog() {
      this.$emit('cancel')
      this.options.img = ''
    }
  },
  watch: {
    visible(newValue) {
      console.log("watch")
      this.localVisible = newValue;
    }
  }
}
</script>

<style lang="less" scoped>
.custom-cropper {
  position: absolute;
  margin: auto;
  height: 100%;
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;

  .reUpload {
    color: #409eff;
    cursor: pointer;
  }
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 560px;
  height: 400px;
  background-color: #f0f2f5;
  margin-right: 10px;
  border-radius: 4px;

  .upload {
    text-align: center;
    margin-bottom: 24px;
  }

  .avatar-crop {
    width: 560px;
    height: 400px;
    position: relative;

    .crop-box {
      width: 100%;
      height: 100%;
      border-radius: 4px;
      overflow: hidden;
    }
  }
}
</style>