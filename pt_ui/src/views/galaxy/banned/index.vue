<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否申诉" prop="appeal">
        <el-select v-model="queryParams.appeal" placeholder="请选择是否申诉" clearable size="small">
          <el-option
            v-for="dict in appealOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['galaxy:banned:add']"
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
          v-hasPermi="['galaxy:banned:edit']"
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
          v-hasPermi="['galaxy:banned:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['galaxy:banned:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bannedList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
<!--      <el-table-column label="用户" align="center" prop="userId" />-->
      <el-table-column label="解封时间" align="center" prop="expire" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expire, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="封禁原因" align="center" prop="reason" />
      <el-table-column label="申诉状态" align="center" prop="appealState" :formatter="appealStatusFormat" />
<!--      <el-table-column label="是否申诉" align="center" prop="appeal" :formatter="appealFormat" />-->
      <el-table-column label="申诉回复" align="center" prop="appealResult">
        <template slot-scope="scope">
          <span v-if="scope.row.appealResult">
            {{ scope.row.appealResult }}
          </span>
          <span v-else>暂无</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="申诉处理人" align="center" prop="appealUserId">-->
<!--        <template slot-scope="scope">-->
<!--          <span v-if="scope.row.appealUserId">-->
<!--            {{ scope.row.appealState }}-->
<!--          </span>-->
<!--          <span v-else>暂无</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="申诉内容" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.appealState !== null"
            size="mini"
            type="text"
            icon="el-icon-phone-outline"
            @click="handleAppeal(scope.row)"
          >申诉</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            :disabled="scope.row.appealState !== '0'"
            v-hasPermi="['galaxy:banned:appeal']"
          >处理</el-button>
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

    <!-- 添加或修改小黑屋对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="解封时间" prop="expire">
          {{ form.expire }}
        </el-form-item>
        <el-form-item label="封禁原因" prop="reason">
          {{ form.reason }}
        </el-form-item>
        <el-form-item label="申诉内容" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" v-if="!isAudit"/>
          <span v-else>{{ form.remark }}</span>
        </el-form-item>
        <el-form-item label="处理结果" prop="appealState" v-if="isAudit">
          <el-radio-group v-model="form.appealState">
            <el-radio label="1">申请通过</el-radio>
            <el-radio label="2">申请驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申诉回复" prop="appealResult" v-if="isAudit">
          <el-input v-model="form.appealResult" placeholder="请输入回执内容." type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <div v-if="!isAudit">
          <el-button type="primary" @click="submitAppealForm">申诉</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
        <div v-else>
          <el-button type="primary" @click="submitAppealAuditForm">提交</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBanned, getBanned, delBanned, addBanned, updateBanned, exportBanned, createAppeal, appealAudit } from "@/api/galaxy/banned";

export default {
  name: "Banned",
  components: {
  },
  data() {
    return {
      isAudit: false,
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
      // 小黑屋表格数据
      bannedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否申诉字典
      appealOptions: [],
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        appeal: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reason: [
          { required: true, message: "封禁原因不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_yes_no").then(response => {
      this.appealOptions = response.data;
    });
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    submitAppealAuditForm () {
      if (!this.form.appealState || !this.form.appealResult) {
        this.msgError("请选择处理结果及填写回复内容。")
        return
      }
      appealAudit(this.form).then(res => {
        this.msgSuccess(res.msg)
        this.cancel()
      })
    },
    submitAppealForm () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            createAppeal(this.form).then(res => {
              this.msgSuccess(res.msg)
              this.cancel()
            })
          }
        })
    },
    /** 查询小黑屋列表 */
    getList() {
      this.loading = true;
      listBanned(this.queryParams).then(response => {
        this.bannedList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    appealStatusFormat(row, column) {
      let dict = {
        "0": "待处理",
        "1": "申请通过",
        "2": "申请拒绝",
      }
      return dict[row.appealState]
    },
    // 是否申诉字典翻译
    appealFormat(row, column) {
      return this.selectDictLabel(this.appealOptions, row.appeal);
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.getList();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        expire: null,
        reason: null,
        appeal: null,
        appealResult: null,
        appealUserId: null,
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
      this.title = "添加小黑屋";
    },
    handleAppeal(row) {
      this.reset();
      const id = row.id || this.ids
      this.isAudit = false;
      getBanned(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "提交申诉";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.isAudit = true;
      getBanned(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "申诉处理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBanned(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBanned(this.form).then(response => {
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
      this.$confirm('是否确认删除小黑屋编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBanned(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有小黑屋数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBanned(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
