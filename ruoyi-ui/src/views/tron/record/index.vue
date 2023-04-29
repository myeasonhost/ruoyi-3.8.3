<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="授权地址" prop="auAddress">
        <el-input
          v-model="queryParams.auAddress"
          placeholder="请输入授权地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tron:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange" size="small">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="授权日期" align="center" width="150">
        <template slot-scope="scope">
          <div style="font-size: 15px;">{{ scope.row.createTime | formatTimer}}</div>
        </template>
      </el-table-column>
      <el-table-column label="地区" align="center" prop="id" v-if="false"/>
      <el-table-column label="授权Token" align="center" prop="token" />
      <el-table-column label="业务员ID" align="center" prop="salemanId" />
      <el-table-column label="地址" align="center" width="400">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">{{ scope.row.address}}</div>
          <div style="color: #888888;font-style: italic;">{{ scope.row.auAddress }}</div>
        </template>
      </el-table-column>
      <el-table-column label="账户IP地址" align="center" prop="ip" />
      <el-table-column label="地区" align="center" prop="area" />
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
import { listRecord,  exportRecord } from "@/api/tron/record";
import store from "@/store";
import {listUser} from "@/api/system/user";

export default {
  name: "Record",
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
      // 授权记录表格数据
      recordList: [],
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
        auId: undefined,
        token: undefined,
        userId: undefined,
        address: undefined,
        auAddress: undefined,
        ip: undefined,
        area: undefined,
      }
    };
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
  created() {
    this.getList();
  },
  methods: {
    /** 查询授权记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询业务员列表-按部门ID查找 */
    getUserListByDeptId() {
      this.salemanIds = [];
      var param = {"pageNum":1,"pageSize":100,"deptId":store.state.user.deptId}; //业务员最高值定在50以内
      listUser(param).then(response => {
        for (let row of response.rows) {
          var option={};
          option.value=row.userName;
          option.label=row.userName+"（"+row.nickName+"）";
          this.salemanIds.push(option);
        }
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
        auId: undefined,
        token: undefined,
        userId: undefined,
        address: undefined,
        auAddress: undefined,
        ip: undefined,
        area: undefined,
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
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有授权记录数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportRecord(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
