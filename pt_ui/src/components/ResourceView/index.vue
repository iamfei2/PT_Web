<template>
  <div style="padding: 15px;">
    <el-card v-if="torrent" :header="torrent.title">
      <el-row>
        <el-col :span="6">
          <img v-if="torrent" :src="torrent.thumburl" style="width: 100%; border-radius: 5px;"/>
        </el-col>
        <el-col :span="18" v-if="torrent">

          <el-form label-width="80px">
            <el-form-item label="标 签" v-if="torrent.tags.length > 0">
              <torrent-tag :torrent="torrent"/>
            </el-form-item>
            <el-form-item label="资源名称">{{ torrent.title }}</el-form-item>
            <el-form-item label="发 布 者">{{ torrent.createBy }}</el-form-item>
            <el-form-item label="下 载 量">{{ formatFileSize(torrent.downloaded) }}</el-form-item>
            <el-form-item label="当前做种">{{ torrent.params.peers }}</el-form-item>
          </el-form>
          <torrent-viewer :torrent="torrent.torrent"/>
          <el-form label-width="80px" v-if="torrent.attachment && torrent.attachment.length > 0">
            <el-form-item label="附 件">
              <li v-for="(v, k) in torrent.attachment" :key="'attachment_' + k">
                <el-link :underline="false" type="primary" style="line-height: 18px;" @click="downloadAttachment(v)">
                  {{ v.name }}
                </el-link>
              </li>
            </el-form-item>
          </el-form>
          <el-form label-width="80px">
            <el-form-item label="操作">
              <el-button type="primary" @click="confirmDownload" size="mini" style="float: left;">下载种子</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    <el-card header="详细介绍" v-if="torrent" style="margin-top: 5px;">
      <div style="padding: 10px; margin-top: 5px;" id="style-1">
        <div v-html="torrent.description"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
let Base64 = require('js-base64').Base64
import TorrentViewer from "@/components/TorrentViewer"
import TorrentTag from "@/components/TorrentTag"
import {getTorrent, getPaymentInfo, getPaymentConfirm, getDownload, getAttachmentDownload} from "@/api/galaxy/piazza"

export default {
  components: {TorrentViewer, TorrentTag},
  props: {
    torrentId: null
  },
  watch: {
    torrentId: {
      immediate: true,
      handler(nv) {
        if (nv !== null)
          this.loadTorrent(nv)
      }
    }
  },
  data() {
    return {
      loading: false,
      torrent: null,
      show: false,
      paymentInfo: null,
    }
  },
  methods: {
    loadTorrent(torrentId) {
      this.torrent = null
      this.show = false
      this.loading = true
      getTorrent(torrentId).then(res => {
        this.loading = false
        this.show = true
        this.torrent = res.data
        // this.$route.meta.title = this.torrent.title
        this.$emit('on-torrent', this.torrent)
      })
    },
    handleClosed() {
      this.torrent = null
      this.paymentInfo = null
      this.$emit('closed')
    },
    downloadBlob(fileName, data) {
      if (!fileName.endsWith(".torrent")) {
        fileName = fileName + ".torrent"
      }
      const a = document.createElement('a')
      a.style.display = "none"
      let de64 = Base64.atob(data)
      let ia = new Uint8Array(de64.length);//创建视图
      for (let i = 0; i < de64.length; i++) {
        ia[i] = de64.charCodeAt(i);
      }
      let blob = new Blob([ia], {type: 'application/octet-stream'})
      if (window.navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, fileName)
      } else {
        let dataUrl = URL.createObjectURL(blob)
        document.body.appendChild(a)
        a.href = dataUrl
        a.download = fileName;
        a.click()
        URL.revokeObjectURL(data);
        document.body.removeChild(a)
      }

    },
    downloadAttachment(attachment) {
      getAttachmentDownload(attachment.id).then(res => {
        if (res.code === 200) {
          this.downloadBlob(res.msg, res.data)
        } else {
          this.msgError("文件无法下载")
        }
      })
    },
    downloadTorrent() {
      getDownload(this.torrentId).then(res => {
        if (res.code === 200) {
          this.downloadBlob(res.msg, res.data)
        } else {
          this.msgError("文件无法下载")
        }
      })
    },
    checkPeers(paymentInfo) {
      const confirmFunc = () => {
        const havePoints = paymentInfo.points_available > paymentInfo.torrent_points;
        
        // 自定义消息模板
        const message = `
          <div class="custom-message-box">
            <p>需要消耗积分: <span class="highlight">${paymentInfo.torrent_points.toFixed(3)}</span></p>
            <p>您的资源点数: <span class="highlight">${paymentInfo.points_available.toFixed(3)}</span></p>
            <p class="${havePoints ? 'success-text' : 'error-text'}">
              ${havePoints ? "可以下载" : "无法下载"}
            </p>
          </div>
        `;

        this.$confirm(message, "购买确认", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          showConfirmButton: havePoints,
          type: "warning",
          dangerouslyUseHTMLString: true,
          customClass: "custom-confirm-dialog"  // 添加自定义类名
        }).then(() => {
          this.loading = true;
          getPaymentConfirm(this.torrentId).then(r => {
            this.loading = false;
            if (r.code === 200) {
              this.msgSuccess("购买成功!");
              this.downloadTorrent();
            } else {
              this.msgError(r.msg);
            }
          });
        });
      };

      // 做种人数为0的提示
      if (this.torrent.params.peers === 0) {
        this.$confirm(
          '<div class="custom-message-box"><p class="warning-text">此资源做种人数为0</p><p>确定要继续购买吗？</p></div>',
          '提示',
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            showConfirmButton: true,
            type: "warning",
            dangerouslyUseHTMLString: true,
            customClass: "custom-confirm-dialog"  // 相同的自定义类名
          }
        ).then(() => {
          confirmFunc();
        });
      } else {
        confirmFunc();
      }
    }
  ,
    confirmDownload() {
      this.loading = true
      getPaymentInfo(this.torrentId).then(res => {
        this.loading = false
        let paymentInfo = res.data
        if (!paymentInfo.isPaid) {
          this.checkPeers(paymentInfo)
        } else {
          this.downloadTorrent()
        }
      })
    }
  }
}
</script>

