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
      this.msgSuccess("文件请求成功, 即将下载. 请勿将此文件分享给其他人, 以免被封号.")
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
      let confirmFunc = () => {
        let havePoints = (paymentInfo.points_available > paymentInfo.torrent_points)
        let message = "下载当前资源需要消耗积分点数:<b>" + paymentInfo.torrent_points.toFixed(3) + "</b><br/>" +
          "您当前资源点数为: <b>" + paymentInfo.points_available.toFixed(3) + "</b>, <b style='color: red'>" + (havePoints ? "可以" : "无法") + "</b>" +
          "进行兑换下载.<br/>兑换之后再次下载将不会扣资源点数."
        this.$confirm(message, "购买确认", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          showConfirmButton: havePoints,
          type: "warning",
          dangerouslyUseHTMLString: true
        }).then(() => {
          this.loading = true
          getPaymentConfirm(this.torrentId).then(r => {
            this.loading = false
            if (r.code === 200) {
              this.msgSuccess("购买成功!")
              this.downloadTorrent()
            } else {
              this.msgError(r.msg)
            }
          })
        })
      }
      if (this.torrent.params.peers === 0) {
        this.$confirm('此资源目前没人做种,可能会无法完整下载资源.确定要继续购买吗?', '提示', {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          showConfirmButton: true,
          type: "warning",
        }).then(() => {
          confirmFunc()
        })
      } else {
        confirmFunc()
      }
    },
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
</style>

