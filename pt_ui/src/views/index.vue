<template>
  <div class="app-container home">
    <div class="main-content">
      <!-- 顶部分类菜单与搜索区域 -->
      <div class="top-section">
        <div class="menu-container">
          <el-menu 
            :default-active="menuIndex" 
            mode="horizontal" 
            background-color="#304156"
            text-color="#fff" 
            active-text-color="#ffd04b" 
            @select="handleMenuSelect"
            class="category-menu"
          >
            <el-menu-item index="0">全部</el-menu-item>
            <el-submenu 
              v-for="(v, k) in categoryTree" 
              :key="'parent_' + k" 
              :index="v.id + ''"
            >
              <template slot="title">{{ v.title }}</template>
              <div v-for="(v1, k1) in v.children" :key="'parent_sub_' + k1">
                <el-submenu v-if="v1.children.length > 0" :index="v1.id + ''">
                  <template slot="title">{{ v1.title }}</template>
                  <el-menu-item 
                    v-for="(v2, k2) in v1.children" 
                    :key="'menu_item_' + k2" 
                    :index="v2.id + ''"
                  >
                    {{ v2.title }}
                  </el-menu-item>
                </el-submenu>
                <el-menu-item v-else :index="v1.id + ''">{{ v1.title }}</el-menu-item>
              </div>
            </el-submenu>
          </el-menu>
        </div>
        
        <!-- 搜索框单独一行，更突出 -->
        <div class="search-container">
          <el-input
            placeholder="搜索种子内容"
            prefix-icon="el-icon-search"
            v-model="queryParams.title"
            @keyup.enter.native="handleSearch"
          >
            <el-button slot="append" icon="el-icon-search" type="primary" @click="handleSearch"></el-button>
          </el-input>
        </div>
      </div>
      
      <!-- 公告区域独立卡片 -->
      <div class="notification-card">
        <el-card shadow="never">
          <div class="notification-header">
            <i class="el-icon-bell"></i>
            <span>最新公告</span>
          </div>
          <el-carousel 
            height="32px" 
            indicator-position="none" 
            arrow="never"
            :autoplay="true"
          >
            <el-carousel-item 
              v-for="(v, k) in notices" 
              :key="'notice_' + k" 
              class="notification-item"
              @click.native="showNotice(v)"
            >
              <div class="notification-content">
                <span class="notification-title">{{ v.noticeTitle }}</span>
                <span class="notification-time">{{ parseTime(v.createTime) }}</span>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-card>
      </div>
    </div>
    
    <!-- 种子列表区域 -->
    <div class="content-section">
      <torrent-list ref="torrentList" class="torrent-list-container"/>
    </div>
    
    <!-- 公告弹窗（保持不变） -->
    <el-dialog 
      :title="notice ? notice.noticeTitle : '公告'" 
      :visible.sync="shouldShowNoticeDialog" 
      width="80%"
    >
      <div v-html="notice.noticeContent" v-if="notice && shouldShowNoticeDialog"></div>
    </el-dialog>
  </div>
</template>

<script>
import TorrentList from "@/components/TorrentList"
import { listCategory } from "@/api/galaxy/category";
import { listTorrents, getNotice } from "@/api/galaxy/piazza"
import { parseTime } from '@/utils/ruoyi'

export default {
  name: "index",
  components: {
    TorrentList
  },
  data() {
    return {
      parseTime, // 添加时间格式化工具
      notice: null,
      menuIndex: null,
      queryParams: {},
      noticeQuery: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      categories: null,
      categoryTree: null,
      notices: null,
      shouldShowNoticeDialog: false,
      loading: false
    };
  },
  methods: {
    handleCurrentChange () {
        this.$refs.torrentList.queryParams = this.queryParams
        this.$refs.torrentList.handleCurrentChange()
      },
    handleSearch () {
        this.queryParams.pageNum = 1
        this.handleCurrentChange()
      },
    showNotice (notice) {
        this.notice = notice
        this.shouldShowNoticeDialog = true
      },
    loadNotice () {
      getNotice(this.noticeQuery).then( res => {
        this.notices = res.rows
      })
    },
    handleMenuSelect (index) {
      if (parseInt(index) === 0) {
        index = null
      }
      this.queryParams.categories = index
      this.handleCurrentChange()
    },
    load () {
      listCategory().then(res => {
        this.categories = res.data
        this.categoryTree = this.handleTree(res.data)
      }).then(() => {
        this.handleCurrentChange()
      })
      this.loadNotice()
    }
  },
  created() {
    this.load();
  }
};
</script>

<style scoped lang="scss">
.app-container.home {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 15px;
  background-color: #f5f7fa;
  
  .main-content {
    flex: 0 0 auto;
    
    .top-section {
      display: flex;
      flex-direction: column;
      margin-bottom: 15px;
      
      .menu-container {
        background-color: #304156;
        border-radius: 8px;
        overflow: hidden;
        margin-bottom: 12px;
        
        .category-menu {
          border: none;
        }
      }
      
      .search-container {
        margin-bottom: 12px;
        display: flex;
        justify-content: center;
        
        .el-input {
          width: 100%;
          max-width: 600px;
          
          ::v-deep .el-input-group__append {
            background-color: #409EFF;
            border-color: #409EFF;
            color: white;
          }
        }
      }
    }
    
    .notification-card {
      margin-bottom: 15px;
      
      .el-card {
        border-radius: 8px;
        border: none;
        
        .notification-header {
          display: flex;
          align-items: center;
          padding-bottom: 10px;
          border-bottom: 1px solid #eee;
          margin-bottom: 5px;
          
          i {
            color: #e6a23c;
            font-size: 18px;
            margin-right: 8px;
          }
          
          span {
            font-weight: bold;
            color: #303133;
          }
        }
        
        .notification-item {
          cursor: pointer;
          display: flex;
          align-items: center;
          
          .notification-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            padding: 0 10px;
            
            .notification-title {
              font-size: 14px;
              color: #606266;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              flex: 1;
            }
            
            .notification-time {
              font-size: 12px;
              color: #909399;
              margin-left: 15px;
              white-space: nowrap;
            }
          }
          
          &:hover {
            background-color: #ecf5ff;
            
            .notification-title {
              color: #409EFF;
            }
          }
        }
      }
    }
  }
  
  .content-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    
    .torrent-list-container {
      flex: 1;
      overflow: hidden;
      border-radius: 8px;
      background: white;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    }
  }
}

@media (max-width: 768px) {
  .app-container.home {
    padding: 10px;
    
    .main-content {
      .notification-card {
        .notification-content {
          flex-direction: column;
          align-items: flex-start !important;
          
          .notification-time {
            margin-left: 0 !important;
            margin-top: 3px;
          }
        }
      }
      
      .search-container {
        .el-input {
          max-width: 100%;
        }
      }
    }
  }
}
</style>