<template>
  <div class="app-container">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="帖子标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入标题" />
      </el-form-item>

      <el-form-item label="帖子内容" prop="content">
        <!-- 富文本编辑器 -->
        <div class="editor-container">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="10"
            placeholder="请输入内容"
            class="editor-textarea"
          />
          <!-- 工具栏 -->
          <div class="editor-toolbar">
            <el-button-group>
              <el-button size="mini" @click="formatText('bold')">
                <i class="el-icon-edit"></i> 加粗
              </el-button>
              <el-button size="mini" @click="formatText('italic')">
                <i class="el-icon-edit-outline"></i> 斜体
              </el-button>
              <el-button size="mini" @click="formatText('underline')">
                <i class="el-icon-document"></i> 下划线
              </el-button>
              <el-button size="mini" @click="insertImage">
                <i class="el-icon-picture"></i> 图片
              </el-button>
              <el-button size="mini" @click="insertLink">
                <i class="el-icon-link"></i> 链接
              </el-button>
            </el-button-group>
          </div>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">发布</el-button>
        <el-button @click="close">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getForumPost, addForumPost, updateForumPost } from '@/api/galaxy/forum'

export default {
  name: 'PostEdit',
  data() {
    return {
      loading: false,
      form: {
        postId: undefined,
        title: '',
        content: ''
      },
      rules: {
        title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
        content: [{ required: true, message: '内容不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    // 检查登录状态
    if (!this.$store.state.user.token) {
      this.$message.warning('请先登录')
      this.$router.push('/login')
      return
    }

    // 如果是编辑，获取帖子数据
    if (this.$route.query.postId) {
      this.loading = true
      getForumPost(this.$route.query.postId).then(res => {
        this.form = res.data
      }).catch(error => {
        console.error('获取帖子失败:', error)
        this.$message.error('获取帖子失败')
      }).finally(() => {
        this.loading = false
      })
    }
  },
  methods: {
    /** 提交表单 */
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.loading = true
          const action = this.form.postId ?
            updateForumPost(this.form) :
            addForumPost(this.form)

          action.then(() => {
            this.$message.success(this.form.postId ? '修改成功' : '发布成功')
            this.close()
          }).catch(error => {
            console.error('操作失败:', error)
            this.$message.error('操作失败: ' + (error.message || '请检查权限或网络连接'))
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },

    /** 关闭页面 */
    close() {
      this.$router.push('/forum/post')
    },

    /** 文本格式化 */
    formatText(type) {
      const textarea = this.$el.querySelector('.editor-textarea textarea')
      const start = textarea.selectionStart
      const end = textarea.selectionEnd
      const selectedText = this.form.content.substring(start, end)

      let formattedText = ''
      switch (type) {
        case 'bold':
          formattedText = `**${selectedText}**`
          break
        case 'italic':
          formattedText = `*${selectedText}*`
          break
        case 'underline':
          formattedText = `__${selectedText}__`
          break
        default:
          formattedText = selectedText
      }

      this.form.content =
        this.form.content.substring(0, start) +
        formattedText +
        this.form.content.substring(end)

      // 恢复焦点
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start, start + formattedText.length)
      })
    },

    /** 插入图片 */
    insertImage() {
      const url = prompt('请输入图片URL:')
      if (url) {
        const imgTag = ``
        this.insertAtCursor(imgTag)
      }
    },

    /** 插入链接 */
    insertLink() {
      const url = prompt('请输入链接URL:')
      if (url) {
        const text = prompt('请输入链接文本:', '链接')
        const linkTag = `[${text}](${url})`
        this.insertAtCursor(linkTag)
      }
    },

    /** 在光标位置插入内容 */
    insertAtCursor(text) {
      const textarea = this.$el.querySelector('.editor-textarea textarea')
      const start = textarea.selectionStart
      const end = textarea.selectionEnd

      this.form.content =
        this.form.content.substring(0, start) +
        text +
        this.form.content.substring(end)

      // 恢复焦点并将光标放在插入内容之后
      this.$nextTick(() => {
        textarea.focus()
        textarea.setSelectionRange(start + text.length, start + text.length)
      })
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  max-width: 900px;
  margin: 20px auto;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: border-color 0.2s;
}

.editor-container:hover {
  border-color: #c0c4cc;
}

.editor-container:focus-within {
  border-color: #409eff;
  outline: 0;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.editor-textarea {
  border: none;
  border-bottom: 1px solid #ebeef5;
  border-radius: 4px 4px 0 0;
}

.editor-textarea textarea {
  border: none;
  padding: 15px;
  font-size: 14px;
  line-height: 1.6;
}

.editor-toolbar {
  padding: 8px 15px;
  background-color: #f5f7fa;
  border-radius: 0 0 4px 4px;
}

.el-button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.el-button--mini {
  padding: 5px 10px;
  font-size: 12px;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-form-item__label {
  font-weight: 600;
  color: #606266;
}
</style>
