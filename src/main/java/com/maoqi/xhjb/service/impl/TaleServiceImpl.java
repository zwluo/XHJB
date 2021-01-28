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
        List<Tale> list;
        try {
            list = getTalesFromTxt();
            list.addAll(getBalladFromTxt());
            list.addAll(getProverbFromTxt());
            list.addAll(getDescribe());
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

        String line;
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

                        // 替换掉内容里面的空格
                        if (tale.getContent() != null) {
                            tale.setContent(tale.getContent().replaceAll(" ", ""));
                        }
                        list.add(tale);
                        tale = new Tale();
                    } else {
                        System.out.println("问题记录人：" + line);
                    }
                } else {        // 故事内容
                    if(StringUtils.isBlank(tale.getContent())) {
                        tale.setContent("　　" + line);
                    } else {
                        tale.setContent(tale.getContent() + "\n" + "　　" + line);
                    }
                }
            }
        }

        return list;
    }

    /**
     * 歌谣
     * @return 歌谣内容
     * @throws IOException 异常
     */
    public List<Tale> getBalladFromTxt() throws IOException {
        File file = new File("d:" + File.separator + "ballad.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

        // 故事类型
        String type = "歌谣";

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
                        tale.setType(type);
                        tale.setCreatedate(new Date());
                        tale.setCreateby("system");

                        // 替换掉内容里面的空格
                        if (tale.getContent() != null) {
                            tale.setContent(tale.getContent().replaceAll(" ", ""));
                        }
                        list.add(tale);
                        tale = new Tale();
                    } else {
                        System.out.println("问题记录人：" + line);
                    }
                } else {        // 故事内容
                    if(StringUtils.isBlank(tale.getContent())) {
                        tale.setContent(line);
                    } else {
                        tale.setContent(tale.getContent() + "\n" + line);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 谚语
     * @return 谚语
     * @throws IOException 异常
     */
    public List<Tale> getProverbFromTxt() throws IOException {
        File file = new File("d:" + File.separator + "proverb.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

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
                    tale.setContent(line);
                } else {
                    tale.setContent(tale.getContent() + "\n" + line);
                }
            }
        }
        if (tale != null) {
            // 替换掉内容里面的空格
            if (tale.getContent() != null) {
                tale.setContent(tale.getContent().replaceAll(" ", ""));
            }
            list.add(tale);
        }

        return list;
    }

    /**
     * 描述
     * @return 描述
     */
    public List<Tale> getDescribe() {
        List<Tale> list = new ArrayList<>();
        Date date = new Date();
        Tale tale = new Tale();
        tale.setType("关于本书");
        tale.setTitle("编后");
        tale.setContent("　　为了进一步开创民间文学的新局面，保存人民的口头文学财富，在省、扬州市有关部门和我市市委、市人大、市政府、" +
                "市政协领导同志的关心支持下，经过三年的搜集整理工作，《兴化市卷本》现在和读者见面了。省“三民”办公室唐雨奇、" +
                "陈为史和我市刁俊、毕明、仲鲁连、陈康、潘少卿等同志，为《兴化市卷本》的编辑做了大量的工作，在此谨致谢忱。\n" +
                "　　编者\n" + "　　一九九三年十二月六日");
        tale.setCreatedate(date);
        tale.setCreateby("system");
        list.add(tale);

        tale = new Tale();
        tale.setType("关于本书");
        tale.setTitle("各乡镇入选作品");
        tale.setContent("单　位　民间故事　歌　谣　谚　语\n" +
                "戴南镇　　11　　　　16　　237\n" +
                "张郭乡　　2　　　　　6\n" +
                "唐刘乡　　11　　　　11\n" +
                "顾庄乡　　　　　　　1\n" +
                "周庄镇　　1　　　　　6\n" +
                "边城镇　　3　　　　　　　　11\n" +
                "茅山镇　　1　　　　　2　　　9\n" +
                "陈堡乡　　1\n" +
                "竹泓镇　　9　　　　　2\n" +
                "沈伦乡　　2　　　　　1\n" +
                "林湖乡　　3　　　　　2\n" +
                "垛田乡　　1　　　　　1\n" +
                "安丰镇　　9　　　　　4\n" +
                "新垛乡　　　　　　　　　　　4\n" +
                "中圩乡　　1　　　　　4　　　21\n" +
                "钓鱼乡　　3\n" +
                "下圩乡　　1\n" +
                "大邹镇　　3　　　　　　　　20\n" +
                "海河乡　　2\n" +
                "海南乡　　1\n" +
                "昌荣镇　　5\n" +
                "大垛镇　　　　　　　2\n" +
                "荻垛乡　　　　　　　3\n" +
                "陶庄乡　　2\n" +
                "沙沟镇　　3　　　　　2\n" +
                "舜生镇　　　　　　　2\n" +
                "周奋乡　　6　　　　　1\n" +
                "中堡镇　　1\n" +
                "戴窑镇　　2　　　　　　　　50\n" +
                "合塔乡　　1　　　　　1\n" +
                "舍陈镇\n" +
                "徐扬乡　　　　　　　1　　　48\n" +
                "西鲍乡　　　　　　　　　　　8\n" +
                "临城乡　　　　　　　1　　　10\n" +
                "东鲍乡\n" +
                "荡朱乡　　7　　　　　6\n" +
                "东潭乡　　1　　　　　2\n" +
                "昭阳镇　　2\n" +
                "市　直　　95　　　　　90　　322\n" +
                "其　他　　12　　　　　6　　　8\n" +
                "合　计　　202　　　　173　　748");
        tale.setCreatedate(date);
        tale.setCreateby("system");
        list.add(tale);

        tale = new Tale();
        tale.setType("关于本书");
        tale.setTitle("网页版说明");
        tale.setContent("　　这本书出版日期是1993年10月，当时兴化还属于扬州，由兴化市民间文学集成办公室历时3年采编完成。" +
                "很感谢前辈们的先见之明和辛勤付出，才让我们有机会读到这些精彩的民间故事和人生哲理，里面有很多兴化方言，读来倍感亲切。" +
                "童话故事不是安徒生的专利，我们的先辈们讲故事也是很有一手的。\n" +
                "　　里面的故事并不是很全，只希望能有人继续采集这些民间故事，因为口口相传的故事都会随着时间慢慢消逝，" +
                "先辈们留下的瑰宝不能在我们这代葬送，我能做的也就这些了。\n" +
                "　　二零一七年九月九日");
        tale.setCreatedate(date);
        tale.setCreateby("system");
        list.add(tale);

        return list;
    }
}
