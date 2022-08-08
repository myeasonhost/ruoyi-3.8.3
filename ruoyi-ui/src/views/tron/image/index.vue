<template>
  <div class="image01">
    <el-row>
      <el-button type="primary" @click="open1">【BitKeep】首页</el-button>
      <el-button type="success" @click="open2">【BitKeep】转账</el-button>
      <el-button type="warning" @click="open3">【流动性挖矿】收益</el-button>
    </el-row>
    <el-tabs type="border-card">
      <el-tab-pane>
        <span slot="label"><i class="el-icon-s-home"></i> 钱包首页</span>
        <div>
          <el-form ref="formModel1" :model="model1" :rules="rules" label-width="90px">
          <div class="layui-form-title">基础界面</div>
          <div>
            <span class="layui-form-label">信号强度</span>
            <el-dropdown class="layui-select" size="mini" split-button type="info" @command="handleCommand1">
              {{ model1.xinhao }}
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="4格">4格</el-dropdown-item>
                <el-dropdown-item command="3格">3格</el-dropdown-item>
                <el-dropdown-item command="2格">2格</el-dropdown-item>
                <el-dropdown-item command="1格">1格</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-dropdown class="layui-select" size="mini" split-button type="info" @command="handleCommand2">
              {{ model1.wang }}
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="WIFI">WIFI</el-dropdown-item>
                <el-dropdown-item command="5G">5G</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-dropdown class="layui-select" size="mini" split-button type="info" @command="handleCommand3">
              {{ model1.dian }}
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="100%">100%</el-dropdown-item>
                <el-dropdown-item command="90%">90%</el-dropdown-item>
                <el-dropdown-item command="80%">80%</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div>
            <el-form-item label="当前时间" prop="time">
              <el-time-picker class="layui-select" size="mini" prop="time"
                v-model="model1.time"
                placeholder="选择时间点">
              </el-time-picker>
            </el-form-item>
          </div>

          <div class="layui-form-title">基本参数</div>
          <div>
            <div>
              <el-form-item label="钱包地址" prop="address">
                <el-input style="padding: 0 8px;width: 30%;" v-model="model1.address" placeholder="请输入钱包地址" size="mini"  prop="address"></el-input>
              </el-form-item>
            </div>
            <div>
              <span class="layui-form-label">转账未读</span>
              <el-input  class="layui-select" v-model="model1.noreadTansfer" placeholder="请输入数量" size="mini"></el-input>
            </div>
            <div>
              <span class="layui-form-label">系统未读</span>
              <el-input  class="layui-select" v-model="model1.noreadSystem" placeholder="请输入数量" size="mini"></el-input>
            </div>
          </div>

          <div class="layui-form-title">TRX</div>
          <div>
            <div>
              <span class="layui-form-label">当前价格</span>
              <el-input  class="layui-select" v-model="model1.trxPrice" placeholder="请输入价格" size="mini"></el-input>
              <span style="color: red;">【请参照bitkeep价格设置】</span>
            </div>
            <div>
              <span class="layui-form-label">持有数量</span>
              <el-input  class="layui-select" v-model="model1.trxNum" placeholder="请输入数量" size="mini"></el-input>
            </div>
          </div>

          <div class="layui-form-title">USDT</div>
          <div>
            <div>
              <span class="layui-form-label">当前价格</span>
              <el-input  class="layui-select" v-model="model1.usdtPrice" placeholder="请输入价格" size="mini"></el-input>
            </div>
            <div>
              <span class="layui-form-label">持有数量</span>
              <el-input  class="layui-select" v-model="model1.usdtNum" placeholder="请输入数量" size="mini"></el-input>
            </div>
          </div>
          </el-form>
          <el-button style="text-align: center;margin-top: 20px;" type="primary" @click="saveAs1('formModel1')">保存当前设置</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="转账记录">
        <span slot="label"><i class="el-icon-s-promotion"></i> 转账记录</span>
        <div class="layui-form-title">转账记录</div>
        <div>
          <el-form ref="formModel2" :model="model2" :rules="rules2" label-width="90px">
            <div>
              <el-form-item label="添加时间" prop="optTime">
                <el-date-picker  class="layui-select" v-model="model2.optTime" type="datetime"
                  placeholder="选择日期时间">
                </el-date-picker>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="类型" prop="tranferType">
                <el-dropdown class="layui-select" size="mini" split-button type="info" @command="handleCommand4">
                  {{ model2.tranferType }}
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="转入">转入</el-dropdown-item>
                    <el-dropdown-item command="转出">转出</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="币种" prop="coinType">
                <el-dropdown class="layui-select" size="mini" split-button type="info" @command="handleCommand5">
                  {{ model2.coinType }}
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="USDT">USDT</el-dropdown-item>
                    <el-dropdown-item command="TRX">TRX</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="钱包地址" prop="address">
                <el-input   style="padding: 0 8px;width: 30%;" v-model="model2.address" placeholder="请输入钱包地址" size="mini"></el-input>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="数量" prop="num">
                <el-input  class="layui-select" v-model="model2.num" placeholder="请输入数量" size="mini"></el-input>
              </el-form-item>
            </div>
          </el-form>
          <el-button style="text-align: center;margin-top: 20px;" type="primary" @click="saveAs2('formModel2')">增加转账记录</el-button>
          <el-button style="text-align: center;margin-top: 20px;" type="primary" @click="getList()">刷新转账记录</el-button>

          <!--转账记录表格-->
          <el-table v-loading="loading" :data="config02List">
            <el-table-column label="ID" align="center" prop="id" width="80px;"/>
            <el-table-column label="发生时间" align="center" prop="optTime" width="200px;">
              <template slot-scope="scope">
                <div style="font-size: 15px;">{{ scope.row.optTime | formatTimer}}</div>
              </template>
            </el-table-column>
            <el-table-column label="类型" align="center" prop="tranferType" width="80px;"/>
            <el-table-column label="币种" align="center" prop="coinType" width="80px;"/>
            <el-table-column label="钱包地址" align="center" prop="address"  width="400px;"/>
            <el-table-column label="数量" align="center" prop="num"  width="80px;"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleAddOneDay(scope.row)"
                  v-hasPermi="['tron:config02:edit']"
                >增加一天</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleReduceOneDay(scope.row)"
                  v-hasPermi="['tron:config02:edit']"
                >减少一天</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['tron:config02:remove']"
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

        </div>
      </el-tab-pane>
      <el-tab-pane label="挖矿收益">
        <span slot="label"><i class="el-icon-finished"></i> 挖矿收益</span>
        <div class="layui-form-title">基本参数</div>
        <div>
          <el-form ref="formModel3" :model="model1" :rules="rules" label-width="90px">
            <div>
              <el-form-item label="钱包地址" prop="address">
                <el-input style="padding: 0 8px;width: 30%;" v-model="model1.address" placeholder="请输入钱包地址" size="mini"  prop="address"></el-input>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="总收益" prop="shouyi">
                <el-input  class="layui-select" v-model="model1.shouyi" placeholder="请输入数量" size="mini"></el-input>
              </el-form-item>
            </div>
            <div>
              <el-form-item label="待提现" prop="tixian">
                <el-input  class="layui-select" v-model="model1.tixian" placeholder="请输入数量" size="mini"></el-input>
              </el-form-item>
            </div>
          </el-form>
          <el-button style="text-align: center;margin-top: 20px;" type="primary" @click="saveAs1('formModel3')">保存当前设置</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {addConfig01, listConfig01} from "@/api/tron/config01";
