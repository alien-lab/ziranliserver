package com.alienlab.ziranli.web.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.domain.CourseOrder;
import com.alienlab.ziranli.web.wechat.service.WechatMessageService;
import com.alienlab.ziranli.web.wechat.util.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Created by 橘 on 2017/5/14.
 */
@Service
public class WechatMessageServiceImpl implements WechatMessageService {
    @Autowired
    WechatUtil wechatUtil;
    @Value("${wechat.host.basepath}")
    private String wechathost;


    @Override
    public JSONObject buyArtworkMessage(ArtworkOrder order) {
        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        first.put("value",order.getUser().getNickName()+"，艺术品购买成功。");
        first.put("color","#000000");
        param.put("first",first);

        JSONObject param1=new JSONObject();
        param1.put("value",order.getArtwork().getName()+"|"+order.getArtwork().getAuthor()+"|"+order.getArtwork().getYear());
        param1.put("color","#173177");
        param.put("keyword1",param1);



        JSONObject param2=new JSONObject();
        param2.put("value",order.getPayPrice());
        param2.put("color","#000000");
        param.put("keyword2",param2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JSONObject param3=new JSONObject();
        param3.put("value",order.getPayTime().format(formatter));
        param3.put("color","#173177");
        param.put("keyword3",param3);

        JSONObject remark=new JSONObject();
        remark.put("value","感谢您的购买，我们很快将与您联系发货事宜，请保持您的手机["+order.getPhone()+"]畅通。");
        remark.put("color","#000000");
        param.put("remark",remark);

        String url= wechatUtil.getPageAuthUrl(wechathost+"#!/artworkDesc",String.valueOf(order.getArtwork().getId()));

        JSONObject result= wechatUtil.sendTemplateMsg(order.getUser().getOpenId(),url,"yHvVOMki-AgzIN1NbuLfRoR3ReqlGv4-9I0whvZgLDA",param);
        return result;
    }

    public JSONObject buyCourseMessage(CourseOrder order){
        JSONObject param=new JSONObject();
        JSONObject first=new JSONObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        first.put("value",order.getUser().getNickName()+"，您已成功报名课程。");
        first.put("color","#000000");
        param.put("first",first);

        JSONObject param1=new JSONObject();
        param1.put("value",order.getCourse().getName());
        param1.put("color","#173177");
        param.put("keyword1",param1);


        JSONObject param2=new JSONObject();
        param2.put("value",order.getCourse().getCourseTime().format(formatter));
        param2.put("color","#000000");
        param.put("keyword2",param2);


        JSONObject remark=new JSONObject();
        remark.put("value","感谢您的报名，点击进入课程详情，获取课程内容。");
        remark.put("color","#000000");
        param.put("remark",remark);


        String url= wechatUtil.getPageAuthUrl(wechathost+"#!/courseDesc",String.valueOf(order.getCourse().getId()));

        JSONObject result= wechatUtil.sendTemplateMsg(order.getUser().getOpenId(),url,"jlNmH7fcKmnWJU1Z1mCYL2l2DEuvAnV3SuOMkA2dDh8",param);
        return result;
    }
}
