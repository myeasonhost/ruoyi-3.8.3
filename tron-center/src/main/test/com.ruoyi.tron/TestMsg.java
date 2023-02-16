package com.ruoyi.tron;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class TestMsg {
    public static void main(String[] args) {
//        public MessageDto sendMessage(Message message) {
//
//            return restTemeplate.exchange(
//                    "https://api.telegram.org/bot{token}/sendMessage",
//                    HttpMethod.POST,
//                    new HttpEntity<>(message, HttpHeaders.EMPTY),
//                    MessageDto.class
//            ).getBody();
//        }
        Map<String, String> appParamMap = new TreeMap<>();
        appParamMap.put("organizationId", "1");
        appParamMap.put("notifyUrl", "2");
        appParamMap.put("userId", "3");
        appParamMap.put("alias", "4"); //分为单元
        appParamMap.put("coinType", "5");
        StringBuffer orgin = new StringBuffer();
        Iterator iter = appParamMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append("&").append(name).append("=").append((String) appParamMap.get(name));
        }
        orgin.append("&").append("key").append("=").append("apiKey");
        orgin.deleteCharAt(0);
        System.out.println("【充值支付】sign加密串=" + orgin);
    }
}
