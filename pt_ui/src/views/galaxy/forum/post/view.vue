<template>
  <div class="app-container">
    <el-card shadow="hover" class="post-detail-card">
      <!-- 帖子标题 -->
      <div class="post-header">
        <h1>{{ post.title }}</h1>
        <div class="post-meta">
          <span class="author">{{ post.userName }}</span>
          <span class="time">{{ parseTime(post.createTime) }}</span>
          <el-tag size="mini" type="info">{{ post.viewCount }} 浏览</el-tag>
        </div>
      </div>

      <!-- 帖子内容 -->
      <div class="post-content">
        <div v-html="post.content"></div>
      </div>

      <!-- 附件区域 -->
      <div v-if="post.attachments && post.attachments.length" class="post-attachments">
        <h3>附件</h3>
        <ul>
          <li v-for="(file, index) in post.attachments" :key="index">
            <el-link :href="file.url" :download="file.name">
              <i class="el-icon-document"></i> {{ file.name }} ({{ formatFileSize(file.size) }})
            </el-link>
          </li>
        </ul>
      </div>
    </el-card>

    <!-- 评论区域 -->
    <div class="comment-section">
      <h2>评论 ({{ comments.total }})</h2>

      <!-- 发表评论 -->
      <div class="new-comment">
        <el-input
          type="textarea"
          :rows="3"
          v-model="newComment.content"
          placeholder="请输入您的评论..."
          resize="none"
        ></el-input>
        <div class="comment-actions">
          <el-button type="primary" size="small" @click="submitComment" :loading="commentLoading">
            发表评论
          </el-button>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comment-list">
        <div v-for="(comment, index) in comments.list" :key="index" class="comment-item">
          <!-- 添加调试信息 -->
          <div v-if="!comment.commentId" style="color:red">
            警告：评论ID缺失！对象: {{ JSON.stringify(comment) }}
          </div>
          <div class="comment-header">
            <!-- 替换头像为图片元素 -->

            <span class="comment-author">{{ comment.userName }}</span>
            <span class="comment-time">{{ parseTime(comment.createTime) }}</span>
          </div>
          <div class="comment-content">
            {{ comment.content }}
          </div>
          <div class="comment-actions">
            <el-link type="primary" @click="replyComment(comment)" v-if="!comment.replying">
              <i class="el-icon-edit"></i> 回复
            </el-link>
            <el-link type="primary" @click="deleteComment(comment)">
              <i class="el-icon-delete"></i> 删除
            </el-link>
            <div v-if="comment.replying" class="reply-box">
              <el-input
                v-model="comment.replyContent"
                placeholder="回复..."
                size="mini"
                class="reply-input"
              ></el-input>
              <el-button size="mini" @click="submitReply(comment)">发送</el-button>
              <el-button size="mini" @click="cancelReply(comment)">取消</el-button>
            </div>
          </div>

          <!-- 回复列表 -->
          <div v-if="comment.replies && comment.replies.length" class="replies-list">
            <div v-for="reply in comment.replies" :key="reply.replyId" class="reply-item">
              <div class="reply-header">
                <!-- 替换头像为图片元素 -->

                <span class="reply-author">{{ reply.userName }}</span>
                <span class="reply-time">{{ parseTime(reply.createTime) }}</span>
              </div>
              <div class="reply-content">
                {{ reply.content }}
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <pagination
          v-show="comments.total > 0"
          :total="comments.total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getComments"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { getForumPost } from '@/api/galaxy/forum'
import { parseTime } from '@/utils/ruoyi' // 确保引入 parseTime
import { listComments, addComment, delComment } from '@/api/galaxy/comment'

