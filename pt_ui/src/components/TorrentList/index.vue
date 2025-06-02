<template>
  <div>
    <el-card v-loading="loading">
      <div class="folder-box" v-if="torrents !== null && torrents.length > 0">
        <torrent-card :torrent="v" v-for="(v, k) in torrents" :key="'torrent_' + k"></torrent-card>
      </div>
      <div v-else style="text-align: center; font-size: 24px;height: 300px; color: #5c5c5c;">
        <i class="el-icon-ice-tea" style="line-height: 300px;">这里还没有种子哦</i>
      </div>
    </el-card>
    <div>
      <el-pagination style="float: right;" @current-change="handleCurrentChange"
        :current-page.sync="searchParams.pageNum" :page-size="searchParams.pageSize" layout="total, prev, pager, next"
        :total="page.total">
      </el-pagination>
    </div>
    <!--    <torrent-dialog :torrent-id="torrentId" @closed="handleClosed"/>-->
  </div>
</template>

<script>
import TorrentCard from "@/components/TorrentCard"
import TorrentViewer from "@/components/TorrentViewer"
import { listCategory } from "@/api/galaxy/category";
import { listTorrents, getNotice } from "@/api/galaxy/piazza"
export default {
  name: "TorrentListIndex",
  components: {
    TorrentViewer,
    TorrentCard
  },
  props: {
    queryParams: {
      type: Object,
      default: () => {
        return {
          pageNum: 1,
          pageSize: 20,
          infoHash: null,
          title: null,
          categories: null,
          status: null,
          orderByColumn: 'updateTime',
          isAsc: 'desc'
        }
      }
    }
  },
  watch: {
    queryParams: {
      immediate: true,
      deep: true,
      handler(nv) {
        if (nv) {
          this.searchParams = nv
          this.handleCurrentChange()
        }
      }
    }
  },
  data() {
    return {
      page: {
        total: 0
      },
      searchParams: this.queryParams,
      torrents: null,
      loading: false
    };
  },
  methods: {
    handleCurrentChange() {
      this.loading = true
      listTorrents(this.searchParams).then(res => {
        this.loading = false
        this.torrents = res.rows
        this.$set(this.page, 'total', res.total)
      })
    },
    load() {
      this.handleCurrentChange()
    }
  },
  mounted() {
    this.load()
  }
};
</script>
<style scoped>
/deep/ .el-dialog__body img {
  max-width: 100% !important;
}

#app .hideSidebar .el-submenu>.el-submenu__title {
  padding: 0 20px !important;
}
</style>
<style scoped lang="scss">
.folder-box {
  display: flex;
  display: -webkit-flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-content: flex-start;
  align-items: center;
}

.folder-item {
  width: 250px;
  overflow: hidden;
  cursor: pointer;
  text-align: center;

  img {
    height: 250px;
  }
}

.folder-item:hover>span {
  color: #3A71A8;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
