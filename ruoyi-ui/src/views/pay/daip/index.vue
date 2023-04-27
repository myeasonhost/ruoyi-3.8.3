<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商户ID" prop="siteId" v-hasPermi="['*:*:*']">
        <el-input
          v-model="queryParams.siteId"
          placeholder="请输入商户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户订单号" prop="orderId" label-width="100">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入商户订单号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提现状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.mbpay_withdraw_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['pay:daip:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="daipList" @selection-change="handleSelectionChange" size="small">
      <el-table-column label="支付订单号" align="center" prop="id" v-if="false"/>
      <el-table-column label="商户ID" align="center" prop="siteId"/>
      <el-table-column label="商户订单号" align="center" prop="orderId"/>
      <el-table-column label="用户名" align="center" prop="userId"/>
      <el-table-column label="产品名" align="center" prop="productName"/>
      <el-table-column label="支付明细" align="left" width="120">
        <template slot-scope="scope">
          <div style="color: #13ce66;font-family: 'Arial Black';font-size: xx-small;">提现金额：{{ scope.row.amount }}</div>
          <div style="color: #f4516c;font-family: 'Arial Black';font-size: xx-small;">转出金额：{{
              scope.row.coinAmount
            }}
          </div>
          <div style="color: #666666;font-size: xx-small;">支付币种：{{ scope.row.coinCode }}</div>
          <div style="color: #666666;font-size: xx-small;">订单币种：{{ scope.row.currency }}</div>
        </template>
      </el-table-column>
      <el-table-column label="提现状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.mbpay_withdraw_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="通知状态" align="center" prop="notifySucceed">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tron_notify_state" :value="scope.row.notifySucceed"/>
        </template>
      </el-table-column>
      <el-table-column label="收款地址" align="center" prop="coinAddress" width="150"/>
      <el-table-column label="转账时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['pay:daip:edit']"
            v-if="scope.row.status==0"
          >审批转账
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-phone-outline"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['pay:daip:edit']"
              v-if="scope.row.notifySucceed==0"
            >手动回调
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['pay:daip:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改商户代付对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <div style="color: green;font-weight: bold;font-size: 10px;">
        <div>
          <i class="el-icon-warning"></i>
          <span>&nbsp;&nbsp;&nbsp;温馨提示：请仔细核对商户余额是否充足，确保用户提现交易成功</span>
        </div>
        <div style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;（1）如果信息匹配，金额无法转账成功，线下进行转账，进行手动通知；</div>
        <div style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;（2）如果信息不匹配，请勿进行手动通知操作；</div>
      </div>
      <div/>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
        <el-col :span="11">
          <el-form-item label="商家账号" prop="siteId">
            <el-input v-model="form.siteId" :disabled="true"/>
          </el-form-item>
        </el-col>
        <el-col :span="13">
          <el-form-item label="玩家ID" prop="userId">
            <el-input v-model="form.userId" :disabled="true"/>
          </el-form-item>
        </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="提现金额" prop="amount">
              <el-input v-model="form.amount" :disabled="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="支付币种" prop="coinCode">
              <el-input v-model="form.coinCode" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="提现状态" prop="status">
              <dict-tag :options="dict.type.mbpay_withdraw_status" :value="form.status"/>
            </el-form-item>
          </el-col>
          <el-col :span="13">
            <el-form-item label="提现时间" prop="currency">
              <el-input v-model="form.payTime" :disabled="true"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="评审意见" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入评审意见" disabled/>
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
import {delDaip, exportDaip, getDaip, listDaip, updateDaip } from '@/api/pay/daip'

export default {
  name: 'Daip',
  dicts: ['mbpay_withdraw_status', 'tron_notify_state'],
  components: {},
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
      // 商户代付表格数据
      daipList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        siteId: undefined,
        orderId: undefined,
        userId: undefined,
        coinAddress: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        siteId: [
          { required: true, message: '商户ID不能为空', trigger: 'blur' }
        ],
        orderId: [
          { required: true, message: '商户订单号不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商户代付列表 */
    getList() {
      this.loading = true
      listDaip(this.queryParams).then(response => {
        this.daipList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        siteId: undefined,
        orderId: undefined,
        userId: undefined,
        productName: undefined,
        amount: undefined,
        currency: undefined,
        coinAmount: undefined,
        coinCode: undefined,
        coinAddress: undefined,
        status: '0',
        notifySucceed: undefined,
        notifyTimes: undefined,
        lastNotifyTime: undefined,
        nextNotifyTime: undefined,
        payTime: undefined,
        notifyUrl: undefined,
        transactionId: undefined,
        remark: undefined,
        createTime: undefined,
        updateTime: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加商户代付'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDaip(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改提现订单'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDaip(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除商户代付编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delDaip(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有商户代付数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportDaip(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    }
  }
}
</script>
