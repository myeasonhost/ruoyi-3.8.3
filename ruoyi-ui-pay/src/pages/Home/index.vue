<!-- 主界面 -->
<template>
  <div>
    <div class="wrapper" v-show="url">
      <header>
        <div class="left el-icon-arrow-left"></div>
        <div class="title">{{ $t('text8') }}</div>
        <div class="right"></div>
      </header>
      <div class="content-box">
        <div class="content">
          <div v-show="Number(orderInfo.status) === 1">
            <div class="type">
              <div class="currency-type">{{ $t('text1') }}</div>
              <div class="currency-type amount" @click="copyAddress(amount)">
                <div>{{ amount }} &nbsp;{{ currency }}</div>
                <i style="color:#409eff;font-size:14px;margin-left:10px" class="el-icon-document-copy"></i>
              </div>
              <div class="address" @click="copyAddress(url)">
                <div class="address-box">{{url}}</div>
                <i style="color:#409eff;font-size:14px;margin-left:10px" class="el-icon-document-copy"></i>
              </div>
            </div>
            <div class="protocol">
              <div class="protocol-type"></div>
              <div class="protocol-code" ref="qrCodeUrl"></div>
            </div>
            <div class="tips" v-if="Number(time.ss)">{{ time.hh }} : {{ time.mm }} : {{ time.ss }}</div>
            <div class="tips" v-else>
              <div>{{ $t('text7') }}</div>
            </div>
          </div>
          <div class="status" style="color: #67C23A;" v-show="Number(orderInfo.status) === 2">{{ $t('text11') }}</div>
          <div class="status" style="color: #F56C6C;" v-show="Number(orderInfo.status) === 3">{{ $t('text7') }}</div>
          <div class="docs-box">
            <div class="docs">
              <div>{{ $t('text2') }}</div>
              <div>{{ $t('text3') }}</div>
              <div>{{ $t('text4') }}</div>
              <div>{{ $t('text5') }}</div>
              <div>{{ $t('text6') }}</div>
            </div>
          </div>
          <div class="select-box">
            <el-select style="width:100px" size="mini" v-model="language" placeholder="请选择">
              <el-option label="简体中文" value="zh-CN"></el-option>
              <el-option label="English" value="en-US"></el-option>
            </el-select>
          </div>
        </div>
      </div>
      <div v-show="!url" style="margin-top: 100px;text-align: center;font-size: 14px;">
        {{ $t('text10') }}
      </div>
      </div>
  </div>
</template>

<script src="./js/PageHome.js"></script>
<style scoped src="./css/PageHome.css"></style>
