package com.maoqi.xhjb.dao;

import com.maoqi.xhjb.pojo.dbbean.Tale;
import com.maoqi.xhjb.pojo.vo.TaleVO;

import java.util.List;

public interface TaleDao {

    /**
     * 查询故事
     * @param id 故事id
     * @return 故事信息
     */
    Tale getTaleById(Integer id);

    /**
     * 查询故事目录
     * @param type 故事类型
     * @return 故事目录
     */
    List<TaleVO> getTalesByType(String type);

    /**
     * 导入故事
     */
    void saveTales(List<Tale> list);
}
