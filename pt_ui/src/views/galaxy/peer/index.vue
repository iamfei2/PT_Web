<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户编号" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点编号" prop="peerId">
        <el-input
          v-model="queryParams.peerId"
          placeholder="请输入节点编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="IP地址" prop="ip">
        <el-input
          v-model="queryParams.ip"
          placeholder="请输入IP地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="端口" prop="port">
        <el-input
          v-model="queryParams.port"
          placeholder="请输入端口"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="事件" prop="event">
        <el-input
          v-model="queryParams.event"
          placeholder="请输入事件"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="种子编号" prop="torrentId">
        <el-input
          v-model="queryParams.torrentId"
          placeholder="请输入种子编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['galaxy:peer:add']"
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
          v-hasPermi="['galaxy:peer:edit']"
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
          v-hasPermi="['galaxy:peer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['galaxy:peer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="peerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="编号" align="center" prop="id" />-->
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="节点编号" align="center" prop="peerId" />
      <el-table-column label="IP地址" align="center" prop="ip" />
      <el-table-column label="端口" align="center" prop="port" />
      <el-table-column label="上传量" align="center" prop="uploaded">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.uploaded) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下载量" align="center" prop="downloaded">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.downloaded) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="剩余量" align="center" prop="leftSize">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.leftSize) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下载速度" align="center" prop="downloadSpeed">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.downloadSpeed) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上传速度" align="center" prop="uploadSpeed">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.uploadSpeed) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="钥匙" align="center" prop="key" />
      <el-table-column label="事件" align="center" prop="event" />
      <el-table-column label="种子" align="center" prop="torrentTitle" />
      <el-table-column label="更新时间" align="center" prop="updateTime" />
<!--      <el-table-column label="状态" align="center" prop="status" />-->
<!--      <el-table-column label="备注信息" align="center" prop="remark" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['galaxy:peer:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['galaxy:peer:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改节点管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户编号" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户编号" />
        </el-form-item>
        <el-form-item label="节点编号" prop="peerId">
          <el-input v-model="form.peerId" placeholder="请输入节点编号" />
        </el-form-item>
        <el-form-item label="IP地址" prop="ip">
          <el-input v-model="form.ip" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input v-model="form.port" placeholder="请输入端口" />
        </el-form-item>
        <el-form-item label="上传量" prop="uploaded">
          <el-input v-model="form.uploaded" placeholder="请输入上传量" />
        </el-form-item>
        <el-form-item label="下载量" prop="downloaded">
          <el-input v-model="form.downloaded" placeholder="请输入下载量" />
        </el-form-item>
        <el-form-item label="剩余量" prop="leftSize">
          <el-input v-model="form.leftSize" placeholder="请输入剩余量" />
        </el-form-item>
        <el-form-item label="下载速度" prop="downloadSpeed">
          <el-input v-model="form.downloadSpeed" placeholder="请输入下载速度" />
        </el-form-item>
        <el-form-item label="上传速度" prop="uploadSpeed">
          <el-input v-model="form.uploadSpeed" placeholder="请输入上传速度" />
        </el-form-item>
        <el-form-item label="钥匙" prop="key">
          <el-input v-model="form.key" placeholder="请输入钥匙" />
        </el-form-item>
        <el-form-item label="事件" prop="event">
          <el-input v-model="form.event" placeholder="请输入事件" />
        </el-form-item>
        <el-form-item label="种子" prop="torrentId">
          <el-input v-model="form.torrentId" placeholder="请输入种子编号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPeer, getPeer, delPeer, addPeer, updatePeer, exportPeer } from "@/api/galaxy/peer";

export default {
  name: "Peer",
  components: {
  },
  data() {
    return {
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
      // 节点管理表格数据
      peerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        peerId: null,
        ip: null,
        port: null,
        uploaded: null,
        downloaded: null,
        leftSize: null,
        downloadSpeed: null,
        uploadSpeed: null,
        key: null,
        event: null,
        torrentId: null,
        status: null,
        orderByColumn: 'updateTime',
        isAsc: 'desc'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询节点管理列表 */
    getList() {
      this.loading = true;
      listPeer(this.queryParams).then(response => {
        this.peerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        peerId: null,
        ip: null,
        port: null,
        uploaded: null,
        downloaded: null,
        leftSize: null,
        downloadSpeed: null,
        uploadSpeed: null,
        key: null,
        event: null,
        torrentId: null,
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
      this.title = "添加节点管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPeer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改节点管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePeer(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPeer(this.form).then(response => {
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
      this.$confirm('是否确认删除节点管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPeer(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有节点管理数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPeer(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
