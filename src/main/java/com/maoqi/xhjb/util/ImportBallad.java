package com.maoqi.xhjb.util;

import com.maoqi.xhjb.pojo.dbbean.Tale;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 歌谣导入
 */

public class ImportBallad {
    public static void main(String[] args) throws IOException {
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
            System.out.println(line);
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
                        System.out.println(tale);
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

        System.out.println(list.size());
        System.out.println(list);

    }
}
