package com.maoqi.xhjb.service;

import com.maoqi.xhjb.pojo.dbbean.Feedback;
import com.maoqi.xhjb.pojo.vo.TaleVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaleService {

    /**
     * 查询故事
     * @param id 故事id
     * @return 故事信息
     */
    TaleVO getTaleById(Integer id);

    /**
     * 查询故事目录
     * @param type 故事类型
     * @return 故事目录
     */
    List<TaleVO> getTalesByType(String type);

    /**
     * 导入故事
     */
    void saveTales();

    /**
     * 获取同时在线人数
     * @return 在线人数
     */
    int getOnlineCounter();

    /**
     * 保存反馈的内容
     * @param feedback 反馈内容
     */
    void saveFeedback(Feedback feedback);
}
