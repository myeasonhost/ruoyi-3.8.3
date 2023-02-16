<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商户ID" prop="siteId">
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

      <el-form-item label="支付状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.three_status"
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
          v-hasPermi="['pay:order:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column label="支付订单号" align="center" prop="id" v-if="false"/>
      <el-table-column label="商户ID" align="center" prop="siteId"/>
      <el-table-column label="商户订单号" align="center" prop="orderId"/>
      <el-table-column label="用户名" align="center" prop="userId"/>
      <el-table-column label="产品名" align="center" prop="productName"/>
      <el-table-column label="金额" align="center" prop="amount"/>
      <el-table-column label="支付明细" align="center">
        <template slot-scope="scope">
          <div style="color: #666666;">支付金额：{{ scope.row.coinAmount }}</div>
          <div style="color: #666666;">支付币种：{{ scope.row.coinAmount }}</div>
          <div style="color: #666666;">订单币种：{{ scope.row.currency }}</div>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.three_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="通知状态" align="center" prop="notifySucceed">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.mbpay_notify_status" :value="scope.row.notifySucceed"/>
        </template>
      </el-table-column>
      <el-table-column label="订单过期时间" align="center" prop="timeout">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.timeout, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
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
            v-hasPermi="['pay:order:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['pay:order:remove']"
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

    <!-- 添加或修改支付订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商户ID" prop="siteId">
          <el-input v-model="form.siteId" placeholder="请输入商户ID"/>
        </el-form-item>
        <el-form-item label="商户订单号" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入商户订单号"/>
        </el-form-item>
        <el-form-item label="用户名" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="产品名" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名"/>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入金额"/>
        </el-form-item>
        <el-form-item label="订单币种单位。支持USD" prop="currency">
          <el-input v-model="form.currency" placeholder="请输入订单币种单位。支持USD"/>
        </el-form-item>
        <el-form-item label="支付金额" prop="coinAmount">
          <el-input v-model="form.coinAmount" placeholder="请输入支付金额"/>
        </el-form-item>
        <el-form-item label="支付币种" prop="coinCode">
          <el-input v-model="form.coinCode" placeholder="请输入支付币种"/>
        </el-form-item>
        <el-form-item label="0=支付中,2=支付成功，3=支付超时">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="0=未通知，1=通知成功，2=通知失败" prop="notifySucceed">
          <el-input v-model="form.notifySucceed" placeholder="请输入0=未通知，1=通知成功，2=通知失败"/>
        </el-form-item>
        <el-form-item label="通知次数" prop="notifyTimes">
          <el-input v-model="form.notifyTimes" placeholder="请输入通知次数"/>
        </el-form-item>
        <el-form-item label="订单过期时间" prop="timeout">
          <el-input v-model="form.timeout" placeholder="请输入订单过期时间"/>
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable size="small"
                          v-model="form.payTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择支付时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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
import { addOrder, delOrder, exportOrder, getOrder, listOrder, updateOrder } from '@/api/pay/order'

export default {
  name: 'Order',
  dicts: ['three_status', 'mbpay_notify_status'],
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
      // 支付订单表格数据
      orderList: [],
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
        ],
        userId: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: '产品名不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '产品名不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询支付订单列表 */
    getList() {
      this.loading = true
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows
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
        status: '0',
        notifySucceed: undefined,
        notifyTimes: undefined,
        lastNotifyTime: undefined,
        nextNotifyTime: undefined,
        timeout: undefined,
        payTime: undefined,
        notifyUrl: undefined,
        redirectUrl: undefined,
        cashierUrl: undefined,
        qrcodeUrl: undefined,
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
      this.title = '添加支付订单'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改支付订单'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrder(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
              this.msgSuccess('新增成功')
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
      this.$confirm('是否确认删除支付订单编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delOrder(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有支付订单数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return exportOrder(queryParams)
      }).then(response => {
        this.download(response.msg)
      })
    }
  }
}
</script>