<style scoped>

/* 全局样式 */
.custom-confirm-dialog {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  background: linear-gradient(135deg, #f8fafc, #ffffff);
  border: 1px solid #e4e7ed;
  width: 500px;
}

.custom-confirm-dialog .el-message-box__header {
  background: linear-gradient(135deg, #6a11cb, #2575fc);
  padding: 16px 20px;
  border-radius: 12px 12px 0 0;
}

.custom-confirm-dialog .el-message-box__title {
  color: white;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.custom-confirm-dialog .el-message-box__content {
  padding: 25px;
  color: #4a5568;
  font-size: 16px;
  line-height: 1.6;
}

.custom-message-box p {
  margin-bottom: 12px;
  text-align: center;
}

.highlight {
  font-weight: 700;
  color: #2d8cf0;
  font-size: 18px;
}

.success-text {
  color: #67c23a;
  font-weight: 600;
  font-size: 17px;
  margin-top: 15px;
}

.error-text {
  color: #f56c6c;
  font-weight: 600;
  font-size: 17px;
  margin-top: 15px;
}

.warning-text {
  color: #e6a23c;
  font-weight: 600;
  font-size: 18px;
  margin-bottom: 10px;
}

.custom-confirm-dialog .el-message-box__btns {
  padding: 15px 25px 25px;
  text-align: center;
}

.custom-confirm-dialog .el-button {
  border-radius: 8px;
  padding: 10px 22px;
  font-size: 15px;
  min-width: 90px;
  transition: all 0.3s;
  font-weight: 500;
}

.custom-confirm-dialog .el-button--primary {
  background: linear-gradient(135deg, #6a11cb, #2575fc);
  border: none;
  color: white;
}

.custom-confirm-dialog .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(37, 117, 252, 0.4);
}

.custom-confirm-dialog .el-button--default {
  border: 1px solid #dcdfe6;
  background: #f5f7fa;
  color: #606266;
}

.custom-confirm-dialog .el-button--default:hover {
  background: #e4e7ed;
}
.el-form-item {
  margin-bottom: 0;
}

#style-1::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  background-color: #F5F5F5;
}

#style-1::-webkit-scrollbar {
  width: 12px;
  background-color: #F5F5F5;
}

#style-1::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
  background-color: #555;
}
.custom-confirm-box {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  border: none;
  background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
  width: 480px;
}

.custom-confirm-box .el-message-box__header {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  padding: 16px 20px;
  border-radius: 12px 12px 0 0;
}

.custom-confirm-box .el-message-box__title {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.custom-confirm-box .el-message-box__content {
  padding: 30px 25px;
  color: #4a5568;
  font-size: 16px;
  line-height: 1.8;
}

.custom-confirm-content p {
  margin-bottom: 12px;
}

.point-value {
  font-weight: 700;
  color: #2d8cf0;
  font-size: 18px;
}

.can-download {
  color: #67c23a;
  font-weight: 600;
  font-size: 17px;
  margin-top: 15px;
}

.cannot-download {
  color: #f56c6c;
  font-weight: 600;
  font-size: 17px;
  margin-top: 15px;
}

.warning-text {
  color: #e6a23c;
  font-weight: 600;
  font-size: 18px;
}

.custom-confirm-box .el-message-box__btns {
  padding: 15px 25px 25px;
  text-align: center;
}

.custom-confirm-box .el-button {
  border-radius: 8px;
  padding: 12px 24px;
  font-size: 15px;
  min-width: 100px;
  transition: all 0.3s;
}

.custom-confirm-box .el-button--primary {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  border: none;
}

.custom-confirm-box .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(37, 117, 252, 0.4);
}

.custom-confirm-box .el-button--default {
  border: 1px solid #dcdfe6;
  background: #f5f7fa;
}

.custom-confirm-box .el-button--default:hover {
  background: #e4e7ed;
}
</style>

