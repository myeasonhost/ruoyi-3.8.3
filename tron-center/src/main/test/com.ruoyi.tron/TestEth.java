package com.ruoyi.tron;

import com.ruoyi.tron.contract.TetherToken;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static com.ruoyi.tron.service.impl.TronApiServiceImpl.toDecimal;

public class TestEth {
    public static void main(String[] args) throws Exception {
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/23efd2dc07164509a9089b9960c3f92b")); // defaults to http://localhost:8545/
//        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
//        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//        System.out.println(clientVersion);
        // 这里要填写真实的钱包地址
        EthGetBalance ethGetBalance = web3
                .ethGetBalance("0xce321267d0E59c4d56E8150a24fFa6bBFc912351", DefaultBlockParameterName.LATEST).send();
        if (ethGetBalance != null) {
            System.out.println("余额1:" + toDecimal(18, ethGetBalance.getBalance()));
            System.out.println("余额2:" + Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER));
        }

//        Function function = new Function("balanceOf",
//                Arrays.asList(new Address("0x4DE23f3f0Fb3318287378AdbdE030cf61714b2f3")),
//                Arrays.asList(new TypeReference<Address>() {
//                }));
//
//        String encode = FunctionEncoder.encode(function);
//        Transaction ethCallTransaction = Transaction.createEthCallTransaction("0x4DE23f3f0Fb3318287378AdbdE030cf61714b2f3", "0xdAC17F958D2ee523a2206206994597C13D831ec7", encode);
//        EthCall ethCall = web3.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//        String value = ethCall.getResult();
//        System.out.println("getERC20Balance balance : " + value);

        //加载合约
        Credentials credentials = Credentials.create("1");
        TetherToken contract = TetherToken.load(
                "0xdAC17F958D2ee523a2206206994597C13D831ec7", web3, credentials,new DefaultGasProvider());
        BigInteger balanceOf = contract.balanceOf("0x4DE23f3f0Fb3318287378AdbdE030cf61714b2f3").sendAsync().get();
        System.out.println("getERC20Balance : " + balanceOf);
        System.out.println("getERC20Balance : " + toDecimal(8, balanceOf));
        System.out.println("余额3:" + balanceOf.doubleValue()/1000000);

        EthTransaction ethTransaction = web3.ethGetTransactionByHash("0x9d2582498fa4d12be9999db90907e730746bb2d11ea83403d3d14636fd7b854b").send();
        System.out.println(ethTransaction.getTransaction().get());

        Function function = new Function("balanceOf",
                Arrays.asList(new Address("0x4DE23f3f0Fb3318287378AdbdE030cf61714b2f3")),
                Arrays.asList(new TypeReference<Address>() {
                }));

        String encode = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction("0x4DE23f3f0Fb3318287378AdbdE030cf61714b2f3", "0xdAC17F958D2ee523a2206206994597C13D831ec7", encode);
        EthCall ethCall = web3.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        String value = ethCall.getResult();
        System.out.println("getERC20Balance balance : " + value);

//        try {
//            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
//            WalletFile walletFile = Wallet.createLight("123456", ecKeyPair);
//            BigInteger publicKey = ecKeyPair.getPublicKey();
//            BigInteger privateKey = ecKeyPair.getPrivateKey();
//            System.out.println(walletFile.getAddress());
//            System.out.println("publicKey="+Numeric.toHexStringNoPrefix(publicKey));
//            System.out.println("privateKey="+ Numeric.toHexStringNoPrefix(privateKey));
//        } catch (Exception e) {
//            throw new Exception(e);
//        }

        Credentials credentials2 =Credentials.create(Keys.createEcKeyPair());
        System.out.println("address="+credentials2.getAddress());
        System.out.println("publicKey="+credentials2.getEcKeyPair().getPublicKey().toString(16));
        System.out.println("privateKey="+credentials2.getEcKeyPair().getPrivateKey().toString(16));
        System.out.println("publicKey="+Numeric.toHexStringNoPrefix(credentials2.getEcKeyPair().getPublicKey()));
        System.out.println("privateKey="+ Numeric.toHexStringNoPrefix(credentials2.getEcKeyPair().getPrivateKey()));


    }

    public void test1() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://kovan.infura.io/<your-token>"));
        String ownAddress = "0xD1c82c71cC567d63Fd53D5B91dcAC6156E5B96B3";
        String toAddress = "0x6e27727bbb9f0140024a62822f013385f4194999";
        Credentials credentials = Credentials.create("xxxxxxxx");

        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j, credentials, toAddress,
                BigDecimal.valueOf(0.2), Convert.Unit.ETHER).send();

        System.out.println(transactionReceipt.getTransactionHash());
    }
}
