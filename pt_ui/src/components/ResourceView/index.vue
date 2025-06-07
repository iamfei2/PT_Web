<template>
  <div class="torrent-detail-page">
    <el-card v-if="torrent" :header="torrent.title" class="main-card" shadow="hover">
      <el-row :gutter="20">
        <!-- 图片列 (响应式设计) -->
        <el-col :xs="24" :sm="24" :md="8" :lg="6">
          <div class="image-container">
            <img src="../../assets/default-thumb.jpg"
                 class="torrent-image"
            />
          </div>
        </el-col>

        <!-- 信息列 (响应式设计) -->
        <el-col :xs="24" :sm="24" :md="16" :lg="18">
          <el-form label-position="top" class="torrent-info-form">
            <!-- 标签信息 -->
            <el-form-item label="资源标签" v-if="torrent.tags.length > 0">
              <torrent-tag :torrent="torrent"/>
            </el-form-item>

            <!-- 基本信息组 -->
            <div class="info-group">
              <div class="info-item">
                <span class="info-label"><i class="el-icon-document"></i> 资源名称</span>
                <span class="info-value">{{ torrent.title }}</span>
              </div>

              <div class="info-item">
                <span class="info-label"><i class="el-icon-user"></i> 发布者</span>
                <span class="info-value">{{ torrent.createBy }}</span>
              </div>

              <div class="info-item">
                <span class="info-label"><i class="el-icon-download"></i> 下载量</span>
                <span class="info-value">{{ formatFileSize(torrent.downloaded) }}</span>
              </div>

              <div class="info-item">
                <span class="info-label"><i class="el-icon-connection"></i> 当前做种</span>
                <span class="info-value">{{ torrent.params.peers }}
                  <el-tag v-if="torrent.params.peers === 0" type="danger" size="mini" effect="dark">无做种</el-tag>
                </span>
              </div>
            </div>

            <!-- Torrent查看器 -->
            <torrent-viewer :torrent="torrent.torrent"/>

            <!-- 附件信息 -->
            <el-form-item label="相关附件" v-if="torrent.attachment && torrent.attachment.length > 0">
              <ul class="attachment-list">
                <li v-for="(v, k) in torrent.attachment" :key="'attachment_' + k" class="attachment-item">
                  <el-link :underline="false"
                           type="primary"
                           class="attachment-link"
                           @click="downloadAttachment(v)">
                    <i class="el-icon-paperclip"></i> {{ v.name }}
                    <el-tag v-if="v.size" size="mini">{{ formatFileSize(v.size) }}</el-tag>
                  </el-link>
                </li>
              </ul>
            </el-form-item>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="primary"
                         class="download-btn"
                         @click="confirmDownload"
                         icon="el-icon-download">
                下载种子
              </el-button>
              <el-button v-if="torrent.url"
                         type="info"
                         plain
                         @click="openExternal(torrent.url)">
                <i class="el-icon-link"></i> 外部链接
              </el-button>
            </div>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <!-- 详细介绍部分 -->
    <el-card header="详细介绍" class="description-card" v-if="torrent">
      <div class="description-content">
        <div v-html="torrent.description"></div>
      </div>
      <div class="stats-footer">
        <span><i class="el-icon-time"></i> 创建时间：{{ formatTime(torrent.createTime) }}</span>
        <span><i class="el-icon-refresh"></i> 最后更新：{{ formatTime(torrent.updateTime) }}</span>
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
    formatTime(timestamp) {
      if (!timestamp) return '未知时间';
      return new Date(timestamp).toLocaleString();
    },
    handleImageError(e) {
      e.target.style.display = 'none';
      e.target.parentNode.querySelector('.image-placeholder').style.display = 'flex';
    },
    openExternal(url) {
      window.open(url, '_blank');
    },
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

.torrent-detail-page {
  padding: 15px;
  background: #f6f9fc;
}

.main-card {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #ffffff 0%, #f7f9fc 100%);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.main-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.image-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  background: #f1f5f9;
  border-radius: 8px;
  overflow: hidden;
}

.torrent-image {
  width: 100%;
  height: auto;
  max-height: 300px;
  object-fit: cover;
  border-radius: 6px;
  transition: transform 0.3s;
}

.torrent-image:hover {
  transform: scale(1.02);
}

.image-placeholder {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 200px;
  background: #f1f5f9;
  color: #a3b1c2;
  border-radius: 8px;
  font-size: 14px;
}

.image-placeholder i {
  font-size: 48px;
  margin-bottom: 10px;
}

.torrent-info-form {
  padding: 5px 10px;
}

.info-group {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.info-item {
  background: #ffffff;
  padding: 12px 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
  border-left: 3px solid #409EFF;
}

.info-label {
  display: block;
  font-size: 12px;
  color: #6b7280;
  margin-bottom: 5px;
}

.info-label i {
  margin-right: 5px;
}

.info-value {
  font-size: 15px;
  color: #1f2937;
  font-weight: 500;
}

.attachment-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.attachment-item {
  padding: 8px 0;
  border-bottom: 1px dashed #e5e7eb;
}

.attachment-item:last-child {
  border-bottom: none;
}

.attachment-link {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.attachment-link i {
  margin-right: 6px;
}

.attachment-link .el-tag {
  margin-left: 8px;
  height: 22px;
  line-height: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  padding-top: 15px;
  margin-top: 20px;
  border-top: 1px solid #f0f3f8;
}

.download-btn {
  background: linear-gradient(135deg, #3f87fe 0%, #396aff 100%);
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  transition: all 0.2s;
  box-shadow: 0 4px 10px rgba(63, 135, 254, 0.3);
}

.download-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(63, 135, 254, 0.4);
}

.description-card {
  margin-top: 20px;
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  background: white;
}

.description-content {
  padding: 15px;
  line-height: 1.8;
  color: #374151;
}

.stats-footer {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #6b7280;
}

.stats-footer i {
  margin-right: 5px;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .image-container {
    margin-bottom: 20px;
  }

  .info-group {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 768px) {
  .info-group {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-buttons .el-button {
    width: 100%;
    margin-bottom: 10px;
  }
}

/* 滚动条样式（保持原样） */
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

