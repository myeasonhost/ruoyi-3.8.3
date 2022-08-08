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
      <el-form-item label="地址类型" prop="addressType">
        <el-select v-model="queryParams.addressType" placeholder="请选择地址类型" clearable size="small">
          <el-option label="TRX" value="TRX" />
          <el-option label="USDT" value="USDT" />
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
      <el-form-item label="授权代码" prop="token">
        <el-input
          v-model="queryParams.token"
          placeholder="请输入授权代码"
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
          v-hasPermi="['tron:auth:add']"
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
          v-hasPermi="['tron:auth:edit']"
        >修改</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="authList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="授权代码" align="center" prop="id" v-if="false"/>
      <el-table-column label="生成日期" align="center" width="130">
        <template slot-scope="scope">
          <div style="font-size: 15px;">【{{ scope.row.createTime | formatDay}}】</div>
        </template>
      </el-table-column>
      <el-table-column label="上级/业务员" align="center" prop="salemanId" width="120">
        <template slot-scope="scope">
          <div style="color: #1890ff;">{{ scope.row.agencyId }}</div>
          <div style="">{{ scope.row.salemanId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="客服号/地址类型" align="center" prop="addressType" width="140">
        <template slot-scope="scope">
          <div style="color: red;">{{ scope.row.salemanPhone }}</div>
          <div style="">{{ scope.row.addressType }}</div>
        </template>
      </el-table-column>
      <el-table-column label="授权地址" align="center" prop="auAddress"  width="400">
        <template slot-scope="scope">
          <div style="color: #1482f0;font-weight: bold;">{{ scope.row.auAddress }}</div>
          <div>
            <span style="color: gray;font-style: italic;">{{ scope.row.remark }}</span>
            <span style="color: red;font-style: italic;">{{ scope.row.token==null?"":"【"+scope.row.token+"】" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="已授权" align="center" prop="auNum" />
      <el-table-column label="余额" align="left"  prop="balance" width="150">
        <template slot-scope="scope">
          <div v-html="scope.row.balance">
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-thumb"
            @click="openAuthAddress(scope.row)"
            v-hasPermi="['tron:auth:edit']"
            >生成地址</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="queryBalance(scope.row)"
            v-hasPermi="['tron:auth:edit']"
          >查询余额</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tron:auth:edit']"
          >修改</el-button>
          <el-button v-if="!scope.row.auNum"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tron:auth:remove']"
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
    <!-- 添加或修改授权对话框 -->
    <el-dialog :title="title" :visible.sync="authAddress" width="600px" append-to-body>
      <div style="color: green;font-weight: bold;font-size: 10px;">
        <i class="el-icon-warning"></i>
        <span>&nbsp;&nbsp;&nbsp;温馨提示：请点击生成地址，发给客户</span>  <br></br>
      </div>
      <span style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;要自己点击按钮复制一下，动动手方便又快捷，么么哒...</span></br>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="地址链接：" prop="urlAddress">
          <el-input v-model="form.urlAddress" placeholder="请生成地址链接" size="medium"
                    WarningColor="danger" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-clipboard:copy="form.urlAddress" @click="createAuthAddress">生成地址并复制链接</el-button>
        <el-button @click="cancelAuthAddress">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改授权对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId" v-hasPermi="['*:*:*']">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId" v-hasPermi="['system:user:list']">
          <el-select
            v-model="form.salemanId"
            placeholder="请输入业务员ID"
            @click.native="getUserListByDeptId">
            <el-option
              v-for="item in salemanIds"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="地址类型" prop="addressType">
          <el-select v-model="form.addressType" placeholder="请选择地址类型">
            <el-option label="TRX" value="TRX"/>
            <el-option label="USDT" value="USDT" />
          </el-select>
        </el-form-item>
        <el-form-item label="客服电话" prop="salemanPhone">
          <el-input v-model="form.salemanPhone" placeholder="示例：Whatsapp:+85777777 或 telegram:@TRON001" />
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
import { listAuth, getAuth, delAuth, addAuth, updateAuth } from "@/api/tron/auth";
import { listUser } from "@/api/system/user";
import store from "@/store";

export default {
  name: "Auth",
  components: {
  },
  filters: {
    formatDay: function(value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      return y + "-" + MM + "-" + d;
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
      // 授权表格数据
      authList: [],
      // 业务员表格数据
      salemanIds: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      authAddress: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        salemanId: undefined,
        addressType: "TRX",
        auAddress: undefined,
        token: undefined
      },
      // 表单参数
      form: {

      },
      // 表单校验
      rules: {
        addressType: [
          { required: true, message: "地址类型不能为空", trigger: "change" }
        ],
        auAddress: [
          { required: true, message: "授权地址不能为空", trigger: "blur" }
        ],
        token: [
          { required: true, message: "授权代码不能为空", trigger: "blur" }
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
    /** 查询授权列表 */
    getList() {
      this.loading = true;
      this.authList = [];
      listAuth(this.queryParams).then(response => {
        response.rows.map( (item,index) =>{
          if (item.balance){
            var balance = eval('(' + item.balance +')');
            item.balance= '<div><i class="usdtIcon"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">'+(balance.usdt).toFixed(6)+'</span></div>'
              +'<div><i class="trxIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">'+(balance.trx).toFixed(6)+'</span></div>';
          }
          this.authList.push(item);
        })
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
    openAuthAddress(row){
      this.reset();
      const id = row.id || this.ids
      getAuth(id,"detail").then(response => {
        this.form = response.data;
        this.authAddress = true;
        this.title = "生成地址";
      });
    },
    createAuthAddress(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAuth(this.form).then(response => {
              this.form = response.data;
              this.msgSuccess("生成链接并复制成功");
              this.open = false;
              this.$toast({ message: "copy Success"});
            });
          }
        }
      });
    },
    cancelAuthAddress(){
      this.authAddress = false;
      this.reset();
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
        addressType: "TRX",
        auAddress: undefined,
        token: undefined,
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
      this.title = "添加授权";

    },
    /** 查询余额操作 */
    queryBalance(row) {
      this.reset();
      const id = row.id || this.ids
      getAuth(id,"queryBalance").then(response => {
        this.msgSuccess("余额查询成功");
        this.getList();
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAuth(id,"detail").then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改授权";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAuth(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAuth(this.form).then(response => {
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
      this.$confirm('是否确认删除授权为"' + row.auAddress + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAuth(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    }
  }
};
</script>
<style>

.el-input.is-disabled .el-input__inner[warningcolor="danger"]{
  background-color: #faf6f5;
  border-color: #dfe4ed;
  color: red;
  font-weight: bold;
  cursor: not-allowed;
}
</style>
