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
        <el-select
          v-model="queryParams.salemanId"
          placeholder="请输入业务员ID"
          @click.native="getUserListByDeptId"
          @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in salemanIds"
            :key="item.value"
            :label="item.label"
            :value="item.value"/>
        </el-select>
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
      <el-form-item label="交易类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择" clearable size="small">
          <el-option label="赠送" value="1" />
          <el-option label="打息" value="2" />
          <el-option label="转账" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable size="small">
          <el-option label="广播中" value="1" />
          <el-option label="广播成功" value="2" />
          <el-option label="广播失败" value="3" />
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
          v-hasPermi="['tron:transfer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transferList" @selection-change="handleSelectionChange" size="small">
      <el-table-column label="ID" align="center" prop="id" width="80"/>
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
      <el-table-column label="交易地址" align="left" width="350" >
        <template slot-scope="scope">
          <div style="color: #00afff;">来源地址：{{ scope.row.fromAddress}}</div>
          <div style="color: #888888;font-style: italic;">接收地址：{{ scope.row.toAddress }}</div>
        </template>
      </el-table-column>
      <el-table-column label="交易类型" align="left" prop="type" width="150">
        <template slot-scope="scope">
          <div>
            <span style="color: blue;font-weight: bold;">{{ scope.row.type=="1"?scope.row.addressType+" 赠送":"" }}</span>
            <span style="color: green;font-weight: bold;">{{ scope.row.type=="2"?scope.row.addressType+" 打息":"" }}</span>
            <span style="color: red;font-weight: bold;">{{ scope.row.type=="3"?scope.row.addressType+" 转账":"" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="交易金额" align="center" prop="balance" width="120"/>
      <el-table-column label="交易状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <div>
            <span style="color: blue;font-style: italic;">{{ scope.row.status=="1"?"广播中":"" }}</span>
            <span style="color: green;font-style: italic;">{{ scope.row.status=="2"?"广播成功":"" }}</span>
            <span style="color: red;font-style: italic;">{{ scope.row.status=="3"?"广播失败":"" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="200"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listTransfer, getTransfer, delTransfer, addTransfer, updateTransfer, exportTransfer } from "@/api/tron/transfer";
import store from "@/store";
import {listUser} from "@/api/system/user";

export default {
  name: "Transfer",
  components: {
  },
  filters: {
    formatTimer: function(value) {
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
      // 转账记录表格数据
      transferList: [],
      // 业务员表格数据
      salemanIds: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        fromAddress: undefined,
        salemanId: undefined,
        toAddress: undefined,
        balance: undefined,
        type: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agencyId: [
          { required: true, message: "代理ID不能为空", trigger: "blur" }
        ],
        fromAddress: [
          { required: true, message: "来源地址不能为空", trigger: "blur" }
        ],
        salemanId: [
          { required: true, message: "业务员ID不能为空", trigger: "blur" }
        ],
        toAddress: [
          { required: true, message: "接收账户不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "1=广播中,2=广播成功，3=广播失败不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询转账记录列表 */
    getList() {
      this.loading = true;
      listTransfer(this.queryParams).then(response => {
        this.transferList = response.rows;
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
        fromAddress: undefined,
        salemanId: undefined,
        toAddress: undefined,
        balance: undefined,
        type: undefined,
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
      this.title = "添加转账记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTransfer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改转账记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTransfer(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTransfer(this.form).then(response => {
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
      this.$confirm('是否确认删除转账记录编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTransfer(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有转账记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTransfer(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /** 查询业务员列表-按部门ID查找 */
    getUserListByDeptId() {
      this.salemanIds = [];
      var param = {"pageNum":1,"pageSize":1000,"deptId":store.state.user.deptId}; //业务员最高值定在50以内
      listUser(param).then(response => {
        for (let row of response.rows) {
          var option={};
          option.value=row.userName;
          option.label=row.userName+"（"+row.nickName+"）";
          this.salemanIds.push(option);
        }
      });
    },
  }
};
</script>
