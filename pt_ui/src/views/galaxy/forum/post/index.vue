<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 搜索区域 -->
      <el-col :span="8">
        <el-input
          v-model="queryParams.title"
          placeholder="搜索帖子标题"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleQuery" />
        </el-input>
      </el-col>

      <!-- 操作按钮 -->
      <el-col :span="16" class="text-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          发表新帖
        </el-button>
      </el-col>
    </el-row>

    <!-- 帖子列表 -->
    <el-table v-loading="loading" :data="postList">
      <el-table-column label="ID" prop="postId" width="80" />
      <el-table-column label="标题" prop="title" min-width="200" />
      <el-table-column label="作者" prop="userId" width="120" />
      <el-table-column label="发布时间" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <!-- 仅显示编辑按钮，如果当前用户是作者或管理员 -->
          <el-button
            v-if="canEdit(scope.row)"
            size="small"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>

          <!-- 删除按钮 -->
          <el-button
            size="small"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listForumPost, delForumPost } from '@/api/galaxy/forum'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  data() {
    return {
      loading: true,
      postList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    /** 获取帖子列表 */
    getList() {
      this.loading = true
      listForumPost(this.queryParams).then(res => {
        this.postList = res.rows
        this.total = res.total
        this.loading = false
      }).catch(error => {
        console.error("获取帖子列表失败:", error)
        this.loading = false
        this.$message.error("获取数据失败")
      })
    },

    /** 搜索按钮 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 新增按钮 */
    handleAdd() {
      this.$router.push({ path: '/forum/post/edit' })
    },

    /** 编辑按钮 */
    handleUpdate(row) {
      this.$router.push({
        path: '/forum/post/edit',
        query: { postId: row.postId }
      })
    },

    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm(`确认删除帖子 "${row.title}"?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delForumPost(row.postId).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 检查编辑权限
    canEdit(post) {
      const currentUser = this.$store.state.user;
      return currentUser.userId === post.userId || currentUser.roles.includes('admin');
    },

    handleView(row) {
      this.$router.push({
        path: '/forum/post/view/' + row.postId
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>
