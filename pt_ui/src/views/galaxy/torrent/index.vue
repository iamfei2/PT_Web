<template>
  <div class="app-container">
    <el-form v-if="!isReview" :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="哈希值" prop="infoHash">-->
<!--        <el-input-->
<!--          v-model="queryParams.infoHash"-->
<!--          placeholder="请输入哈希值"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目录" prop="categories">
        <el-input
          v-model="queryParams.categories"
          placeholder="请输入目录"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-if="!isReview">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['galaxy:torrent:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['galaxy:torrent:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['galaxy:torrent:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['galaxy:torrent:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="torrentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="编号" align="center" prop="id" />-->
<!--      <el-table-column label="哈希值" align="center" prop="infoHash" />-->
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="目录" align="center" prop="categories">
        <template slot-scope="scope">
          <span>{{ scope.row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="thumburl">
        <template slot-scope="scope">
          <img :src="scope.row.thumburl" style="height: 80px; cursor: pointer;" @click="previewImage(scope.row.thumburl)"/>
        </template>
      </el-table-column>
      <el-table-column label="文件大小" align="center" prop="fileSize">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.fileSize) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分享率">
        <template slot-scope="scope">
          <span v-if="scope.row.downloaded > 0 && scope.row.uploaded">{{ (scope.row.uploaded / scope.row.downloaded).toFixed(2) }}</span>
          <span v-else>0</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="下载次数" align="center" prop="totalDownload" />-->
<!--      <el-table-column label="上传积分" align="center" prop="uploaded">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ formatFileSize(scope.row.uploaded) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="下载积分" align="center" prop="downloaded">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ formatFileSize(scope.row.downloaded) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
<!--      <el-table-column label="备注信息" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleVerify(scope.row)"
            v-hasPermi="['galaxy:torrent:verify']">
            审核
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['galaxy:torrent:edit']"
          >{{ isReview ? '查看' : '修改' }}</el-button>
          <el-button
            v-if="!isReview"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['galaxy:torrent:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog :visible.sync="showVerify" width="500px" append-to-body title="审核">
      <el-form v-if="verifyRow">
        <el-form-item label="审核结果">
          <el-select v-model="verifyRow.status" placeholder="请选择审核状态" clearable size="small">
            <el-option
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
              :disabled="dict.dictValue === '0'"
            />
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
    <!-- 添加或修改资源广场对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" :disabled="isReview">
        <el-row>
          <el-col :span="8">
            <el-form-item label="目录" prop="categories">
              <el-cascader v-model="form.categories" :options="categories" @change="handleCategoryChanged"></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缩略图">
              <imageUpload v-model="form.thumburl"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="!isReview">
            <el-form-item label="种子文件">
              <fileUpload v-model="form.fileName" :file-type="['torrent']" url="/galaxy/torrent/upload" @success="handleTorrentUploaded"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-show="torrent">
            <el-form-item label="种子信息">
              <torrent-viewer :torrent="torrent" />
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="!isReview">
            <el-form-item label="附加种子">
              <torrent-uploader :limit="0" v-model="form.attachment" :file-type="['torrent']" :token="form.remark"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="duplicate && duplicate.length > 0">
            <el-form-item label="重复文件">
              <div style="max-height: 300px; overflow-y: scroll">
                <span v-for="(v, k) in duplicate" :key="'duplicate_' + k" style="color: red; font-weight: bold; margin-right: 5px;">{{ v.fileName }}</span>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="标签">
              <el-select
                v-model="form.tags"
                multiple
                filterable
                allow-create
                default-first-option
                placeholder="请输入种子标签" style="width: 100%;">
                <el-option
                  v-for="item in tagList"
                  :key="item.id"
                  :label="item.tag"
                  :value="item.tag">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="详情">
              <editor v-model="form.description" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="torrent && !isReview">
            <el-form-item label="Tracker">
              <el-input v-model="torrent.announce" readonly/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="发布状态" v-has-role="['admin', 'user_plus']" v-if="!isReview">
          <el-radio-group v-model="form.status">
            <el-radio label="0">审核后发布</el-radio>
            <el-radio label="1">直接发布</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isReview">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 资源广场表格数据
      torrentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        infoHash: null,
        title: null,
        categories: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      categories: [],
      torrent: null,
      verifyRow: null,
      tagList: null
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
    handleCategoryChanged (category) {
      let categoryId = category[category.length - 1]
      listTags({categoryId: categoryId, pageSize: 999999}).then(res => {
        this.tagList = res.rows
      })
      //listTags
    },
    cancelVerify() {
      this.verifyRow = null
      this.showVerify = false
    },
    submitVerify () {
      if (this.verifyRow.status !== "0") {
        if (this.verifyRow.status === "2" && this.verifyRow.remark.length === 0) {
          this.msgError("请填写备注信息")
          return
        }
        getVerify(this.verifyRow.id, this.verifyRow.status, this.verifyRow.remark).then( res => {
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
    handleVerify (row) {
      this.verifyRow = row
      this.showVerify = true
    },
    previewImage (url) {
      window.open(url)
    },
    handleTorrentUploaded (res) {
      let torrent = res.torrent
      if (torrent) {
        this.duplicate = res.duplicate
        this.torrent = torrent
        if (!this.form.title) {
          this.form.title = torrent.name
        }
      }
    },
    /** 查询资源广场列表 */
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
      let listFunc = listTorrent
      if (this.isReview) {
        listFunc = listReviewTorrent
      }
      listFunc(this.queryParams).then(response => {
        for(let torrent of response.rows) {
          let category = this.$store.getters.categories.find(x => x.id === torrent.categories)
          torrent.categoryName = category.title
        }
        this.torrentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.torrent = null
      this.form = {
        id: null,
        infoHash: null,
        title: null,
        fileName: null,
        categories: null,
        description: null,
        thumburl: null,
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "发布资源";
    },
    /** 修改按钮操作 */
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
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let formCpy = JSON.parse(JSON.stringify(this.form))
          delete formCpy.torrent
          if (typeof(formCpy.categories) === 'object') {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除资源广场编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTorrent(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有资源广场数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTorrent(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
