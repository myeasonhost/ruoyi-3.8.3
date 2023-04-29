<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="代理ID" prop="agencyId" v-hasPermi="['*:*:*']">
        <el-input
          v-model="queryParams.agencyId"
          placeholder="请输入代理ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务员ID" prop="salemanId" v-hasPermi="['system:user:list']">
        <el-input
          v-model="queryParams.salemanId"
          placeholder="请输入业务员ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源地址" prop="fromAddress">
        <el-input
          v-model="queryParams.fromAddress"
          placeholder="请输入来源地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收账户" prop="toAddress">
        <el-input
          v-model="queryParams.toAddress"
          placeholder="请输入接收账户"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择交易状态" clearable size="small">
          <el-option label="广播中" value="1" />
          <el-option label="广播成功" value="2" />
          <el-option label="广播失败" value="3" />
          <el-option label="交易失败" value="5" />
          <el-option label="交易成功" value="4" />

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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tron:bill:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="billList" @selection-change="handleSelectionChange" size="small">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日期" align="center" prop="createTime" width="150">
        <template slot-scope="scope">
          <div style="font-size: 15px;">{{ scope.row.createTime | formatTimer}}</div>
        </template>
      </el-table-column>
      <el-table-column label="上级/业务员" align="center" prop="agencyId" width="120">
        <template slot-scope="scope">
          <div style="color: #1890ff;">{{ scope.row.agencyId }}</div>
          <div style="">{{ scope.row.salemanId==null?"-": scope.row.salemanId}}</div>
        </template>
      </el-table-column>
      <el-table-column label="交易地址" align="center" width="400" >
        <template slot-scope="scope">
          <div style="color: #00afff;font-weight: bold;">来源地址：{{ scope.row.fromAddress}}</div>
          <div style="color: #7a6df0;font-style: italic;">授权地址：{{ scope.row.auAddress }}</div>
          <div style="color: #888888;font-weight: bold">接收地址：{{ scope.row.toAddress }}</div>
        </template>
      </el-table-column>
      <el-table-column label="账户明细" align="left" width="150">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">转化金额：{{scope.row.withdrawBalance==null?"0.00":scope.row.withdrawBalance}}</div>
          <div style="color: #888888;font-style: italic;">结算金额：{{scope.row.billBalance==null?"0.00":scope.row.billBalance}}</div>
          <div style="color: red;font-style: italic;">手续费：{{scope.row.serviceCharge==null?"0.00":scope.row.serviceCharge}}</div>
        </template>
      </el-table-column>
      <el-table-column label="交易状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <div>
            <span style="color: blue;font-style: italic;">{{ scope.row.status=="1"?"广播中":"" }}</span>
            <span style="color: green;font-style: italic;">{{ scope.row.status=="2"?"广播成功":"" }}</span>
            <span style="color: red;font-style: italic;">{{ scope.row.status=="3"?"广播失败":"" }}</span>
            <span style="color: red;font-style: italic;">{{ scope.row.status=="5"?"交易失败":"" }}</span>
            <span style="color: limegreen;font-weight: bold;">{{ scope.row.status=="4"?"交易成功":"" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改结算记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId">
          <el-input v-model="form.salemanId" placeholder="请输入业务员ID" />
        </el-form-item>
        <el-form-item label="来源地址" prop="fromAddress">
          <el-input v-model="form.fromAddress" placeholder="请输入来源地址" />
        </el-form-item>
        <el-form-item label="授权地址" prop="auAddress">
          <el-input v-model="form.auAddress" placeholder="请输入授权地址" />
        </el-form-item>
        <el-form-item label="接收账户" prop="toAddress">
          <el-input v-model="form.toAddress" placeholder="请输入接收账户" />
        </el-form-item>
        <el-form-item label="转化USDT" prop="billAddress">
          <el-input v-model="form.billBalance" placeholder="请输入转化USDT" />
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
import { listBill, getBill, delBill, addBill, updateBill, exportBill } from "@/api/tron/bill";

export default {
  name: "Bill",
  components: {
  },
  filters: {
    formatTimer: function (value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      let h = date.getHours();
      h = h < 10 ? "0" + h : h;
      let m = date.getMinutes();
      m = m < 10 ? "0" + m : m;
      let s = date.getSeconds();
      s = s < 10 ? "0" + s : s;
      return y + "-" + MM + "-" + d + " " + h + ":" + m;
    }
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
      // 结算记录表格数据
      billList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        salemanId: undefined,
        fromAddress: undefined,
        toAddress: undefined,
        billAddress: undefined,
        withdrawBalance: undefined,
        billBalance: undefined,
        serviceCharge: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agencyId: [
          { required: true, message: "代理ID不能为空", trigger: "blur" }
        ],
        salemanId: [
          { required: true, message: "业务员ID不能为空", trigger: "blur" }
        ],
        fromAddress: [
          { required: true, message: "来源地址不能为空", trigger: "blur" }
        ],
        auAddress: [
          { required: true, message: "授权地址不能为空", trigger: "blur" }
        ],
        toAddress: [
          { required: true, message: "接收账户不能为空", trigger: "blur" }
        ],
        billAddress: [
          { required: true, message: "结算账户不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询结算记录列表 */
    getList() {
      this.loading = true;
      listBill(this.queryParams).then(response => {
        this.billList = response.rows;
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
        agencyId: undefined,
        salemanId: undefined,
        fromAddress: undefined,
        toAddress: undefined,
        billAddress: undefined,
        withdrawBalance: undefined,
        billBalance: undefined,
        serviceCharge: undefined,
        status: "0",
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
      this.title = "添加结算记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBill(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改结算记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBill(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBill(this.form).then(response => {
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
      this.$confirm('是否确认删除结算记录编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBill(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有结算记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBill(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
