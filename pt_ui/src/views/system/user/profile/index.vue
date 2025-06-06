<template>
  <div class="profile-container">
    <div class="profile-layout">
      <div class="profile-card">
        <div class="profile-header">
          <span class="profile-title">用户档案</span>
        </div>
        <div class="profile-content">
          <div class="avatar-container">
            <userAvatar :user="user" />
          </div>
          <ul class="profile-stats">
            <li class="stat-item">
              <i class="stat-icon">👤</i>
              <span class="stat-label">用户名</span>
              <span class="stat-value">{{ user.userName }}</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">✉️</i>
              <span class="stat-label">邮箱</span>
              <span class="stat-value">{{ user.email }}</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">📅</i>
              <span class="stat-label">注册时间</span>
              <span class="stat-value">{{ user.createTime }}</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">⬇️</i>
              <span class="stat-label">下载量</span>
              <span class="stat-value">{{ formatFileSize(user.downloaded) }}</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">⬆️</i>
              <span class="stat-label">上传量</span>
              <span class="stat-value">{{ formatFileSize(user.uploaded) }}</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">📊</i>
              <span class="stat-label">分享率</span>
              <span class="stat-value" v-if="user.downloaded > 0 && user.uploaded > 0">
                {{ (user.uploaded / user.downloaded).toFixed(2) }}
              </span>
              <span class="stat-value" v-else>0</span>
            </li>
            <li class="stat-item">
              <i class="stat-icon">⭐</i>
              <span class="stat-label">积分</span>
              <span class="stat-value">{{ user.points }}</span>
            </li>
            <li class="stat-item" v-if="reward">
              <i class="stat-icon">🎁</i>
              <span class="stat-label">奖励</span>
              <span class="stat-value">
                {{ reward.points_ph }}/小时
              </span>
            </li>
          </ul>
        </div>
      </div>

      <div class="profile-card">
        <div class="profile-header">
          <span class="profile-title">账户设置</span>
        </div>
        <div class="profile-tabs">
          <div class="tab-buttons">
            <button 
              :class="['tab-btn', { active: activeTab === 'userinfo' }]"
              @click="activeTab = 'userinfo'">
              基本信息
            </button>
            <button 
              :class="['tab-btn', { active: activeTab === 'resetPwd' }]"
              @click="activeTab = 'resetPwd'">
              修改密码
            </button>
          </div>
          
          <div class="tab-content">
            <userInfo :user="user" v-if="activeTab === 'userinfo'" />
            <resetPwd :user="user" v-if="activeTab === 'resetPwd'" />
          </div>
        </div>
      </div>
    </div>

    <div class="reward-modal" v-if="shouldShowDetail">
      <div class="modal-content">
        <div class="modal-header">
          <h3>奖励详情</h3>
          <button class="close-btn" @click="shouldShowDetail = false">×</button>
        </div>
        <div class="modal-body">
          <div class="reward-grid">
            <div class="grid-header">
              <div class="grid-cell">种子标题</div>
              <div class="grid-cell">文件大小</div>
              <div class="grid-cell">积分奖励</div>
              <div class="grid-cell">奖励倍率</div>
            </div>
            <div class="grid-row" v-for="(peer, index) in reward.peers" :key="index">
              <div class="grid-cell">{{ peer.torrent_title }}</div>
              <div class="grid-cell">{{ formatFileSize(peer.torrent_size) }}</div>
              <div class="grid-cell">{{ peer.torrent_points }}</div>
              <div class="grid-cell">{{ peer.reward_rate }}</div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="action-btn" @click="shouldShowDetail = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";
import { getReward } from "@/api/galaxy/piazza";

export default {
  name: "Profile",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      loading: false,
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      reward: {
        peers: []
      },
      shouldShowDetail: false
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    showRewardDetail() {
      this.shouldShowDetail = true;
    },
    getUser() {
      this.loading = true;
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      }).then(() => {
        getReward().then(res => {
          this.reward = res;
          this.loading = false;
        });
      });
    },
    formatFileSize(size) {
      // 保留原有的文件大小格式化逻辑
      if (size < 1024) return size + ' B';
      else if (size < 1048576) return (size / 1024).toFixed(2) + ' KB';
      else if (size < 1073741824) return (size / 1048576).toFixed(2) + ' MB';
      else return (size / 1073741824).toFixed(2) + ' GB';
    }
  }
};
</script>

<style scoped>
/* 整体布局 */
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.profile-layout {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 25px;
}

/* 卡片样式 */
.profile-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-5px);
}

.profile-header {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  padding: 18px 24px;
}

.profile-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: white;
  letter-spacing: 0.5px;
}

.profile-content {
  padding: 20px;
}

/* 头像区域 */
.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

/* 统计信息 */
.profile-stats {
  list-style: none;
  padding: 0;
  margin: 0;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 14px 10px;
  border-bottom: 1px solid #f0f3ff;
  transition: background 0.2s;
}

.stat-item:hover {
  background-color: #f8faff;
  border-radius: 8px;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-icon {
  font-size: 1.4rem;
  margin-right: 15px;
  width: 36px;
  text-align: center;
}

.stat-label {
  flex: 1;
  font-weight: 500;
  color: #4a5568;
}

.stat-value {
  font-weight: 600;
  color: #2d3748;
  min-width: 120px;
  text-align: right;
}

/* 标签页 */
.profile-tabs {
  padding: 15px;
}

.tab-buttons {
  display: flex;
  border-bottom: 2px solid #edf2f7;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 12px 24px;
  background: none;
  border: none;
  font-size: 1rem;
  font-weight: 500;
  color: #718096;
  cursor: pointer;
  position: relative;
  transition: all 0.3s;
}

.tab-btn:hover {
  color: #4299e1;
}

.tab-btn.active {
  color: #2b6cb0;
  font-weight: 600;
}

.tab-btn.active::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 3px;
  background: #4299e1;
  border-radius: 3px 3px 0 0;
}

.tab-content {
  padding: 15px 0;
}

/* 按钮样式 */
.detail-btn {
  background: #4299e1;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 4px 10px;
  font-size: 0.85rem;
  cursor: pointer;
  margin-left: 10px;
  transition: background 0.2s;
}

.detail-btn:hover {
  background: #3182ce;
}

/* 奖励弹窗 */
.reward-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: #2d3748;
  color: white;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.4rem;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.8rem;
  cursor: pointer;
  line-height: 1;
  padding: 0 10px;
}

.modal-body {
  padding: 20px;
  max-height: 60vh;
  overflow-y: auto;
}

.modal-footer {
  padding: 15px 24px;
  text-align: right;
  border-top: 1px solid #e2e8f0;
}

.action-btn {
  background: #4299e1;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.action-btn:hover {
  background: #3182ce;
}

/* 网格布局 */
.reward-grid {
  display: grid;
  grid-template-columns: 3fr 1fr 1fr 1fr;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
}

.grid-header {
  display: contents;
}

.grid-header > .grid-cell {
  background: #edf2f7;
  font-weight: 600;
  padding: 14px 15px;
  border-bottom: 1px solid #e2e8f0;
}

.grid-row {
  display: contents;
}

.grid-row:nth-child(even) > .grid-cell {
  background: #f8fafc;
}

.grid-cell {
  padding: 12px 15px;
  border-bottom: 1px solid #edf2f7;
}

.grid-row:last-child > .grid-cell {
  border-bottom: none;
}
</style>