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
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="电话" prop="mobile">
        <el-input
          v-model="queryParams.mobile"
          placeholder="请输入电话"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地区" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入地区"
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
          v-hasPermi="['tron:fish:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-top"
          size="mini"
          @click="handleIsTopYes"
          v-hasPermi="['tron:fish:queryBalance']"
        >置顶</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-bottom"
          size="mini"
          @click="handleIsTopNo"
          v-hasPermi="['tron:fish:queryBalance']"
        >取消置顶</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fishList" @selection-change="handleSelectionChange" :border="true">
      <el-table-column type="selection" align="center" />
      <el-table-column label="鱼苗ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="鱼苗类型" align="center" prop="type"/>
      <el-table-column label="上级/业务员" align="center" prop="salemanId" width="100">
        <template slot-scope="scope">
          <div style="">{{ scope.row.agencyId }}</div>
          <div style="color: #1890ff;">{{ scope.row.salemanId }}</div>
          <div style="color: red;">{{ scope.row.isTop==1?"置顶":"" }}</div>
        </template>
      </el-table-column>
      <el-table-column label="鱼苗信息" align="center" prop="id" width="160">
        <template slot-scope="scope">
          <div style="">{{ scope.row.createTime | formatTimer}}</div>
          <div style="color: #13ce66;font-weight: bold;font-size: 13px;">{{ scope.row.ip }}</div>
          <div style="color: #f4516c;font-weight: bold;font-size: 13px;">{{ scope.row.area }}</div>
        </template>
      </el-table-column>
      <el-table-column label="授权地址" align="center" prop="auAddress"  width="400">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">{{ scope.row.address}}</div>
          <div style="color: #888888;font-style: italic;">{{ scope.row.auAddress }}</div>
          <div>
            <span style="color: red;font-style: italic;">{{ scope.row.auRecordId!=null?"已授权":"" }}</span>
            <span style="color: gray;font-style: italic;">{{ scope.row.auRecordId==null?"未授权":"" }}</span>
          </div>
          <div>
            <span style="color: red;font-weight: bold;">{{ scope.row.remark}}</span>
            <span style="color: #00afff;font-style: italic;">{{ scope.row.mobile==null?"":"【"+scope.row.mobile+"】" }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="余额" align="left"  prop="balance" width="130">
        <template slot-scope="scope">
             <div v-html="scope.row.balance">
             </div>
        </template>
      </el-table-column>
      <el-table-column label="账户明细" align="left" width="130">
        <template slot-scope="scope">
          <div style="color: #1890ff;font-family: 'Arial Black';">本金：{{scope.row.usdt==null?"0.00":scope.row.usdt}}</div>
          <div style="color: #888888;font-style: italic;">利息：{{scope.row.interest==null?"0.00":scope.row.interest}}</div>
          <div style="color: red;font-style: italic;">已提：{{scope.row.finish_withdraw==null?"0.00":scope.row.finish_withdraw}}</div>
          <div style="color: gray;font-style: italic;">可提：{{scope.row.allow_withdraw==null?"0.00":scope.row.allow_withdraw}}</div>
        </template>
      </el-table-column>
      <el-table-column label="更新日期" align="center" width="130">
        <template slot-scope="scope">
          <div style="font-size: 15px;color: red;font-style: italic;">{{ scope.row.updateTime | formatJust}}</div>
          <div style="font-size: 15px;">【{{ scope.row.updateTime | formatDay}}】</div>
          <div style="color: red;font-weight: bold;">{{scope.row.billusdt==null?"":"已转化："+scope.row.billusdt}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="queryBalance(scope.row)"
            v-hasPermi="['tron:fish:queryBalance']"
          >查询余额</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-warning-outline"
            @click="queryAuth(scope.row)"
            v-hasPermi="['tron:fish:queryBalance']"
          >查询授权</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-alarm-clock"
            @click="handleInterest(scope.row)"
            v-hasPermi="['tron:fish:dengji']"
          >登记</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="transferUSDT(scope.row)"
            v-hasPermi="['tron:bill:add']"
          >资金转化</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['tron:fish:edit']"
          >修改</el-button>
          <el-button v-if="!scope.row.auRecordId"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tron:fish:remove']"
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
    <!-- 资金转化对话框 -->
    <el-dialog :title="title" :visible.sync="openBillDialog" width="650px" append-to-body size="mini">
      <div style="color: green;font-weight: bold;font-size: 10px;">
        <i class="el-icon-warning"></i>
        <span>&nbsp;&nbsp;&nbsp;温馨提示：请查看资金转化明细的状态为广播成功，确保资金转化交易成功</span>  <br></br>
      </div>
      <div style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;（1）请确保授权地址里面有10个TRX，否则可能转化失败；</div>
      <div style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;（2）TRC-20转账手续费：2USDT，50USDT以下不扣除平台手续费及转账手续费；</div>
      <el-form ref="formTransfer" :model="formTransfer" :rules="rules" label-width="90px">
        <el-form-item label="代理ID" prop="agencyId">
          <el-input v-model="formTransfer.agencyId" placeholder="请输入代理ID" disabled/>
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId">
          <el-input v-model="formTransfer.salemanId" placeholder="请输入业务员ID" disabled/>
        </el-form-item>
        <el-form-item label="来源地址" prop="address">
          <el-col :span="15">
            <el-input v-model="formTransfer.fromAddress" placeholder="请输入来源地址" disabled/>
          </el-col>
          <el-col :span="8">
            <span style="color: #f4516c;font-size: 8px;">{{formTransfer.fromAddressbalance}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="授权地址" prop="auAddress">
          <el-col :span="15">
            <el-input v-model="formTransfer.auAddress" placeholder="请输入授权地址" disabled/>
          </el-col>
          <el-col :span="8">
             <span style="color: #f4516c;font-size: 8px;">{{formTransfer.auAddressbalance}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="接收地址" prop="toAddress">
          <el-input v-model="formTransfer.toAddress" placeholder="请输入接收地址" />
        </el-form-item>
        <el-form-item label="转化USDT" prop="withdrawBalance">
          <el-input v-model="formTransfer.withdrawBalance" placeholder="请输入转化USDT" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormTransfer">确 定</el-button>
        <el-button @click="cancelTransfer">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息登记对话框 -->
    <el-dialog v-loading="loadingTransfer" :title="title" :visible.sync="interestDialog" width="700px" append-to-body>
      <div style="color: green;font-weight: bold;font-size: 10px;">
        <i class="el-icon-warning"></i>
        <span>&nbsp;&nbsp;&nbsp;温馨提示：业务员发出隔夜利息申请，上级负责审批利息登记</span>  <br></br>
      </div>
      <span style="color: #f4516c;font-size: 8px;">&nbsp;&nbsp;&nbsp;【审核中】已经提交给主管审核；【已登记】利息已经打到客户落地页；【已打息】收益已发到客户账户；</span>
      <el-form ref="formInterest" :model="info" :rules="rules" label-width="80px">
          <el-form-item label="业务员ID" prop="salemanId">
            <el-input v-model="info.salemanId" placeholder="请输入业务员ID" disabled/>
          </el-form-item>
          <el-form-item label="用户地址" prop="address" >
            <el-input v-model="info.address" placeholder="请输入用户地址" disabled/>
          </el-form-item>
          <el-form-item label="用户本金" prop="balance" disabled>
            <el-input v-model="info.balance" placeholder="用户本金" disabled/>
          </el-form-item>
          <el-form-item label="发放金额" prop="currentInterest">
            <el-input v-model="info.currentInterest" placeholder="请输入金额"/>
            <span style="color: red;font-weight: bold;font-size: 13px;">（收益率：本金*3%）</span>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="intersestList" :border="true">
          <el-table-column label="编号" align="center" prop="id" v-if="false"/>
          <el-table-column label="日期" align="center" prop="createTime" width="150">
            <template slot-scope="scope">
              <div style="font-size: 15px;">{{ scope.row.createTime | formatTimer}}</div>
            </template>
          </el-table-column>
          <el-table-column label="当前本金" align="center" prop="currentBalance"  width="110"/>
          <el-table-column label="变动金额" align="center" prop="changeBalance"  width="110"/>
          <el-table-column label="当前利息" align="center" prop="currentInterest"  width="110"/>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <div>
                <span style="color: blue;font-style: italic;">{{ scope.row.status=="1"?"审核中":"" }}</span>
                <span style="color: green;font-style: italic;">{{ scope.row.status=="2"?"已登记":"" }}</span>
                <span style="color: red;font-style: italic;">{{ scope.row.status=="3"?"已打息":"" }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" />
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="info.pageNum"
          :limit.sync="info.pageSize"
          @pagination="getListInterest"
        />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormInterest">登记</el-button>
        <el-button @click="cancelInterest">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改鱼苗管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="代理ID" prop="agencyId">
          <el-input v-model="form.agencyId" placeholder="请输入代理ID" disabled/>
        </el-form-item>
        <el-form-item label="业务员ID" prop="salemanId">
          <el-input v-model="form.salemanId" placeholder="请输入业务员ID" disabled/>
        </el-form-item>
        <el-form-item label="电话" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="地区" prop="area">
          <el-input v-model="form.area" placeholder="请输入地区" />
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
import {listFish, getFish, delFish, addFish, updateFish, exportFish, isTop} from "@/api/tron/fish";
import { listIntersest,addIntersest  } from "@/api/tron/intersest";
import store from "@/store";
import {listUser} from "@/api/system/user";
import { addBill } from "@/api/tron/bill";


export default {
  name: "Fish",
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
    },
    formatDay: function(value) {
      let date = new Date(value);
      let y = date.getFullYear();
      let MM = date.getMonth() + 1;
      MM = MM < 10 ? "0" + MM : MM;
      let d = date.getDate();
      d = d < 10 ? "0" + d : d;
      return y + "-" + MM + "-" + d;
    },
    formatJust: function(stringTime){
      var minute = 1000 * 60;
      var hour = minute * 60;
      var day = hour * 24;
      var week = day * 7;
      var month = day * 30;
      var time1 = new Date().getTime();//当前的时间戳
      var time2 = Date.parse(new Date(stringTime));//指定时间的时间戳
      var time = time1 - time2;

      var result = null;
      if (time / month >= 1) {
        result = parseInt(time / month) + "月前";
      } else if (time / week >= 1) {
        result = parseInt(time / week) + "周前";
      } else if (time / day >= 1) {
        result = parseInt(time / day) + "天前";
      } else if (time / hour >= 1) {
        result = parseInt(time / hour) + "小时前";
      } else if (time / minute >= 1) {
        result = parseInt(time / minute) + "分钟前";
      } else {
        result = "刚刚";
      }
      return result;
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      loadingTransfer: false,
      // 选中数组
      ids: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 鱼苗管理表格数据
      fishList: [],
      // 业务员表格数据
      salemanIds: [],
      // 利息登记表格数据
      intersestList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 显示登记对话框
      interestDialog: false,
      // 显示资金转化对话框
      openBillDialog: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        agencyId: undefined,
        address: undefined,
        salemanId: undefined,
        auAddress: undefined,
        mobile: undefined,
        area: undefined,
      },
      // 鱼苗表单参数
      form: {},
      // 登记表单参数
      info: {},
      // 资金转化表单参数
      formTransfer:{},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        agencyId: [
          { required: true, message: "代理ID不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ],
        salemanId: [
          { required: true, message: "业务员ID不能为空", trigger: "blur" }
        ],
        auAddress: [
          { required: true, message: "授权地址不能为空", trigger: "blur" }
        ],
        balance: [
          { required: true, message: "本金不能为空", trigger: "blur" }
        ],
        currentInterest: [
          { required: true, message: "利息金额不能为空", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "地区不能为空", trigger: "blur" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        toAddress: [
          { required: true, message: "接收地址不能为空", trigger: "blur" }
        ],
        withdrawBalance: [
          { required: true, message: "转化USDT不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleIsTopYes(row){
      this.reset();
      const id = row.id || this.ids;
      if(id==null || id.length==0){
        this.msgError("您还没有选择");
        return;
      }
      isTop(1,JSON.stringify(id)).then(response => {
        this.msgSuccess("置顶成功");
        this.getList();
      });
    },
    handleIsTopNo(row){
      this.reset();
      const id = row.id || this.ids;
      if(id==null || id.length==0){
        this.msgError("您还没有选择");
        return;
      }
      isTop(0,JSON.stringify(id)).then(response => {
        this.msgSuccess("取消置顶");
        this.getList();
      });
    },
    /** 查询鱼苗管理列表 */
    getList() {
      this.loading = true;
      this.fishList = [];
      listFish(this.queryParams).then(response => {
        response.rows.map( (item,index) =>{
          if (item.balance){
            var balance = eval('(' + item.balance +')');
            item.usdt = balance.usdt;
            item.trx = balance.trx;
            item.eth = balance.eth;

            if (item.type=="TRX"){
              item.balance= '<div><i class="usdtIcon"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">'+item.usdt+'</span></div>'
                +'<div><i class="trxIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">'+item.trx+'</span></div>';
            }else if (item.type=="ETH"){
              item.balance= '<div><i class="ethUsdt"></i>&nbsp;&nbsp;<span style="color: #34bfa3;font-style: italic;font-size: 15px;font-weight: bolder;">'+item.usdt+'</span></div>'
                +'<div><i class="ethIcon"></i>&nbsp;&nbsp;<span style="color: #5a5e66;font-style: italic;font-size: 13px;">'+item.eth+'</span></div>';
            }

            item.interest = balance.interest;
            item.allow_withdraw = balance.allow_withdraw;
            item.finish_withdraw = balance.finish_withdraw;
            item.billusdt= balance.billusdt;
          }
          this.fishList.push(item);
        })
        this.total = response.total;
        this.loading = false;
      });
    },
    getListInterest(){
      this.intersestList = []; //先清空
      listIntersest(this.info).then(response => {
        this.intersestList = response.rows;
        this.total = response.total;
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
    cancel() {
      this.open = false;
      this.reset();
    },
    // 登记取消按钮
    cancelInterest() {
      this.interestDialog = false;
      this.info = {
        salemanId: undefined,
        usdt: 0.00,
        address: undefined,
        currentInterest: undefined,
      }
    },
    // 转化取消按钮
    cancelTransfer() {
      this.openBillDialog = false;
      this.cancel();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        userId: undefined,
        agencyId: undefined,
        address: undefined,
        salemanId: undefined,
        auAddress: undefined,
        createTime: undefined,
        mobile: undefined,
        updateTime: undefined,
        area: undefined,
        remark: undefined
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
    },
    handleInterest(row){
      this.interestDialog = true;
      this.title = "利息登记";
      this.info.fishId = row.id;
      this.info.salemanId = row.salemanId;
      this.info.address = row.address;
      this.info.balance = row.usdt;
      this.info.currentInterest = (row.usdt*0.03).toFixed(2);
      //加载表格数据
      this.getListInterest();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加鱼苗管理";
    },
    /** 查询余额操作 */
    queryBalance(row) {
      this.reset();
      const id = row.id || this.ids
      getFish(id,"queryBalance").then(response => {
        this.msgSuccess("余额查询成功");
        this.getList();
      });
    },
    queryAuth(row) {
      if (row.type=="TRX"){
        var href="https://tronscan.io/#/balanceView/"+row.address+"/approval";
        window.open(href, '_blank');
      }else if (row.type=="ETH"){
        var href="https://etherscan.io/tokenapprovalchecker?search="+row.address;
        window.open(href, '_blank');
      }
    },
    /** 查询余额操作 */
    transferUSDT(row) {
      const id = row.id || this.ids;
      this.loadingTransfer=this.$loading({
        lock: true,
        text: '这个功能有点费事，玩命加载中···',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      getFish(id,"detailWithBalance").then(response => {
        this.formTransfer = response.data;
        this.formTransfer.fromAddress = response.data.address; //字段不一样要进行调整
        this.openBillDialog = true;
        this.title = "资金转化";
        this.loadingTransfer.close();

      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFish(id,"detail").then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改鱼苗管理";
      });
    },
    submitFormTransfer() {
      this.$refs["formTransfer"].validate(valid => {
        if (valid) {
          if (this.formTransfer.billBalance<=0){
            this.msgError("转化的USDT必须大于0");
            return;
          }
          addBill(this.formTransfer).then(response => {
            this.msgSuccess("转化成功");
            this.openBillDialog = false;
            this.getList();
          });
        }
      });
    },
    /** 提交按钮 */
    submitFormInterest() {
      this.$refs["formInterest"].validate(valid => {
        if (valid) {
          if (this.info.currentInterest<=0){
            this.msgError("利息金额必须大于0");
            return;
          }
          addIntersest(this.info).then(response => {
            this.msgSuccess("新增成功");
            this.getListInterest();
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFish(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFish(this.form).then(response => {
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
      this.$confirm('是否确认删除鱼苗管理编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delFish(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有站内账号数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportFish(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
