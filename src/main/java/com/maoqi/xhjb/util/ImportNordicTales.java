package com.maoqi.xhjb.util;

import com.maoqi.xhjb.pojo.dbbean.Tale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportNordicTales {

    public static void main(String[] args) {
        File file = new File("d:" + File.separator + "nordicTales.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = null;

            // 故事类型
            String type = "";

            List<Tale> list = new ArrayList<>();
            Tale tale = null;
            Date date = new Date();

            boolean isTitle = false;

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll(" ", "");

                // 如果当前行是空行，就认为前一个故事已经结束了，并且下一行是新故事的标题
                if(StringUtils.isBlank(line)) {
                    if (tale != null) {
                        list.add(tale);
                        tale = null;
                    }
                    isTitle = true;
                    continue;
                }

                // 故事类别
                if ("挪威".equals(line) || "丹麦".equals(line) || "瑞典".equals(line)) {
                    type = line;
                    continue;
                } else {
                    // 如果isTitle为true，表示当前行是标题
                    if(isTitle) {
                        tale = new Tale();
                        tale.setType(type);
                        tale.setCreateby("system");
                        tale.setCreatedate(date);
                        tale.setTitle(line);

                        isTitle = false;
                        continue;
                    }

                    // 将当前行作为故事内容添加到实体类里面
                    if (StringUtils.isBlank(tale.getContent())) {
                        // 替换掉内容里面的空格
                        tale.setContent(line);
                    } else {
                        tale.setContent(tale.getContent() + "\n" + line);
                    }
                }
            }

            // 如果txt文档最后一行不是空行，会漏掉最后一个故事
            if (tale != null) {
                list.add(tale);
                tale = null;
            }

            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
