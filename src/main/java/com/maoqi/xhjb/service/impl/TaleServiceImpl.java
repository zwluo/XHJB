package com.maoqi.xhjb.service.impl;

import com.maoqi.xhjb.dao.impl.TaleDaoImpl;
import com.maoqi.xhjb.pojo.dbbean.Tale;
import com.maoqi.xhjb.pojo.vo.TaleVO;
import com.maoqi.xhjb.service.TaleService;
import com.maoqi.xhjb.util.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaleServiceImpl implements TaleService {

    private final TaleDaoImpl taleDao;

    public TaleServiceImpl(TaleDaoImpl taleDao) {
        this.taleDao = taleDao;
    }

    @Cacheable(value = "xhjb:cache:getTaleById", key = "'id:' + #p0")
    @Override
    public TaleVO getTaleById(Integer id) {
        TaleVO taleVO = null;
        Tale tale = taleDao.getTaleById(id);
        if(tale != null) {
            taleVO = new TaleVO(tale);
        }

        return taleVO;
    }

    @Cacheable(value = "xhjb:cache:getTalesByType", key = "'type:' + #p0")
    @Override
    public List<TaleVO> getTalesByType(String type) {
        return taleDao.getTalesByType(type);
    }

    @Override
    public void saveTales() {
        List<Tale> list = null;
        try {
            list = getTalesFromTxt();
            list.addAll(getBalladFromTxt());
            list.addAll(getProverbFromTxt());
            taleDao.saveTales(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Tale> getTalesFromTxt() throws IOException {
        List<Tale> list = new ArrayList<>();

        File file = new File("d:" + File.separator + "tale.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        // 故事类型
        String type = "";

        // 上一行是否是故事类型
        boolean lastIsType = false;

        // 上一行是否是空白行
        boolean lastIsBlank = false;

        Tale tale = new Tale();

        String line = null;
        while ((line = reader.readLine()) != null) {
            // 故事类别
            if("神话".equals(line) || "传说".equals(line) || "故事".equals(line)) {
                type = line;
                lastIsType = true;
            } else if(StringUtils.isBlank(line)) {
                lastIsBlank = true;
            } else {
                // 故事标题
                if(lastIsType || lastIsBlank) {
                    tale.setTitle(line.replaceAll("。", "·"));
                    lastIsType = false;
                    lastIsBlank = false;
                } else if(line.contains("口述人") || line.contains("搜集人") || line.contains("整理人") || line.contains("采集人")) {   // 记录人
                    if (line.length() > 9 && line.indexOf("：") != line.lastIndexOf("：")) {
                        // 去除记录人空格
                        line = line.trim();
                        int lastIndex = line.lastIndexOf("：");
                        // 将多个空格替换为一个
                        tale.setRecorder(line.substring(lastIndex + 1).replaceAll("\\s+", " "));

                        line = line.substring(0, lastIndex - 3).trim();

                        // 将多个空格替换为一个
                        tale.setNarrator(line.substring(line.indexOf("：") + 1).replaceAll("\\s+", " "));

                        // 设置类型
                        tale.setType(type);
                        tale.setCreatedate(new Date());
                        tale.setCreateby("system");

                        list.add(tale);
                        tale = new Tale();
                    } else {
                        System.out.println("问题记录人：" + line);
                    }
                } else {        // 故事内容
                    if(StringUtils.isBlank(tale.getContent())) {
                        // 替换掉内容里面的空格
                        tale.setContent(line.replaceAll(" ", ""));
                    } else {
                        tale.setContent(tale.getContent() + "\n" + line);
                    }
                }
            }
        }

        return list;
    }

    public List<Tale> getBalladFromTxt() throws IOException {
        File file = new File("d:" + File.separator + "ballad.txt");
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        inputStream.available();

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = null;

        // 故事类型
        String type = "";

        // 上一行是否是故事类型
        boolean lastIsType = false;

        // 上一行是否是空白行
        boolean lastIsBlank = false;

        List<Tale> list = new ArrayList<>();

        Tale tale = new Tale();

        while ((line = reader.readLine()) != null) {
            // 故事类别
            if("神话".equals(line) || "传说".equals(line) || "故事".equals(line)) {
                type = line;
                lastIsType = true;
            } else if(StringUtils.isBlank(line)) {
                lastIsBlank = true;
            } else {
                // 故事标题
                if((lastIsType || lastIsBlank) && StringUtils.isBlank(tale.getTitle())) {
                    tale.setTitle(line.replaceAll("。", "·"));
                    tale.setContent("");
                    lastIsType = false;
                    lastIsBlank = false;
                } else if(line.contains("口述人") || line.contains("搜集人") || line.contains("整理人") || line.contains("采集人") || line.contains("演唱者")) {   // 记录人
                    if (line.length() > 9 && line.indexOf("：") != line.lastIndexOf("：")) {
                        // 去除记录人空格
                        line = line.trim();
                        int lastIndex = line.lastIndexOf("：");
                        // 将多个空格替换为一个
                        tale.setRecorder(line.substring(lastIndex + 1).replaceAll("\\s+", " "));

                        line = line.substring(0, lastIndex - 3).trim();

                        // 将多个空格替换为一个
                        tale.setNarrator(line.substring(line.indexOf("：") + 1).replaceAll("\\s+", " "));

                        // 设置类型
                        tale.setType("歌谣");
                        tale.setCreatedate(new Date());
                        tale.setCreateby("system");

                        list.add(tale);
                        tale = new Tale();
                    } else {
                        System.out.println("问题记录人：" + line);
                    }
                } else {        // 故事内容
                    if(StringUtils.isBlank(tale.getContent())) {
                        // 替换掉内容里面的空格
                        tale.setContent(line.replaceAll(" ", ""));
                    } else {
                        tale.setContent(tale.getContent() + "\n" + line);
                    }
                }
            }
        }
        return list;
    }

    public List<Tale> getProverbFromTxt() throws IOException {
        File file = new File("d:" + File.separator + "proverb.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = null;

        // 故事类型
        String type = "谚语";

        List<Tale> list = new ArrayList<>();
        Tale tale = null;
        Date date = new Date();

        while ((line = reader.readLine()) != null) {
            // 故事类别
            if(line.contains("（") && line.contains("）") && line.contains("类")) {
                if (tale != null) {
                    list.add(tale);
                }
                tale = new Tale();
                tale.setType(type);
                tale.setCreateby("system");
                tale.setCreatedate(date);
                tale.setTitle(line);
            } else {
                if (tale == null) {
                    continue;
                }

                if(StringUtils.isBlank(tale.getContent())) {
                    // 替换掉内容里面的空格
                    tale.setContent(line.replaceAll(" ", ""));
                } else {
                    tale.setContent(tale.getContent() + "\n" + line);
                }
            }
        }
        if (tale != null) {
            list.add(tale);
        }

        return list;
    }
}
