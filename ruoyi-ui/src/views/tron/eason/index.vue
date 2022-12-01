<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="代理ID" prop="agencyId">
        <el-input
          v-model="queryParams.agencyId"
          placeholder="请输入代理ID"
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
          v-hasPermi="['tron:eason:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tron:eason:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="easonList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="10" align="center" />
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="代理ID" align="center" prop="agencyId" />
      <el-table-column label="地址" align="center" prop="address"  width="360">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">{{ scope.row.address}}</div>
          <div style="color: #888888;font-style: italic;">地址类型:{{ scope.row.addressType }}</div>
        </template>
      </el-table-column>
      <el-table-column label="账户明细" align="left" width="130">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">占比：{{scope.row.point==null?"0.00":scope.row.point}}</div>
          <div style="color: #888888;font-style: italic;">限额：{{scope.row.min==null?"0.00":scope.row.min}}</div>
          <div style="color: red;font-style: italic;">服务费：{{scope.row.serviceCharge==null?"0.00":scope.row.serviceCharge}}</div>
        </template>
      </el-table-column>
      <el-table-column label="余额" align="left"  prop="balance" width="130">
        <template slot-scope="scope">
          <div v-html="scope.row.balance">
          </div>
        </template>
      </el-table-column>
      <el-table-column label="生成日期" align="center" width="130">
        <template slot-scope="scope">
          <div style="font-size: 15px;">【{{ scope.row.createTime | formatDay}}】</div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <div>
            <span style="color: blue;font-style: italic;">{{ scope.row.status=="0"?"启用":"" }}</span>
            <span style="color: green;font-style: italic;">{{ scope.row.status=="1"?"禁用":"" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  width="150">
        <template slot-scope="scope">
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
          <el-button
            size="mini"
            type="text"
            icon="el-icon-key"
            @click="transfer(scope.row)"
            v-hasPermi="['tron:account:transfer']"
          >转账</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tron:eason:remove']"
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
    <!-- 转账对话框 -->
    <el-dialog :title="title" :visible.sync="openTransfer" width="500px" append-to-body>
      <el-form ref="form" :model="fromTransfer" :rules="rules" label-width="80px">
        <div style="color: green;font-weight: bold;font-size: 10px;">
          <i class="el-icon-warning"></i>
          <span>&nbsp;&nbsp;&nbsp;温馨提示：您在进行转账操作，请核对转账金额与地址</span>  <br></br>
        </div>
        <div style="color: green;font-weight: bold;font-size: 12px;">
          当前余额
          <span v-if="fromTransfer.transferType=='TRX'">【TRX=<span style="color: #f4516c;font-size: 13px; font-weight: bold;">{{fromTransfer.trx}}</span>】</span>
          <span v-if="fromTransfer.transferType=='ETH'">【ETH=<span style="color: #f4516c;font-size: 13px; font-weight: bold;">{{fromTransfer.eth}}</span>】</span>
          &nbsp;&nbsp;&nbsp;&nbsp;<span>【USDT=<span style="color: #f4516c;font-size: 13px;font-weight: bold;">{{fromTransfer.usdt}}</span>】</span>
        </div>
        <el-form-item label="代理ID" prop="agencyId">
          <el-input v-model="fromTransfer.agencyId" placeholder="请输入代理ID" disabled />
        </el-form-item>
        <el-form-item label="来源地址" prop="fromAddress">
          <el-input v-model="fromTransfer.fromAddress" placeholder="请输入地址" disabled/>
        </el-form-item>
        <el-form-item label="地址类型" prop="addressType">
          <el-select v-model="fromTransfer.addressType" placeholder="请选择地址类型">
            <el-option v-if="fromTransfer.transferType=='TRX'" label="TRX" value="TRX"/>
            <el-option v-if="fromTransfer.transferType=='TRX'" label="USDT-TRC20" value="USDT-TRC20"/>
            <el-option v-if="fromTransfer.transferType=='ETH'" label="ETH" value="ETH"/>
            <el-option v-if="fromTransfer.transferType=='ETH'" label="USDT-ERC20" value="USDT-ERC20"/>
          </el-select>
        </el-form-item>
        <el-form-item label="收款地址" prop="toAddress">
          <el-input v-model="fromTransfer.toAddress" placeholder="收款地址"/>
        </el-form-item>
        <el-form-item label="转账金额" prop="balance">
          <el-input v-model="fromTransfer.balance" placeholder="请输入转账金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormTransfer">确 定</el-button>
        <el-button @click="cancelTransfer">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改总站账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="地址类型" prop="addressType">
          <el-select v-model="form.addressType" placeholder="请选择地址类型">
            <el-option label="TRX" value="TRX"/>
            <el-option label="USDT" value="USDT" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status" size="medium">
            <el-radio-button label="0" border>启用</el-radio-button>
            <el-radio-button label="1" border>禁用</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="占比" prop="point">
          <el-input v-model="form.point" placeholder="请输入代理ID" />
        </el-form-item>
        <el-form-item label="限额" prop="point">
          <el-input v-model="form.min" placeholder="请输入限额" />
        </el-form-item>
        <el-form-item label="服务费" prop="serviceCharge">
          <el-input v-model="form.serviceCharge" placeholder="请输入服务费" />
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
import { listEason, getEason, delEason, addEason, updateEason, exportEason } from "@/api/tron/eason";
import store from "@/store";
import {addTransfer} from "@/api/tron/transfer";

export default {
  name: "Eason",
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
      // 总站账户表格数据
      easonList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      openTransfer: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        addressType: undefined,
        address: undefined,
        hexAddress: undefined,
        privatekey: undefined,
        balance: undefined,
      },
      // 表单参数
      form: {},
      // 查询参数
      fromTransfer: {
        agencyId: undefined,
        fromAddress: undefined,
        toAddress: undefined,
        balance: undefined,
        addressType: undefined,
        type: undefined
      },
      // 表单校验
      rules: {
        agencyId: [
          { required: true, message: "代理ID不能为空", trigger: "blur" }
        ],
        point: [
          { required: true, message: "占比不能为空", trigger: "blur" }
        ],
        min: [
          { required: true, message: "限额不能为空", trigger: "blur" }
        ],
        serviceCharge: [
          { required: true, message: "服务费不能为空", trigger: "blur" }
        ],
        addressType: [
          { required: true, message: "地址类型不能为空", trigger: "change" }
        ],
        fromAddress: [
          { required: true, message: "来源地址不能为空", trigger: "blur" }
        ],
        toAddress: [
          { required: true, message: "转账地址不能为空", trigger: "blur" }
        ],
        balance: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
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
    /** 查询总站账户列表 */
    getList() {
      this.loading = true;
      this.easonList = [];
      listEason(this.queryParams).then(response => {
        response.rows.map( (item,index) =>{
          if (item.balance){
            var balance = eval('(' + item.balance +')');
            item.usdt = balance.usdt;
            item.trx = balance.trx;
            item.balance= '<div><i class="usdtIcon"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">'+(balance.usdt).toFixed(6)+'</span></div>'
              +'<div><i class="trxIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">'+(balance.trx).toFixed(6)+'</span></div>';
          }
          this.easonList.push(item);
        })
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelTransfer() {
      this.openTransfer = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        agencyId: undefined,
        addressType: undefined,
        address: undefined,
        hexAddress: undefined,
        privatekey: undefined,
        balance: undefined,
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
    /** 转账操作 */
    transfer(row) {
      this.reset();
      this.fromTransfer.agencyId=row.agencyId;
      this.fromTransfer.fromAddress=row.address;
      this.fromTransfer.addressType=row.addressType;
      this.fromTransfer.transferType=row.addressType;
      this.fromTransfer.trx=row.trx;
      this.fromTransfer.eth=row.eth;
      this.fromTransfer.usdt=row.usdt;
      this.openTransfer=true;
      this.title = "总站账户转账";
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加总站账户";
      this.form.agencyId = store.getters.name; //代理ID显示代理用户名
    },
    /** 查询余额操作 */
    queryBalance(row) {
      this.reset();
      const id = row.id || this.ids
      getEason(id,"queryBalance").then(response => {
        this.msgSuccess("余额查询成功");
        this.getList();
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEason(id,"detail").then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改总站账户";
      });
    },
    submitFormTransfer() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.fromTransfer.type=3;//转账类型 1=赠送,2=打息,3=转账
          this.$confirm('是否进行【'+this.fromTransfer.addressType+'】转账'+this.fromTransfer.balance+'的操作？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.fromTransfer.addressType == "TRX"){
              if (this.fromTransfer.balance>=this.fromTransfer.trx){
                this.msgError("TRX余额不够，请充值！");
                return;
              }
            }
            if (this.fromTransfer.addressType == "ETH"){
              if (this.fromTransfer.balance>=this.fromTransfer.eth){
                this.msgError("TRX余额不够，请充值！");
                return;
              }
            }
            if (this.fromTransfer.addressType == "USDT"){
              if (this.fromTransfer.balance>=this.fromTransfer.usdt){
                this.msgError("USDT余额不够，请充值！");
                return;
              }
            }
            addTransfer(this.fromTransfer).then(response => {
              this.msgSuccess("转账成功");
              this.openTransfer = false;
              this.getList();
            });
          })
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEason(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEason(this.form).then(response => {
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
      this.$confirm('是否确认删除总站账户编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEason(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有总站账户数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportEason(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
