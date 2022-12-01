package com.ruoyi.tron.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.tron.contract.TetherToken;
import com.ruoyi.tron.domain.TronAccountAddress;
import com.ruoyi.tron.domain.TronAuthAddress;
import com.ruoyi.tron.domain.TronEasonAddress;
import com.ruoyi.tron.service.ITronAccountAddressService;
import com.ruoyi.tron.service.ITronApiService;
import com.ruoyi.tron.service.ITronAuthAddressService;
import com.ruoyi.tron.service.ITronEasonAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.web3j.tx.Transfer.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * ETH接口管理
 *
 * @author eason
 * @date 2022-11-06
 */
@Service("ethApiServiceImpl")
public class EthApiServiceImpl implements ITronApiService {

    @Autowired
    private ITronAccountAddressService iTronAccountAddressService;
    @Autowired
    private ITronAuthAddressService iTronAuthAddressService;
    @Autowired
    private ITronEasonAddressService iTronEasonAddressService;

    @Override
    public String queryBalance(String auAddress) {
        // defaults to http://localhost:8545/
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));
        String eth = null;
        String usdt = null;
        try {
            EthGetBalance ethGetBalance = web3.ethGetBalance("0x" + auAddress, DefaultBlockParameterName.LATEST).send();
            eth = toDecimal(18, ethGetBalance.getBalance());
        } catch (Exception e) {
            eth = "0.00";
        }

//        LambdaQueryWrapper<TronAuthAddress> lqw2 = Wrappers.lambdaQuery();
//        lqw2.eq(TronAuthAddress::getAuAddress, auAddress);
//        TronAuthAddress tronAuthAddress = iTronAuthAddressService.getOne(lqw2);
//        Credentials credentials = Credentials.create(tronAuthAddress.getPrivatekey());
        Credentials credentials = Credentials.create("0xdAC17F958D2ee523a2206206994597C13D831ec7");
        TetherToken contract = TetherToken.load(
                "0xdAC17F958D2ee523a2206206994597C13D831ec7", web3, credentials, new DefaultGasProvider());
        try {
            BigInteger balanceOf = contract.balanceOf(auAddress).sendAsync().get();
            usdt = String.valueOf(balanceOf.doubleValue() / 1000000);
        } catch (Exception e) {
            usdt = "0.00";
        }
        String balance = String.format("{'eth':%s,'usdt':%s}", eth, usdt);
        return balance;
    }

    @Override
    public String queryTransactionbyid(String txId) {
        String info = "ERROR";
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));
        try {
            EthTransaction ethTransaction = web3.ethGetTransactionByHash(txId).send();
            if (ethTransaction.getTransaction() != null) {
                info = "SUCCESS";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public AjaxResult transferTRX(String formAddress, String toAddress, Double amount) {
        try {
            //（1）ETH转账申请
            Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));
            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(formAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            BigInteger amount2 = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, amount2);

            //（2）签名打包
            LambdaQueryWrapper<TronAccountAddress> lqw2 = Wrappers.lambdaQuery();
            lqw2.eq(TronAccountAddress::getAddress, formAddress);
            TronAccountAddress tronAccountAddress = iTronAccountAddressService.getOne(lqw2);
            Credentials credentials = Credentials.create(tronAccountAddress.getPrivateKey());

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            //（3）广播交易
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            if (ethSendTransaction.hasError()) {
                return AjaxResult.error(ethSendTransaction.getError().getMessage());
            } else {
                String transactionHash = ethSendTransaction.getTransactionHash();
                if (!transactionHash.isEmpty()) {
                    return AjaxResult.success("success");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("transferETH error");
    }

    @Override
    public AjaxResult transferUSDT(String formAddress, String toAddress, Double amount) throws Exception {
        //（1）USDT转账申请
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(formAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        BigInteger amount2 = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();

        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(amount2)),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        //（2）签名打包
        String contractAddress = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, contractAddress, encodedFunction);

        LambdaQueryWrapper<TronAccountAddress> lqw2 = Wrappers.lambdaQuery();
        lqw2.eq(TronAccountAddress::getAddress, formAddress);
        TronAccountAddress tronAccountAddress = iTronAccountAddressService.getOne(lqw2);
        Credentials credentials = Credentials.create(tronAccountAddress.getPrivateKey());

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        //（3）广播交易
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        if (ethSendTransaction.hasError()) {
            return AjaxResult.error(ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            if (!transactionHash.isEmpty()) {
                return AjaxResult.success("success");
            }
        }
        return AjaxResult.error("transferETH USDT error");
    }

    @Override
    public AjaxResult transferUSDTForEASON(String agencyId, String formAddress, String toAddress, Double amount) throws Exception {
        //（1）USDT转账申请
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(formAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        BigInteger amount2 = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();

        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(amount2)),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        //（2）签名打包
        LambdaQueryWrapper<TronEasonAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(TronEasonAddress::getAgencyId, agencyId);
        lqw.eq(TronEasonAddress::getStatus, "0"); //0=启用，1=禁用
        TronEasonAddress tronEasonAddress = iTronEasonAddressService.getOne(lqw);

        String contractAddress = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, contractAddress, encodedFunction);

        Credentials credentials = Credentials.create(tronEasonAddress.getPrivatekey());

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        //（3）广播交易
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
        if (ethSendTransaction.hasError()) {
            return AjaxResult.error(ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            if (!transactionHash.isEmpty()) {
                return AjaxResult.success("success");
            }
        }
        return AjaxResult.error("transferUSDTForEASON USDT error");
    }

    public static String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
        return balance;
    }

    @Override
    public AjaxResult transferFrom(String formAddress, String auAddress, String toAddress, Double amount) throws Exception {
        //（1）三方账户USDT转账申请
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b"));

        LambdaQueryWrapper<TronAuthAddress> lqw2 = Wrappers.lambdaQuery();
        lqw2.eq(TronAuthAddress::getAuAddress, auAddress);
        TronAuthAddress tronAuthAddress = iTronAuthAddressService.getOne(lqw2);

        Credentials credentials = Credentials.create(tronAuthAddress.getPrivatekey());
        String contractAddress = "0xdAC17F958D2ee523a2206206994597C13D831ec7";

        TetherToken contract = TetherToken.load(contractAddress, web3, credentials, new DefaultGasProvider());

        BigInteger amount2 = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();
        RemoteFunctionCall<TransactionReceipt> functionCall = contract.transferFrom(formAddress, toAddress, amount2);
        String result3 = functionCall.encodeFunctionCall();
        return AjaxResult.error(result3);
    }


}
