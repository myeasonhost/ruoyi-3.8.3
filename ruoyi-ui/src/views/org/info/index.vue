<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户名" prop="agencyId">
        <el-input
          v-model="queryParams.agencyId"
          placeholder="请输入商户名"
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
          v-hasPermi="['org:info:add']"
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
          v-hasPermi="['org:info:edit']"
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
          v-hasPermi="['org:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['org:info:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" v-if="false"/>
      <el-table-column label="用户ID" align="center" prop="userId" width="100"/>
      <el-table-column label="商户名" align="center" prop="agencyId" width="100"/>
      <el-table-column label="白名单" align="center" prop="whiteIp" />
      <el-table-column label="谷歌秘钥" align="center" prop="googleSecretCode" width="200"/>
      <el-table-column label="谷歌秘钥二维码" align="center" prop="googleSecretQrurl">
        <template slot-scope="scope">
          <image-preview :src="scope.row.googleSecretQrurl" :width="100" :height="100"/>
        </template>
      </el-table-column>
      <el-table-column label="费率明细" align="left" width="130">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">占比：{{scope.row.point==null?"0.00":scope.row.point}}</div>
          <div style="color: green;font-style: italic;">服务费：{{scope.row.serviceCharge==null?"0.00":scope.row.serviceCharge}}U</div>
          <div style="color: red;font-style: italic;">限额：{{scope.row.finish_withdraw==null?"0.00":scope.row.min}}</div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['org:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['org:info:remove']"
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

    <!-- 添加或修改商户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="商户名" prop="agencyId">
              <el-input v-model="form.agencyId" placeholder="请输入商户名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="占比" prop="point">
              <el-input v-model="form.point" placeholder="请输入占比" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最低消费额" prop="min">
              <el-input v-model="form.min" placeholder="请输入最低消费额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务费" prop="serviceCharge">
              <el-input v-model="form.serviceCharge" placeholder="请输入服务费" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="谷歌秘钥" prop="googleSecretCode">
          <el-input v-model="form.googleSecretCode" placeholder="请输入谷歌秘钥" />
        </el-form-item>
        <el-form-item label="谷歌二维码" prop="googleSecretQrurl">
          <el-input v-model="form.googleSecretQrurl" placeholder="请输入谷歌秘钥二维码" />
        </el-form-item>
        <el-form-item label="白名单" prop="whiteIp">
          <el-input v-model="form.whiteIp" placeholder="请输入白名单" />
        </el-form-item>
        <el-form-item label="回调地址" prop="notifyUrl">
          <el-input v-model="form.notifyUrl" placeholder="请输入回调通知地址" />
        </el-form-item>
        <el-form-item label="公钥" prop="publicKey">
          <el-input v-model="form.publicKey" placeholder="请输入公钥" />
        </el-form-item>
        <el-form-item label="私钥" prop="privateKey">
          <el-input v-model="form.privateKey" placeholder="请输入私钥" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo, exportInfo } from "@/api/org/info";

export default {
  name: "Info",
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
      // 商户信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        agencyId: undefined,
        point: undefined,
        serviceCharge: undefined,
        min: undefined,
        whiteIp: undefined,
        googleSecretCode: undefined,
        googleSecretQrurl: undefined,
        notifyUrl: undefined,
        publicKey: undefined,
        privateKey: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        agencyId: [
          { required: true, message: "商户名不能为空", trigger: "blur" }
        ],
        point: [
          { required: true, message: "占比不能为空", trigger: "blur" }
        ],
        serviceCharge: [
          { required: true, message: "服务费不能为空", trigger: "blur" }
        ],
        googleSecretCode: [
          { required: true, message: "谷歌秘钥不能为空", trigger: "blur" }
        ],
        googleSecretQrurl: [
          { required: true, message: "谷歌秘钥二维码不能为空", trigger: "blur" }
        ],
        publicKey: [
          { required: true, message: "公钥不能为空", trigger: "blur" }
        ],
        privateKey: [
          { required: true, message: "私钥不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询商户信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
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
        id: undefined,
        userId: undefined,
        agencyId: undefined,
        point: undefined,
        serviceCharge: undefined,
        min: undefined,
        whiteIp: undefined,
        googleSecretCode: undefined,
        googleSecretQrurl: undefined,
        notifyUrl: undefined,
        publicKey: undefined,
        privateKey: undefined,
        remark: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.title = "添加商户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改商户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
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
      this.$confirm('是否确认删除商户信息编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInfo(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有商户信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
