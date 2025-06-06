<template>
  <div class="navbar">
    <div class="left-section">
      <breadcrumb id="breadcrumb-container" />
    </div>

    <div class="center-section">
      <template v-if="device !== 'mobile'">
        <div class="stats-panel">
          <div class="stat-item">
            <i class="el-icon-coin"></i>
            <div class="stat-info">
              <div class="stat-value">{{ points }}</div>
              <div class="stat-label">积分</div>
            </div>
          </div>
          <div class="stat-item">
            <i class="el-icon-download"></i>
            <div class="stat-info">
              <div class="stat-value">{{ formatFileSize(downloaded) }}</div>
              <div class="stat-label">下载量</div>
            </div>
          </div>
          <div class="stat-item">
            <i class="el-icon-upload2"></i>
            <div class="stat-info">
              <div class="stat-value">{{ formatFileSize(uploaded) }}</div>
              <div class="stat-label">上传量</div>
            </div>
          </div>
          <div class="stat-item">
            <i class="el-icon-odometer"></i>
            <div class="stat-info">
              <div class="stat-value">{{ (uploaded / downloaded).toFixed(2) }}</div>
              <div class="stat-label">分享率</div>
            </div>
          </div>
          <div class="activity-indicator" :class="activityStatus">
            <el-tooltip effect="dark" placement="top">
              <div slot="content" class="activity-tooltip">
                <div class="tooltip-title">用户活跃度状态</div>
                <div class="tooltip-status">当前状态：{{ activityTooltip }}</div>
                <div class="tooltip-ratio">当前分享率：{{ (uploaded / downloaded).toFixed(2) }}</div>
                <div class="tooltip-rules">
                  <div class="rules-title">考核规则：</div>
                  <div class="rule-item">
                    <span class="dot high"></span>
                    高活跃：分享率 > 1.5
                  </div>
                  <div class="rule-item">
                    <span class="dot normal"></span>
                    正常：0.5 ≤ 分享率 ≤ 1.5
                  </div>
                  <div class="rule-item">
                    <span class="dot low"></span>
                    低活跃：分享率 < 0.5 </div>
                  </div>
                </div>
                <div class="indicator-dot"></div>
            </el-tooltip>
          </div>
        </div>
      </template>
    </div>

    <div class="right-section">
      <!-- Ban Warning -->
      <el-tooltip v-if="banExpire" effect="dark" placement="bottom">
        <div slot="content" class="ban-tooltip">
          <div class="ban-title">账号已被封禁</div>
          <div class="ban-reason">原因：{{ banReason }}</div>
          <div class="ban-expire">解封时间：{{ banExpire }}</div>
        </div>
        <div class="warning-badge">
          <i class="el-icon-warning"></i>
        </div>
      </el-tooltip>

      <!-- User Menu -->
      <el-dropdown trigger="click" class="user-dropdown">
        <div class="user-info">
          <img :src="avatar" class="user-avatar">
          <div class="user-meta">
            <span class="username">{{ username }}</span>
            <i class="el-icon-caret-bottom"></i>
          </div>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
          <router-link to="/user/profile">
            <el-dropdown-item>
              <i class="el-icon-user"></i>个人中心
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <i class="el-icon-setting"></i>布局设置
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <i class="el-icon-switch-button"></i>退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'points',
      'uploaded',
      'downloaded',
      'banExpire',
      'banReason'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    activityStatus() {
      const ratio = this.uploaded / this.downloaded;
      if (ratio === 0 || !isFinite(ratio)) return 'neutral';
      if (ratio > 1.5) return 'high';
      if (ratio < 0.5) return 'low';
      return 'normal';
    },

    activityTooltip() {
      const statusMap = {
        high: '高活跃用户',
        normal: '正常用户',
        low: '低活跃用户',
        neutral: '未统计'
      };
      return statusMap[this.activityStatus];
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .left-section {
    min-width: 200px;
  }

  .center-section {
    flex: 1;
    display: flex;
    justify-content: center;

    .stats-panel {
      display: flex;
      gap: 32px;
      padding: 0 24px;
      background: #f8f9fa;
      border-radius: 8px;
      height: 44px;

      .stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 0;

        i {
          font-size: 24px;
          color: #409EFF;
        }

        .stat-info {
          .stat-value {
            font-size: 15px;
            font-weight: 500;
            color: #303133;
            line-height: 1.2;
          }

          .stat-label {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .right-section {
    display: flex;
    align-items: center;
    gap: 20px;

    .warning-badge {
      padding: 8px;
      border-radius: 50%;
      background: #fff0f0;

      i {
        font-size: 18px;
        color: #f56c6c;
      }
    }

    .user-dropdown {
      .user-info {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 4px;
        border-radius: 24px;
        transition: all 0.3s;

        &:hover {
          background: #f5f7fa;
        }

        .user-avatar {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          border: 2px solid #fff;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .user-meta {
          display: flex;
          align-items: center;
          gap: 4px;

          .username {
            font-size: 14px;
            font-weight: 500;
          }
        }
      }
    }
  }
}

.activity-indicator {
  display: flex;
  align-items: center;
  padding-left: 8px;

  .indicator-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    transition: background-color 0.3s ease;
    cursor: help;
  }

  &.high .indicator-dot {
    background-color: #67C23A;
  }

  &.normal .indicator-dot {
    background-color: #409EFF;
  }

  &.low .indicator-dot {
    background-color: #F56C6C;
  }

  &.neutral .indicator-dot {
    background-color: #909399;
  }
}

// Add styles for the tooltip content
:deep(.el-tooltip__popper) {
  .activity-tooltip {
    padding: 8px;
    min-width: 200px;

    .tooltip-title {
      font-size: 14px;
      font-weight: bold;
      margin-bottom: 8px;
      color: #fff;
    }

    .tooltip-status,
    .tooltip-ratio {
      font-size: 13px;
      margin-bottom: 6px;
      color: #fff;
    }

    .tooltip-rules {
      margin-top: 8px;
      border-top: 1px solid rgba(255, 255, 255, 0.2);
      padding-top: 8px;

      .rules-title {
        font-size: 13px;
        margin-bottom: 6px;
        color: #fff;
      }

      .rule-item {
        display: flex;
        align-items: center;
        font-size: 12px;
        margin-bottom: 4px;
        color: rgba(255, 255, 255, 0.9);

        .dot {
          width: 6px;
          height: 6px;
          border-radius: 50%;
          margin-right: 6px;

          &.high {
            background-color: #67C23A;
          }

          &.normal {
            background-color: #409EFF;
          }

          &.low {
            background-color: #F56C6C;
          }
        }
      }
    }
  }
}


.ban-tooltip {
  padding: 8px;

  .ban-title {
    font-weight: 500;
    margin-bottom: 4px;
  }

  .ban-reason,
  .ban-expire {
    font-size: 12px;
    color: #909399;
  }
}

.user-dropdown-menu {
  padding: 4px 0;
  min-width: 160px;

  .el-dropdown-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 16px;

    i {
      font-size: 16px;
    }
  }
}
</style>