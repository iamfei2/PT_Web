<template>
  <div class="app-container home">
    <div class="main-content">
      <!-- 顶部分类菜单与搜索区域 -->
      <div class="top-section">
        <div class="menu-container">
          <el-menu :default-active="menuIndex" mode="horizontal" background-color="#ff8800" text-color="#fff"
            active-text-color="#ffd04b" @select="handleMenuSelect" class="category-menu">
            <el-menu-item index="0">全部</el-menu-item>
            <el-submenu v-for="(v, k) in categoryTree" :key="'parent_' + k" :index="v.id + ''">
              <template slot="title">{{ v.title }}</template>
              <div v-for="(v1, k1) in v.children" :key="'parent_sub_' + k1">
                <el-submenu v-if="v1.children.length > 0" :index="v1.id + ''">
                  <template slot="title">{{ v1.title }}</template>
                  <el-menu-item v-for="(v2, k2) in v1.children" :key="'menu_item_' + k2" :index="v2.id + ''">
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
          <el-input placeholder="搜索种子内容" prefix-icon="el-icon-search" v-model="queryParams.title"
            @keyup.enter.native="handleSearch">
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
          <el-carousel height="32px" indicator-position="bottom" arrow="never" :autoplay="true" :interval="5000">
            <el-carousel-item v-for="(v, k) in notices" :key="'notice_' + k" class="notification-item"
              @click.native="showNotice(v)">
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
      <torrent-list ref="torrentList" class="torrent-list-container" />
    </div>

    <!-- 公告弹窗（保持不变） -->
    <el-dialog :title="notice ? notice.noticeTitle : '公告'" :visible.sync="shouldShowNoticeDialog" width="80%">
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
    handleCurrentChange() {
      this.$refs.torrentList.queryParams = this.queryParams
      this.$refs.torrentList.handleCurrentChange()
    },
    handleSearch() {
      this.queryParams.pageNum = 1
      this.handleCurrentChange()
    },
    showNotice(notice) {
      this.notice = notice
      this.shouldShowNoticeDialog = true
    },
    loadNotice() {
      // 模拟多条公告数据
      const mockNotices = [
        {
          noticeTitle: "重要通知：系统维护",
          createTime: new Date().getTime() - 3600000, // 1小时前
          noticeContent: "<p>系统将于今晚22:00-24:00进行例行维护，请各位用户注意。</p>"
        },
        {
          noticeTitle: "新功能上线：种子评分",
          createTime: new Date().getTime() - 86400000, // 1天前
          noticeContent: "<p>我们新增了种子评分功能，欢迎大家使用并提供反馈。</p>"
        },
        {
          noticeTitle: "社区规则更新",
          createTime: new Date().getTime() - 259200000, // 3天前
          noticeContent: "<p>请所有用户注意查看更新后的社区规则，以免违规。</p>"
        }
      ];

      // 使用模拟数据替代 API 调用
      this.notices = mockNotices;

      // 如果你仍然需要调用 API，可以保留原来的代码
      // getNotice(this.noticeQuery).then(res => {
      //   this.notices = res.rows;
      // });
    },
    handleMenuSelect(index) {
      if (parseInt(index) === 0) {
        index = null
      }
      this.queryParams.categories = index
      this.handleCurrentChange()
    },
    load() {
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
  background-color: #f0f2f5; // 修改背景颜色
  padding: 20px; // 增加内边距

  .main-content {
    .top-section {
      margin-bottom: 20px; // 增加底部外边距

      .menu-container {
        background-color: #20d213;
        border-radius: 6px; // 修改圆角
        overflow: hidden;
        margin-bottom: 15px; // 增加底部外边距

        .category-menu {
          border: none;
          background-color: transparent; // 去除菜单背景色

          .el-menu-item,
          .el-submenu__title {
            height: 50px; // 增加菜单项高度
            line-height: 50px; // 增加菜单项行高
            color: #c9d1d9; // 修改菜单项文本颜色
            font-size: 14px; // 修改菜单项文本大小

            &.is-active {
              color: #fff; // 修改激活状态文本颜色
              background-color: rgba(255, 255, 255, 0.2); // 添加激活状态背景色
            }
          }

          .el-submenu {
            .el-menu {
              background-color: #ff8800; // 修改子菜单背景色
              border-radius: 4px; // 添加子菜单圆角
              padding: 6px 0; // 添加子菜单内边距SS

              .el-menu-item {
                height: 36px;
                line-height: 36px;
                padding: 0 30px;
                color: #c9d1d9; // 修改子菜单项文本颜色

                &:hover,
                &.is-active {
                  background-color: rgba(255, 255, 255, 0.1); // 修改子菜单项悬停和激活状态背景色
                  color: #fff; // 修改子菜单项悬停和激活状态文本颜色
                }
              }
            }
          }
        }
      }

      .search-container {
        margin-bottom: 15px;

        .el-input {
          ::v-deep .el-input__inner {
            border-radius: 20px; // 修改输入框圆角
            padding-left: 15px; // 增加输入框内边距
            padding-right: 15px;
          }
        }
      }
    }

    .notification-card {
      margin-bottom: 20px; // 增加底部外边距

      .el-card {
        border-radius: 6px; // 修改卡片圆角
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05); // 添加卡片阴影

        .notification-header {
          padding-bottom: 8px;
          border-bottom: 1px solid #e4e7ed; // 修改分割线颜色
          margin-bottom: 10px;

          i {
            color: #f56c6c; // 修改图标颜色
          }

          span {
            color: #383056; // 修改标题颜色
          }
        }

        .notification-item {
          .notification-content {
            padding: 8px 15px; // 增加内边距

            .notification-title {
              font-size: 13px; // 修改标题字体大小
            }

            .notification-time {
              font-size: 12px;
            }
          }

          &:hover {
            background-color: #f0f2f5; // 修改悬停背景色

            .notification-title {
              color: #383056; // 修改悬停标题颜色
            }
          }
        }
      }
    }
  }

  .content-section {
    .torrent-list-container {
      border-radius: 6px;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05); // 添加阴影
    }
  }
}

@media (max-width: 768px) {
  .app-container.home {
    padding: 15px; // 修改移动端内边距
  }
}
</style>