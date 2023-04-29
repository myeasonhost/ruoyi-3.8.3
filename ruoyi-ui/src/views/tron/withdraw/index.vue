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
      <el-form-item label="客户地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入客户地址"
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
          v-hasPermi="['tron:withdraw:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="withdrawList" @selection-change="handleSelectionChange" size="small">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日期" align="center" width="150">
        <template slot-scope="scope">
          <div style="font-size: 15px;">{{ scope.row.createTime | formatTimer}}</div>
        </template>
      </el-table-column>
      <el-table-column label="上级/业务员" align="center"  width="120">
        <template slot-scope="scope">
          <div style="color: #1890ff;">{{ scope.row.agencyId }}</div>
          <div style="">{{ scope.row.salemanId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="客户地址" align="center" prop="address"  width="360"/>
      <el-table-column label="当前本金" align="center" prop="currentBalance" />
      <el-table-column label="挖矿总金额" align="center" prop="totalBalance" />
      <el-table-column label="当前提款" align="center" prop="currentWithdraw" />
      <el-table-column label="状态" align="center" >
        <template slot-scope="scope">
          <div>
            <span style="color: blue;font-style: italic;">{{ scope.row.status=="1"?"申请提现":"" }}</span>
            <span style="color: green;font-style: italic;">{{ scope.row.status=="2"?"同意提现":"" }}</span>
            <span style="color: red;font-style: italic;">{{ scope.row.status=="3"?"打款已提":"" }}</span>
            <span style="color: gray;font-style: italic;">{{ scope.row.status=="4"?"拒绝提现":"" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <span v-if="scope.row.status=='1'">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleKeti(scope.row)"
              v-hasPermi="['tron:withdraw:keti']"
            >同意可提</el-button>
          </span>
          <span v-if="scope.row.status=='2'" v-hasPermi="['system:user:list']">
               <el-button
                 size="mini"
                 type="text"
                 icon="el-icon-edit"
                 @click="handleUpdate(scope.row)"
                 v-hasPermi="['tron:transfer:add']"
               >打款</el-button>
          </span>
          <span v-if="scope.row.status=='1'">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tron:withdraw:remove']"
          >拒绝提现</el-button>
          </span>
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

    <!-- 添加或修改利息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <div style="color: green;font-weight: bold;font-size: 10px;">
        <i class="el-icon-warning"></i>
        <span>&nbsp;&nbsp;&nbsp;温馨提示：请按时审核客户提现，打款操作请查询转账记录查看是否成功</span>  <br></br>
      </div>
      <span style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;【打款】请确保打款账户余额充足；</span>
      <el-form ref="form" :model="formTransfer" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId" >
          <el-input v-model="formTransfer.agencyId" placeholder="请输入代理ID" disabled/>
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId">
          <el-input v-model="formTransfer.salemanId" placeholder="请输入业务员ID" disabled/>
        </el-form-item>
        <el-form-item label="收款地址" prop="toAddress">
          <el-input v-model="formTransfer.toAddress" placeholder="请输入收款地址" disabled/>
        </el-form-item>
        <el-form-item label="当前提款" prop="balance">
          <el-input v-model="formTransfer.balance" placeholder="请输入当前利息" />
        </el-form-item>
        <el-form-item label="地址类型" prop="addressType">
          <el-select v-model="formTransfer.addressType" placeholder="请选择地址类型">
            <el-option label="TRX" value="TRX"/>
            <el-option label="USDT" value="USDT" />
          </el-select>
        </el-form-item>
        <el-form-item label="打款账户" prop="fromAddress" width="300">
          <el-col :span="15">
            <el-select
              v-model="formTransfer.fromAddress"
              placeholder="请输入打款账户"
              @click.native="getAccountList" size="medium">
              <el-option
                v-for="item in accountList"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
            </el-select>
          </el-col>
          <el-col :span="8">
            <dev style="color: red;font-size: 8px;font-weight: bold;">*账户余额要充足*</dev>
          </el-col>
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
import {delWithdraw, exportWithdraw, getWithdraw, listWithdraw, updateWithdraw} from "@/api/tron/withdraw";
import store from "@/store";
import {listUser} from "@/api/system/user";
import {listAccount} from "@/api/tron/account";
import {addTransfer} from "@/api/tron/transfer";

export default {
  name: "Withdraw",
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
      // 提款表格数据
      withdrawList: [],
      // 业务员表格数据
      salemanIds: [],
      // 账户下拉框数据
      accountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fishId: undefined,
        agencyId: undefined,
        salemanId: undefined,
        address: undefined,
        currentBalance: undefined,
        totalBalance: undefined,
        currentWithdraw: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 打息转账表单数据,
      formTransfer: {},
      // 表单校验
      rules: {
        fishId: [
          { required: true, message: "鱼苗ID不能为空", trigger: "blur" }
        ],
        agencyId: [
          { required: true, message: "代理ID不能为空", trigger: "blur" }
        ],
        salemanId: [
          { required: true, message: "业务员ID不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        currentInterest: [
          { required: true, message: "利息不能为空", trigger: "blur" }
        ],
        balance: [
          { required: true, message: "提款金额不能为空", trigger: "blur" }
        ],
        fromAddress: [
          { required: true, message: "打款账户不能为空", trigger: "blur" }
        ],
        toAddress: [
          { required: true, message: "收款账户不能为空", trigger: "blur" }
        ],
        addressType: [
          { required: true, message: "地址类型不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询提款列表 */
    getList() {
      this.loading = true;
      listWithdraw(this.queryParams).then(response => {
        this.withdrawList = response.rows;
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
        fishId: undefined,
        agencyId: undefined,
        salemanId: undefined,
        address: undefined,
        currentBalance: undefined,
        totalBalance: undefined,
        currentWithdraw: undefined,
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
      this.title = "添加提款";
    },
    /** 同意提现按钮操作 */
    handleKeti(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdraw(id).then(response => {
        this.form = response.data;
      });
      this.$confirm('提现申请是否同意?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form.status = 2;
        this.form.remark = "同意提现";
        updateWithdraw(this.form).then(response => {
          this.msgSuccess("同意提现");
          this.getList();
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 打款按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdraw(id).then(response => {
        this.form = response.data;
        this.formTransfer.agencyId = this.form.agencyId;
        this.formTransfer.salemanId = this.form.salemanId;
        this.formTransfer.toAddress = this.form.address;
        this.formTransfer.balance = this.form.currentWithdraw;
        this.formTransfer.addressType = "USDT";
        this.open = true;
        this.title = "可提现转账处理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.formTransfer.type = 3; //转账类型 1=赠送,2=打息,3=转账
          this.$confirm('是否进行【'+this.formTransfer.addressType+'】打款'+this.formTransfer.balance+'的操作？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            addTransfer(this.formTransfer).then(response => {
              this.msgSuccess("打款成功");
              //更新利息记录表状态
              this.form.status=3;
              this.form.remark="打款已提";
              updateWithdraw(this.form).then(response => {
                this.open = false;
                this.getList();
              });
            });
          })
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$confirm('是否确认拒绝提款 鱼苗ID为"' + row.fishId + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delWithdraw(id);
        }).then(() => {
          this.getList();
          this.msgSuccess("拒绝成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有提款数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportWithdraw(queryParams);
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
    /** 查询站内帐户 */
    getAccountList() {
      this.accountList = [];
      var param = {"pageNum":1,"pageSize":1000}; //账户最高值定在50以内
      listAccount(param).then(response => {
        response.rows.map( (item,index) =>{
          if (item.balance){
            var balance = eval('(' + item.balance +')');
            item.label ="【USDT:"+balance.usdt+",TRX:"+balance.trx+"】"+ item.address;
            item.value = item.address;
          }
          this.accountList.push(item);
        })
      });
    }
  }
};
</script>