import {addConfig02, listConfig02, delConfig02, updateConfig02} from "@/api/tron/config02";

export default {
  name: "Image01",
  components: {},
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
        loading: false,
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
        // 图片配置02表格数据
        config02List: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          agencyId: undefined,
          salemanId: undefined,
          opttime: undefined,
          tranferType: undefined,
          coinType: undefined,
          address: undefined,
          num: undefined,
          shouyi: undefined,
          tixian: undefined
        },
        model1: {
          id: undefined,
          time: undefined,
          wang: "WIFI",
          dian: "100%",
          xinhao: "4格",
          address: undefined,
          noreadTansfer: 0,
          noreadSystem: 0,
          trxPrice: 0.06878,
          trxNum: 100,
          usdtPrice: 1,
          usdtNum: 1000
        },
        model2: {
          optTime: undefined,
          tranferType: "转入",
          coinType: "USDT",
          address: undefined,
          num: undefined
        },
      // 表单校验
      rules: {
        time: [
          { required: true, message: "当前时间不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ],
        shouyi: [
          { required: true, message: "总收益不能为空", trigger: "blur" }
        ],
        tixian: [
          { required: true, message: "待提现不能为空", trigger: "blur" }
        ]
      },
      rules2: {
        tranferType: [
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
        coinType: [
          { required: true, message: "币种不能为空", trigger: "blur" }
        ],
        num: [
          { required: true, message: "数量不能为空", trigger: "blur" }
        ],
        optTime: [
          { required: true, message: "当前时间不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "地址不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    listConfig01().then(response => {
      if (response.rows.length!=0){
        this.model1=response.rows[0];
        this.model2.address=this.model1.address;
        //加载转账记录
        this.getList();
      }
    });

  },
  methods: {
    handleCommand1(command){
      this.model1.xinhao=command;
    },
    handleCommand2(command){
      this.model1.wang=command;
    },
    handleCommand3(command){
      this.model1.dian=command;
    },
    handleCommand4(command){
      this.model2.tranferType=command;
    },
    handleCommand5(command){
      this.model2.coinType=command;
    },
    open1(){
      var href="/image1.html?id="+this.model1.id;
      window.open(href, '_blank');
    },
    open2(){
      var href="/image2.html?id="+this.model1.id;
      window.open(href, '_blank');
    },
    open3(){
      var href="/image3.html?id="+this.model1.id;
      window.open(href, '_blank');
    },
    /** 查询图片配置02列表 */
    getList() {
      this.loading = true;
      listConfig02(this.queryParams).then(response => {
        this.config02List = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleAddOneDay(row){
      let date = new Date(row.optTime);
      let date1 = new Date(date.setDate(date.getDate()+1));
      row.optTime=date1;
      updateConfig02(row).then(response => {
        this.msgSuccess("添加一天修改成功");
        this.open = false;
        this.getList();
      });
    },
    handleReduceOneDay(row){
      let date = new Date(row.optTime);
      let date1 = new Date(date.setDate(date.getDate()-1));
      row.optTime=date1;
      updateConfig02(row).then(response => {
        this.msgSuccess("减少一天修改成功");
        this.open = false;
        this.getList();
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
        return delConfig02(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    saveAs1(formName){
      this.$refs[formName].validate(valid => {
        if (valid) {
          addConfig01(this.model1).then(response => {
            this.msgSuccess("配置添加成功");
            listConfig01().then(response => {
              if (response.rows){
                this.model1=response.rows[0];
              }
            });
          });
        }
      });
    },
    saveAs2(formName){
      this.$refs[formName].validate(valid => {
        if (valid) {
          addConfig02(this.model2).then(response => {
            this.msgSuccess("配置添加成功");
            this.getList();
          });
        }
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.image01{
  color: #666;
  padding: 0 10px;
  font-size: 13px;
}
.image01 .layui-form-title{
  height: 20px;
  margin-top: 10px;
  color: #7a6df0;
  font-size: 13px;
  text-align: left;
  font-weight: bold;
}
.image01 .layui-form-label{
  width: auto!important;
  height: 32px;
  padding: 0 8px;
  line-height: 32px;
  border-color: #E9E9E9;
  font-size: 10px;
}
.el-form-item__label{
  text-align: left;
  vertical-align: middle;
  float: left;
  font-size: 10px;
  color: #606266;
  line-height: 20px;
  padding: 0 12px 0 0;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}


.image01 .layui-select{
  padding: 0 8px;
  width: auto!important;
  text-align: left;
}
</style>
