<template>
  <div class="app-container">
    <el-form v-if="!isReview" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch"
      label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="目录" prop="categories">
        <el-input v-model="queryParams.categories" placeholder="请输入目录" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
            :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-if="!isReview">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['galaxy:torrent:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['galaxy:torrent:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['galaxy:torrent:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['galaxy:torrent:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="已审核" name="reviewed">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in reviewedTorrentList" :key="item.id">
            <el-card class="torrent-card" shadow="hover">
              <div slot="header" class="clearfix">
                <span class="torrent-title">{{ item.title }}</span>
              </div>
              <div class="torrent-info">
                <p><i class="el-icon-folder"></i> {{ item.categoryName }}</p>
                <p><i class="el-icon-document"></i> {{ formatFileSize(item.fileSize) }}</p>
                <p><i class="el-icon-share"></i> {{ item.downloaded > 0 && item.uploaded ? (item.uploaded /
                  item.downloaded).toFixed(2) : 0 }}</p>
                <p><i class="el-icon-info"></i> {{ statusFormat(item) }}</p>
              </div>
              <div class="torrent-operations">
                <el-button size="mini" type="text" @click="handleUpdate(item)"
                  v-hasPermi="['galaxy:torrent:edit']">修改</el-button>
                <el-button size="mini" type="text" @click="handleDelete(item)"
                  v-hasPermi="['galaxy:torrent:remove']">删除</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="未审核" name="unreviewed">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in unreviewedTorrentList" :key="item.id">
            <el-card class="torrent-card" shadow="hover">
              <div slot="header" class="clearfix">
                <span class="torrent-title">{{ item.title }}</span>
              </div>
              <div class="torrent-info">
                <p><i class="el-icon-folder"></i> {{ item.categoryName }}</p>
                <p><i class="el-icon-document"></i> {{ formatFileSize(item.fileSize) }}</p>
                <p><i class="el-icon-share"></i> {{ item.downloaded > 0 && item.uploaded ? (item.uploaded /
                  item.downloaded).toFixed(2) : 0 }}</p>
                <p><i class="el-icon-info"></i> {{ statusFormat(item) }}</p>
              </div>
              <div class="torrent-operations">
                <el-button size="mini" type="text" @click="handleVerify(item)"
                  v-hasPermi="['galaxy:torrent:verify']">审核</el-button>
                <el-button size="mini" type="text" @click="handleUpdate(item)"
                  v-hasPermi="['galaxy:torrent:edit']">修改</el-button>
                <el-button size="mini" type="text" @click="handleDelete(item)"
                  v-hasPermi="['galaxy:torrent:remove']">删除</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :visible.sync="showVerify" width="500px" append-to-body title="审核">
      <el-form v-if="verifyRow">
        <el-form-item label="审核结果">
          <el-select v-model="verifyRow.status" placeholder="请选择审核状态" clearable size="small">
            <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel"
              :value="dict.dictValue" :disabled="dict.dictValue === '0'" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注内容">
          <el-input type="textarea" v-model="verifyRow.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVerify">提 交</el-button>
        <el-button @click="cancelVerify">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="open" width="80%" fullscreen custom-class="torrent-dialog" :show-close="false">
      <div class="torrent-form-container">
        <h2 class="form-title">{{ title }}</h2>
        <el-form ref="form" :model="form" :rules="rules" label-position="top" :disabled="isReview" class="torrent-form">
          <el-row :gutter="30">
            <el-col :span="12">
              <el-form-item label="目录" prop="categories">
                <el-cascader v-model="form.categories" :options="categories" @change="handleCategoryChanged"></el-cascader>
              </el-form-item>
              <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" placeholder="请输入标题" />
              </el-form-item>
              <el-form-item label="种子文件" v-if="!isReview">
                <fileUpload v-model="form.fileName" :file-type="['torrent']" url="/galaxy/torrent/upload" @success="handleTorrentUploaded" />
              </el-form-item>
              <el-form-item label="附加种子" v-if="!isReview">
                <torrent-uploader :limit="0" v-model="form.attachment" :file-type="['torrent']" :token="form.remark" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="标签">
                <el-select v-model="form.tags" multiple filterable allow-create default-first-option placeholder="请输入种子标签">
                  <el-option v-for="item in tagList" :key="item.id" :label="item.tag" :value="item.tag">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="Tracker" v-if="torrent && !isReview">
                <el-input v-model="torrent.announce" readonly />
              </el-form-item>
              <el-form-item label="发布状态" v-has-role="['admin', 'user_plus']" v-if="!isReview">
                <el-radio-group v-model="form.status">
                  <el-radio label="0">审核后发布</el-radio>
                  <el-radio label="1">直接发布</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="种子信息" v-show="torrent">
            <torrent-viewer :torrent="torrent" />
          </el-form-item>

          <el-form-item label="重复文件" v-if="duplicate && duplicate.length > 0">
            <div class="duplicate-files">
              <el-tag v-for="(v, k) in duplicate" :key="'duplicate_' + k" type="danger">{{ v.fileName }}</el-tag>
            </div>
          </el-form-item>

          <el-form-item label="详情">
            <editor v-model="form.description" :min-height="192" />
          </el-form-item>
        </el-form>
        <div class="form-actions">
          <el-button type="primary" @click="submitForm" v-if="!isReview">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCategory } from "@/api/galaxy/category";
import { listTags } from "@/api/galaxy/tags";
import { listTorrent, getTorrent, delTorrent, addTorrent, updateTorrent, exportTorrent, getVerify, listReviewTorrent } from "@/api/galaxy/torrent";
import ImageUpload from '@/components/ImageUpload';
import FileUpload from '@/components/FileUpload';
import TorrentUploader from '@/components/TorrentUploader';
import Editor from '@/components/Editor';
import TorrentViewer from "@/components/TorrentViewer"

export default {
  name: "Torrent",
  components: {
    ImageUpload,
    FileUpload,
    Editor,
    TorrentViewer,
    TorrentUploader
  },
  props: {
    isReview: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      duplicate: null,
      showVerify: false,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      reviewedTorrentList: [],
      unreviewedTorrentList: [],
      title: "",
      open: false,
      statusOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        infoHash: null,
        title: null,
        categories: null,
        status: null,
      },
      form: {},
      rules: {},
      categories: [],
      torrent: null,
      verifyRow: null,
      tagList: null,
      activeTab: 'reviewed',
      thumburl: '@/assets/default-thumb.jpg',
    };
  },
  computed: {
  },
  created() {
    this.getList();
    this.getDicts("glx_verified").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    handleCategoryChanged(category) {
      let categoryId = category[category.length - 1]
      listTags({ categoryId: categoryId, pageSize: 999999 }).then(res => {
        this.tagList = res.rows
      })
    },
    cancelVerify() {
      this.verifyRow = null
      this.showVerify = false
    },
    submitVerify() {
      if (this.verifyRow.status !== "0") {
        if (this.verifyRow.status === "2" && this.verifyRow.remark.length === 0) {
          this.msgError("请填写备注信息")
          return
        }
        getVerify(this.verifyRow.id, this.verifyRow.status, this.verifyRow.remark).then(res => {
          if (res.code === 200) {
            this.msgSuccess("审核完成")
            this.showVerify = false
            this.verifyRow = null
            this.getList()
          } else {
            this.msgError("审核失败: " + res.msg)
          }
        })
      } else {
        this.msgError("请选择审核状态")
      }
    },
    handleVerify(row) {
      this.verifyRow = row
      this.showVerify = true
    },
    previewImage(url) {
      window.open(url)
    },
    handleTorrentUploaded(res) {
      let torrent = res.torrent
      if (torrent) {
        this.duplicate = res.duplicate
        this.torrent = torrent
        if (!this.form.title) {
          this.form.title = torrent.name
        }
      }
    },
    getList() {
      this.loading = true;
      listCategory().then(res => {
        res.data.forEach(x => {
          x.label = x.title
          x.value = x.id
          x.children = null
        })
        this.categories = this.handleTree(res.data)
      })
      let listFunc = this.isReview ? listReviewTorrent : listTorrent;
      listFunc(this.queryParams).then(response => {
        for (let torrent of response.rows) {
          let category = this.$store.getters.categories.find(x => x.id === torrent.categories)
          torrent.categoryName = category.title
        }
        this.reviewedTorrentList = response.rows.filter(torrent => torrent.status === "1");
        this.unreviewedTorrentList = response.rows.filter(torrent => torrent.status === "0");
        this.total = response.total;
        this.loading = false;
      });
    },
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.torrent = null
      this.form = {
        id: null,
        infoHash: null,
        title: null,
        fileName: null,
        categories: null,
        description: null,
        thumburl: '@/assets/default-thumb.jpg',
        fileSize: null,
        totalDownload: null,
        uploaded: null,
        downloaded: null,
        uploadToken: null,
        downloadToken: null,
        status: "0",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "发布资源";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTorrent(id).then(response => {
        this.form = response.data;
        this.torrent = response.data.torrent
        this.open = true;
        this.title = "修改资源";
        this.handleCategoryChanged([this.form.categories])
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let formCpy = JSON.parse(JSON.stringify(this.form))
          delete formCpy.torrent
          if (typeof (formCpy.categories) === 'object') {
            formCpy.categories = formCpy.categories[formCpy.categories.length - 1]
          }
          if (formCpy.id != null) {
            updateTorrent(formCpy).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTorrent(formCpy).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除资源广场编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delTorrent(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有资源广场数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportTorrent(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },
    handleTabClick(tab) {
      this.activeTab = tab.name;
      this.getList();
    }
  }
};
</script>


<style lang="scss" scoped>
.app-container {
  padding: 20px;
  background-color: #f0f2f5;
}

.el-form {
  margin-bottom: 20px;
}

.el-row {
  margin-bottom: 20px;
}

.torrent-card {
  transition: all 0.3s ease;
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  }
}

.torrent-title {
  font-weight: bold;
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.torrent-info {
  font-size: 14px;
  color: #606266;

  p {
    margin: 5px 0;
    display: flex;
    align-items: center;

    i {
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.torrent-operations {
  margin-top: 15px;
  text-align: right;

  .el-button {
    padding: 7px 15px;
    font-size: 14px;
  }
}

// 弹窗样式
.torrent-dialog {
  background-color: rgba(0, 0, 0, 0.7);

  :deep(.el-dialog__header) {
    display: none;
  }

  :deep(.el-dialog__body) {
    padding: 0;
  }
}

.torrent-form-container {
  background-color: #fff;
  border-radius: 10px;
  padding: 40px;
  max-width: 1200px;
  margin: 40px auto;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.torrent-form {
  :deep(.el-form-item__label) {
    font-weight: 600;
    color: #333;
    font-size: 16px;
  }

  :deep(.el-input__inner),
  :deep(.el-textarea__inner) {
    border: none;
    border-bottom: 2px solid #dcdfe6;
    border-radius: 0;
    padding-left: 0;
    font-size: 16px;
    transition: all 0.3s ease;

    &:focus {
      border-color: #409EFF;
    }
  }

  :deep(.el-select) {
    width: 100%;
  }

  :deep(.el-cascader) {
    width: 100%;
  }
}

.duplicate-files {
  background-color: #f8f8f8;
  border-radius: 8px;
  padding: 15px;
  max-height: 150px;
  overflow-y: auto;

  .el-tag {
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

.form-actions {
  text-align: center;
  margin-top: 40px;

  .el-button {
    padding: 12px 30px;
    font-size: 16px;
  }
}

// 标签页样式
.el-tabs {
  margin-bottom: 20px;

  :deep(.el-tabs__item) {
    font-size: 16px;
    padding: 0 20px;
  }
}

// 分页样式
.pagination-container {
  margin-top: 20px;
  text-align: right;
}

// 响应式调整
@media (max-width: 768px) {
  .torrent-form-container {
    padding: 20px;
  }

  .form-title {
    font-size: 24px;
  }

  .torrent-form {
    :deep(.el-form-item__label) {
      font-size: 14px;
    }

    :deep(.el-input__inner),
    :deep(.el-textarea__inner) {
      font-size: 14px;
    }
  }

  .form-actions {
    .el-button {
      padding: 10px 20px;
      font-size: 14px;
    }
  }
}
</style>