export default {
  name: 'PostView',
  data() {
    return {
      isAdmin: false, // 添加管理员状态
      postId: this.$route.params.postId,
      post: {},
      comments: {
        list: [],
        total: 0
      },
      newComment: {
        content: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      commentLoading: false,
      loading: false,
      errorMessage: '',
      currentUserId: null // 添加当前用户ID
    }
  },
  created() {
    this.currentUserId = this.$store.state.user.userId;
    if (!this.$route.params.postId) {
      this.$notify.error({
        title: '错误',
        message: '无效的帖子ID参数'
      })
      this.$router.replace('/')
      return
    }

    this.loading = true
    this.getPostDetail()
    this.getComments()
    // 添加管理员检查
    const userRoles = this.$store.state.user.roles || [];
    this.isAdmin = userRoles.some(role =>
      role.toLowerCase().includes('admin')
    );
  },
  methods: {
    getPostDetail() {
      getForumPost(this.postId).then(response => {
        this.post = response.data
      }).catch(error => {
        console.error('API错误:', error)
        this.errorMessage = '加载帖子详情失败'
      }).finally(() => {
        this.loading = false
      })
    },

    getComments() {
      listComments({
        postId: this.postId,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }).then(response => {
        this.comments.list = response.rows.map(c => {
          // 调试日志：检查数据有效性
          if(!c.commentId || !c.userId) {
            console.error('无效评论数据:', c);
          }

          // 添加默认值保护
          return {
            commentId: c.commentId || 0,
            userId: c.userId || 0,
            userName: c.userName || '未知用户',
            content: c.content || '',
            createTime: c.createTime || new Date(),
            // 其他必要字段...
            replying: false,
            replyContent: ''
          };
        });
        this.comments.total = response.total;
      }).catch(error => {
        console.error('加载评论失败:', error);
        this.$message.error('加载评论失败');
      });
    },

    submitComment() {
      if (!this.newComment.content.trim()) {
        this.$message.warning('评论内容不能为空')
        return
      }

      this.commentLoading = true
      addComment({
        postId: this.postId,
        content: this.newComment.content
      }).then(() => {
        this.$message.success('评论成功')
        this.newComment.content = ''
        this.getComments()
      }).finally(() => {
        this.commentLoading = false
      })
    },

    replyComment(comment) {
      this.comments.list.forEach(c => {
        c.replying = false
      })
      comment.replying = true
    },

    submitReply(comment) {
      if (!comment.replyContent.trim()) {
        this.$message.warning('回复内容不能为空')
        return
      }

      addComment({
        postId: this.postId,
        parentId: comment.commentId,
        content: comment.replyContent
      }).then(() => {
        this.$message.success('回复成功')
        comment.replying = false
        comment.replyContent = ''
        this.getComments()
      })
    },

    cancelReply(comment) {
      comment.replying = false
      comment.replyContent = ''
    },

    parseTime, // 使用导入的 parseTime 方法
    formatFileSize(size) {
      if (size < 1024) return size + 'B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
      return (size / (1024 * 1024)).toFixed(1) + 'MB'
    },

    // 删除评论方法
    deleteComment(comment) {
      const commentId = comment.commentId || comment.id || 0;

      if (!commentId || commentId === 0) {
        this.$message.error('无法删除：无效的评论ID');
        console.error('无效的评论对象:', comment);
        return;
      }

      this.$confirm('确定删除该评论吗?', '提示').then(() => {
        delComment(comment.commentId).then(() => {
          // 直接在前端删除评论项，无需刷新整个页面
          const index = this.comments.list.findIndex(c => c.commentId === comment.commentId);
          if (index !== -1) {
            this.$delete(this.comments.list, index); // 确保响应式删除
            this.comments.total -= 1; // 更新评论总数
            this.$message.success('删除成功');

            // 同时更新帖子模型中的评论计数
            if (this.post && this.post.commentCount) {
              this.post.commentCount -= 1;
            }
          }
        }).catch(error => {
          this.$message.error('删除失败: ' + (error.message || '未知错误'));
        });
      })
        .catch(() => { /* 取消操作 */ });
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.post-detail-card {
  margin-bottom: 30px;

  .post-header {
    padding-bottom: 15px;
    border-bottom: 1px solid #eee;
    margin-bottom: 20px;

    h1 {
      font-size: 24px;
      margin-bottom: 10px;
    }

    .post-meta {
      font-size: 14px;
      color: #666;

      span {
        margin-right: 15px;
      }
    }
  }

  .post-content {
    line-height: 1.8;
    font-size: 16px;
  }

  .post-attachments {
    margin-top: 30px;
    padding-top: 15px;
    border-top: 1px solid #eee;

    h3 {
      font-size: 18px;
      margin-bottom: 10px;
    }

    li {
      margin-bottom: 8px;
    }
  }
}

.comment-section {
  h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }

  .new-comment {
    margin-bottom: 30px;

    .comment-actions {
      margin-top: 10px;
      text-align: right;
    }
  }

  .comment-item {
    padding: 15px;
    border-bottom: 1px solid #eee;

    .comment-header {
      display: flex;
      align-items: center;
      margin-bottom: 10px;

      .avatar-img {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        object-fit: cover;
        margin-right: 10px;
      }

      .comment-author {
        font-weight: bold;
        margin-right: 10px;
      }

      .comment-time {
        color: #999;
        font-size: 12px;
      }
    }

    .comment-content {
      line-height: 1.6;
      margin-left: 42px; /* 调整左边距 */
    }

    .comment-actions {
      margin-top: 10px;
      margin-left: 42px; /* 调整左边距 */
    }

    .replies-list {
      margin-top: 15px;
      margin-left: 42px; /* 调整左边距 */
      border-left: 2px solid #eee;
      padding-left: 15px;

      .reply-item {
        padding: 10px 0;

        .reply-header {
          display: flex;
          align-items: center;

          .avatar-img {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 8px;
          }

          .reply-author {
            font-weight: bold;
            margin-right: 10px;
            font-size: 14px;
          }

          .reply-time {
            color: #999;
            font-size: 12px;
          }
        }

        .reply-content {
          margin-left: 36px; /* 调整左边距 */
          font-size: 14px;
        }
      }
    }

    .reply-box {
      margin-top: 10px;
      display: flex;

      .reply-input {
        flex: 1;
        margin-right: 10px;
      }
    }
  }
}
</style>
