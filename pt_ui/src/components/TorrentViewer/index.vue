<template>
  <el-form label-width="80px" v-if="torrent">
    <el-form-item label="文件名称">{{ torrent.name }}</el-form-item>
    <el-form-item label="文件大小">{{ formatFileSize(totalSize) }}</el-form-item>
    <el-form-item label="文件数量">{{ torrent.fileList ? torrent.fileList.length : 1 }}</el-form-item>
    <el-form-item label="文件详情" v-if="torrent.fileList !== null && torrent.fileList.length > 0">
      <div v-for="(v, k) in fileList" :key="'torrent.file_' + k">
        [{{ formatFileSize(v.fileLength) }}] {{ v.fileDirs.join("/") }}
      </div>
      <div v-if="torrent.fileList.length > 5 && !showAll" style="cursor: pointer; color:#1890ff;" @click="showAll = !showAll">[剩余{{ (torrent.fileList.length - 5) }}个文件暂不显示, 点击此处全部显示]</div>
      <div v-if="torrent.fileList.length > 5 && showAll" style="cursor: pointer; color:#1890ff;" @click="showAll = !showAll">[总计{{ torrent.fileList.length }}个文件, 点击此处只显示一部分]</div>
    </el-form-item>
    <el-form-item v-if="token" label="Tracker">{{ origin + "/announce?token=" + token + "&type=attachment" }}</el-form-item>
  </el-form>
</template>

<script>
  export default {
    props: {
      torrent: {
        type: Object,
        default: null
      },
      token: {
        type: String,
        default: null
      }
    },
    watch: {
      torrent (nv) {
        this.showAll = false
      }
    },
    computed: {
      fileList () {
        if (this.showAll) {
          return this.torrent.fileList
        } else {
          return this.torrent.fileList.slice(0, 5)
        }
      },
      totalSize () {
        if (this.torrent.totalSize) {
          return this.torrent.totalSize
        }
        let total = 0
        this.torrent.fileList.forEach(x => {
          total += x.fileLength
        })
        return total
      }
    },
    data () {
      return {
        showAll: false,
        origin: document.location.origin
      }
    }
  }
</script>
<style scoped>
  .el-form-item {
    margin-bottom: 0;
  }
</style>
