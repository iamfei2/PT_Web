<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
<!--              <li class="list-group-item">-->
<!--                <svg-icon icon-class="phone" />手机号码-->
<!--                <div class="pull-right">{{ user.phonenumber }}</div>-->
<!--              </li>-->
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
<!--              <li class="list-group-item">-->
<!--                <svg-icon icon-class="tree" />所属分组-->
<!--                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div>-->
<!--              </li>-->
<!--              <li class="list-group-item">-->
<!--                <svg-icon icon-class="peoples" />所属角色-->
<!--                <div class="pull-right">{{ roleGroup }}</div>-->
<!--              </li>-->
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="download" />下载总量
                <div class="pull-right">{{ formatFileSize(user.downloaded) }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="upload" />上传总量
                <div class="pull-right">{{ formatFileSize(user.uploaded) }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="eye" />分享率
                <div class="pull-right" v-if="user.downloaded > 0 && user.uploaded > 0">{{ (user.uploaded / user.downloaded).toFixed(2) }}</div>
                <div class="pull-right" v-else>0</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="rate" />剩余积分
                <div class="pull-right">{{ user.points }}</div>
              </li>
              <li class="list-group-item" v-if="reward">
                <svg-icon icon-class="chart" />积分奖励
                <div class="pull-right">{{ reward.points_ph }}/小时(<el-button type="text" size="mini" @click="showRewardDetail">详情</el-button>)</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog title="奖励详情" :visible.sync="shouldShowDetail">
      <el-table :data="reward.peers" v-loading="loading">
        <el-table-column prop="torrent_title" label="种子标题"/>
        <el-table-column prop="torrent_size" label="文件大小" width="100">
          <template slot-scope="scope">{{ formatFileSize(scope.row.torrent_size) }}</template>
        </el-table-column>
        <el-table-column prop="torrent_points" label="积分奖励" width="100"/>
        <el-table-column prop="reward_rate" label="奖励倍率" width="100"/>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="shouldShowDetail = false">关 闭</el-button>
      </span>
    </el-dialog>
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
    showRewardDetail () {
      this.shouldShowDetail = true
      // let content = ''
      // this.reward.peers.forEach(x => {
      //   content += x.torrent_title + " 积分: <b>" + x.torrent_points + "</b> x " + x.reward_rate + "倍</br>"
      // })
      // this.$alert(content, '奖励明细', {
      //   dangerouslyUseHTMLString: true
      // });
    },
    getUser() {
      this.loading = true
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      }).then( res => {
        getReward().then(res => {
          this.reward = res
          this.loading = false
        })
      });
    }
  }
};
</script>
