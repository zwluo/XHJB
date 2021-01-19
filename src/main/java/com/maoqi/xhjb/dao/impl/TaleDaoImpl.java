package com.maoqi.xhjb.dao.impl;

import com.maoqi.xhjb.dao.TaleDao;
import com.maoqi.xhjb.pojo.dbbean.Tale;
import com.maoqi.xhjb.pojo.vo.TaleVO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TaleDaoImpl implements TaleDao {

    private final EntityManager template;

    public TaleDaoImpl(EntityManager template) {
        this.template = template;
    }

    @Override
    public Tale getTaleById(Integer id) {
        return template.find(Tale.class, id);
    }

    @Override
    public List<TaleVO> getTalesByType(String type) {
        List<TaleVO> voList = new ArrayList<>();

        String sql = "select id,title from tale where type =?1 order by id ";
        Query query= template.createNativeQuery(sql );
        query.setParameter(1,type);

        List list = query.getResultList();
        if(list != null && list.size() > 0) {
            for(int i=0; i<list.size(); i++) {
                Object[] arr = (Object[])list.get(i);
                TaleVO taleVO = new TaleVO(arr[0], arr[1]);
                voList.add(taleVO);
            }
        }
        return voList;
    }

    @Override
    public void saveTales(List<Tale> list) {
        int j = 0;

        for (Tale item : list) {
            template.persist(item);

            j++;
            if (j % 50 == 0 || j == list.size()) {
                try {
                    template.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    template.clear();
                }
            }
        }
    }


}
