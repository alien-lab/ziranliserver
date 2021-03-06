package com.alienlab.ziranli.web.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.ziranli.domain.ArtworkOrder;
import com.alienlab.ziranli.domain.CourseOrder;

/**
 * Created by 橘 on 2017/5/14.
 */
public interface WechatMessageService {
    JSONObject buyArtworkMessage(ArtworkOrder order);
    JSONObject buyCourseMessage(CourseOrder order);
}
