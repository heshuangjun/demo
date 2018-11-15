package cn.pyg.sms.controller;

import cn.pyg.sms.util.SmsUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heshuangjun
 * @date 2018-11-13 08:39
 */
@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    private SmsUtil smsUtil;

    /**
     * 13:39分
     * @param phoneNumbers
     * @param signName
     * @param templateCode
     * @param param
     * @return
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public Map sendMessage(String phoneNumbers, String signName, String templateCode, String param) {
        try {

            //调用阿里大于封装好的通信工具类
            SendSmsResponse response = smsUtil.sendSms(phoneNumbers, signName, templateCode, param);

            Map map = new HashMap();
            map.put("Code", response.getCode());
            map.put("Message", response.getMessage());
            map.put("RequestId", response.getRequestId());
            map.put("BizId", response.getBizId());

            return map;


        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
