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
        <div v-for="comment in comments.list" :key="comment.commentId" class="comment-item">
          <div class="comment-header">
            <el-avatar :src="comment.avatar || require('@/assets/images/profile.jpg')" size="small"></el-avatar>
            <span class="comment-author">{{ comment.userName }}</span>
            <span class="comment-time">{{ parseTime(comment.createTime) }}</span>
          </div>
          <div class="comment-content">
            {{ comment.content }}
          </div>
          <div class="comment-actions">
            <el-link type="primary" @click="replyComment(comment)" v-if="!comment.replying">回复</el-link>
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
                <el-avatar :src="reply.avatar || require('@/assets/images/profile.jpg')" size="small"></el-avatar>
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
import { getForumPost } from '@/api/galaxy/forum' // 路径已修正
import { listComments, addComment } from '@/api/galaxy/comment' // 使用统一的addComment

export default {
  name: 'PostView',
  data() {
    return {
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
      commentLoading: false
    }
  },
  created() {
    this.getPostDetail()
    this.getComments()
  },
  methods: {
    // 获取帖子详情
    getPostDetail() {
      getForumPost(this.postId).then(response => {
        this.post = response.data
      })
    },

    // 获取评论列表
    getComments() {
      listComments({
        postId: this.postId,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }).then(response => {
        this.comments.list = response.rows.map(c => ({ ...c, replying: false, replyContent: '' }))
        this.comments.total = response.total
      })
    },

    // 提交评论
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

    // 回复评论
    replyComment(comment) {
      this.comments.list.forEach(c => {
        c.replying = false
      })
      comment.replying = true
    },

    // 提交回复方法
    submitReply(comment) {
      if (!comment.replyContent.trim()) {
        this.$message.warning('回复内容不能为空')
        return
      }

      addComment({ // 使用统一的评论接口
        postId: this.postId,
        parentId: comment.commentId, // 添加父评论ID
        content: comment.replyContent
      }).then(() => {
        this.$message.success('回复成功')
        comment.replying = false
        comment.replyContent = ''
        this.getComments()
      })
    },

    // 取消回复
    cancelReply(comment) {
      comment.replying = false
      comment.replyContent = ''
    },

    // 辅助方法
    parseTime,
    formatFileSize(size) {
      if (size < 1024) return size + 'B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + 'KB'
      return (size / (1024 * 1024)).toFixed(1) + 'MB'
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

      .comment-author {
        font-weight: bold;
        margin: 0 10px;
      }

      .comment-time {
        color: #999;
        font-size: 12px;
      }
    }

    .comment-content {
      line-height: 1.6;
      margin-left: 35px;
    }

    .comment-actions {
      margin-top: 10px;
      margin-left: 35px;
    }

    .replies-list {
      margin-top: 15px;
      margin-left: 35px;
      border-left: 2px solid #eee;
      padding-left: 15px;

      .reply-item {
        padding: 10px 0;

        .reply-header {
          display: flex;
          align-items: center;

          .reply-author {
            font-weight: bold;
            margin: 0 10px;
            font-size: 14px;
          }

          .reply-time {
            color: #999;
            font-size: 12px;
          }
        }

        .reply-content {
          margin-left: 35px;
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
