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
      <el-form-item label="电话" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入电话"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="渠道" prop="chanel">
        <el-input
          v-model="queryParams.chanel"
          placeholder="请输入渠道"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职业" prop="profession">
        <el-input
          v-model="queryParams.profession"
          placeholder="请输入职业"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否有效" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择是否有效" clearable size="small">
          <el-option label="有效" value="0" />
          <el-option label="无效" value="1" />
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
          v-hasPermi="['tron:fans:add']"
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
          v-hasPermi="['tron:fans:edit']"
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
          v-hasPermi="['tron:fans:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['tron:fans:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fansList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="上级/业务员" align="center" prop="salemanId" width="120">
        <template slot-scope="scope">
          <div style="color: #1890ff;">{{ scope.row.agencyId }}</div>
          <div style="">{{ scope.row.salemanId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="电话" align="center" prop="mobile" />
      <el-table-column label="渠道" align="center" prop="chanel" />
      <el-table-column label="职业" align="center" prop="profession" />
      <el-table-column label="钱包/交易所" align="center" prop="salemanId" width="120">
        <template slot-scope="scope">
          <div style="color: #1890ff;">{{ scope.row.walletType }}</div>
          <div style="">{{ scope.row.exchangeType }}</div>
        </template>
      </el-table-column>
      <el-table-column label="地区" align="center" prop="area" />
      <el-table-column label="是否有效" align="center" prop="status" >
        <template slot-scope="scope">
          <div>
            <span style="color: green;font-style: italic;">{{ scope.row.status=="0"?"有效":"" }}</span>
            <span style="color: gray;font-style: italic;">{{ scope.row.status=="1"?"无效":"" }}</span>
          </div>
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
            v-hasPermi="['tron:fans:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tron:fans:remove']"
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
    <!-- 添加粉丝对话框 -->
    <el-dialog :title="title" :visible.sync="openAdd" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId" v-hasPermi="['*:*:*']">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId" v-hasPermi="['system:user:list']">
          <el-select
            v-model="form.salemanId"
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
        <el-form-item label="电话" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="渠道" prop="chanel">
          <el-input v-model="form.chanel" placeholder="请输入渠道" />
        </el-form-item>
        <el-form-item label="职业" prop="profession">
          <el-input v-model="form.profession" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelAdd">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改粉丝对话框 -->
    <el-dialog :title="title" :visible.sync="openEdit" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId" v-hasPermi="['*:*:*']">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId" v-hasPermi="['system:user:list']">
          <el-select
            v-model="form.salemanId"
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
        <el-form-item label="电话" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入电话" disabled/>
        </el-form-item>
        <el-form-item label="渠道" prop="chanel">
          <el-input v-model="form.chanel" placeholder="请输入渠道" />
        </el-form-item>
        <el-form-item label="职业" prop="profession">
          <el-input v-model="form.profession" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="钱包" prop="walletType">
          <el-input v-model="form.walletType" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="交易所" prop="exchangeType">
          <el-input v-model="form.exchangeType" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="地区" prop="area">
          <el-input v-model="form.area" placeholder="请输入地区" />
        </el-form-item>
        <el-form-item label="是否有效">
          <el-select v-model="form.status" placeholder="请选择是否有效" clearable size="small">
            <el-option label="有效" value="0" />
            <el-option label="无效" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelEdit">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFans, getFans, delFans, addFans, updateFans, exportFans } from "@/api/tron/fans";
import store from "@/store";
import {listUser} from "@/api/system/user";

export default {
  name: "Fans",
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
      // 粉丝表格数据
      fansList: [],
      // 业务员表格数据
      salemanIds: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      openAdd: false,
      openEdit: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        salemanId: undefined,
        mobile: undefined,
        chanel: undefined,
        profession: undefined,
        walletType: undefined,
        exchangeType: undefined,
        area: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        mobile: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        chanel: [
          { required: true, message: "渠道不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询粉丝列表 */
    getList() {
      this.loading = true;
      listFans(this.queryParams).then(response => {
        this.fansList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    // 取消按钮
    cancelAdd() {
      this.openAdd = false;
      this.reset();
    },
    cancelEdit() {
      this.openEdit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        agencyId: undefined,
        salemanId: undefined,
        mobile: undefined,
        chanel: undefined,
        profession: undefined,
        walletType: undefined,
        exchangeType: undefined,
        area: undefined,
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
      this.openAdd = true;
      this.title = "添加粉丝";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFans(id).then(response => {
        this.form = response.data;
        this.openEdit = true;
        this.title = "修改粉丝";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFans(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFans(this.form).then(response => {
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
      this.$confirm('是否确认删除粉丝编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFans(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有粉丝数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportFans(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
