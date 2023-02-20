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
      <el-form-item label="地址状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['pay:address:add']"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="addressList" @selection-change="handleSelectionChange">
      <el-table-column label="编号" align="center" prop="id" v-if="false"/>
      <el-table-column label="代理ID" align="center" prop="agencyId"/>
      <el-table-column label="地址类型" align="center" prop="addressType" width="80"/>
      <el-table-column label="用户地址" align="center" prop="address" width="400"/>
      <el-table-column label="余额集合" align="left" prop="balance" width="150">
        <template slot-scope="scope">
          <div v-html="scope.row.balance">
          </div>
        </template>
      </el-table-column>
      <el-table-column label="累计收款金额(USDT)" align="center" prop="totalAmount" width="150"/>
      <el-table-column label="帐号状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="queryBalance(scope.row)"
            v-hasPermi="['pay:address:query']"
          >查询余额
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['pay:address:edit']"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.status==1"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['pay:address:remove']"
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

    <!-- 添加或修改收款地址对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <div style="color: green;font-weight: bold;font-size: 10px;">
          <i class="el-icon-warning"></i>
          <span>&nbsp;&nbsp;&nbsp;温馨提示：请确保至少一条地址信息处于启用状态，否则无法发起订单收款。</span><br></br>
        </div>
        <el-form-item label="代理ID" prop="agencyId" v-hasPermi="['*:*:*']">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID"/>
        </el-form-item>
        <el-form-item label="地址类型" prop="addressType">
          <el-select v-model="form.addressType" placeholder="请选择地址类型">
            <el-option label="TRX" value="TRX"/>
          </el-select>
        </el-form-item>
        <el-form-item label="用户地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入波场网络以T开头的地址"/>
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
import { addAddress, changeStatus, delAddress, getAddress, listAddress, updateAddress } from '@/api/pay/address'

export default {
  name: 'Address',
  dicts: ['sys_normal_disable'],
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
      // 收款地址表格数据
      addressList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agencyId: undefined,
        addressType: 'TRX',
        address: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        addressType: [
          { required: true, message: '地址类型不能为空', trigger: 'change' }
        ],
        address: [
          { required: true, message: '用户地址不能为空', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '备注不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询收款地址列表 */
    getList() {
      this.loading = true
      this.addressList = []
      listAddress(this.queryParams).then(response => {
        response.rows.map((item, index) => {
          if (item.balance) {
            var balance = eval('(' + item.balance + ')')
            item.usdt = balance.usdt
            item.trx = balance.trx
            item.eth = balance.eth

            if (item.addressType == 'TRX') {
              item.balance = '<div><i class="usdtIcon"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">' + item.usdt + '</span></div>'
                + '<div><i class="trxIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">' + item.trx + '</span></div>'
            } else if (item.addressType == 'ETH') {
              item.balance = '<div><i class="ethUsdt"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">' + item.usdt + '</span></div>'
                + '<div><i class="ethIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">' + item.eth + '</span></div>'
            }
          }
          this.addressList.push(item)
        })

        this.total = response.total
        this.loading = false
      })
    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.status === '0' ? '启用' : '停用'
      this.$modal.confirm('确认要' + text + '【' + row.address + '】地址吗？').then(function() {
        return changeStatus(row.id, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(function() {
        row.status = row.status === '0' ? '1' : '0'
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
        agencyId: undefined,
        addressType: 'TRX',
        address: undefined,
        balance: undefined,
        totalAmount: undefined,
        status: '0',
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
      this.title = '添加收款地址'
    },
    /** 查询余额操作 */
    queryBalance(row) {
      this.reset()
      const id = row.id || this.ids
      getAddress(id, 'queryBalance').then(response => {
        this.msgSuccess('余额查询成功')
        this.getList()
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getAddress(id, 'detail').then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改收款地址'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAddress(this.form).then(response => {
              this.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addAddress(this.form).then(response => {
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
      this.$confirm('是否确认删除收款地址编号为"' + ids + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delAddress(ids)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      })
    }
  }
}
</script>
