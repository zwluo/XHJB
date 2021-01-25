package com.maoqi.xhjb.util;

import com.maoqi.xhjb.pojo.dbbean.Tale;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 导入谚语
 */
public class ImportProverb {
    public static void main(String[] args) throws IOException {
        File file = new File("d:" + File.separator + "proverb.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = null;

        // 故事类型
        String type = "谚语";

        List<Tale> list = new ArrayList<>();
        Tale tale = null;
        Date date = new Date();

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
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

        System.out.println(list.size());
        System.out.println(list);

    }
}
