<template>
  <div class="folder-item">
    <img :src="torrent.thumburl" @click="viewTorrent">
    <div class="torrent-detail">
      <span style="background-color: rgba(23,23,23,0.4); border-radius: 5px; padding: 5px;">{{ formatFileSize(torrent.fileSize) }}</span>
    </div>

    <el-tooltip :content="torrent.title">
      <div class="item-title"><span @click="viewTorrent">{{ torrent.title }}</span></div>
    </el-tooltip>
    <torrent-tag v-if="torrent.tags && torrent.tags.length > 0" :torrent="torrent" size="mini" :limit="3"/>
<!--    <div>-->
<!--      <el-tag size="mini" style="margin: 5px;" type="success">{{ torrent.categoryName }}</el-tag>-->
<!--      <el-tag size="mini" style="margin: 5px;">{{ formatFileSize(torrent.fileSize) }}</el-tag>-->
<!--    </div>-->
  </div>
</template>

<script>
  import TorrentTag from "@/components/TorrentTag"
  export default {
    components: { TorrentTag },
    props: {
      torrent: {
        type: Object,
        default: null
      }
    },
    data () {
      return {}
    },
    methods: {
      viewTorrent () {
        this.$router.push('/detail/torrent/' + this.torrent.id)
      }
    }
  }
</script>
<style scoped lang="scss">
  .torrent-detail {
    margin-top: -22px;
    color: white;
  }
  .folder-item {
    width: 250px;
    overflow: hidden;
    cursor: pointer;
    text-align: center;
    border-radius: 10px;
    padding: 5px;
    height: 330px;
    img {
      height: 250px;
      /*max-width: 100%;*/
      width: 170px;
      border-radius: 5px;
    }
    .item-title {
      height: 34px;
      line-height: 34px;
      text-overflow:ellipsis;
      overflow: hidden;
      white-space: nowrap;
      -o-text-overflow: ellipsis;
    }
  }
  .folder-item:hover {
    background-color: rgba(233,233,233,0.8);
  }
</style>
