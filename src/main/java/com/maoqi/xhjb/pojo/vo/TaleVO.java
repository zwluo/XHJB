package com.maoqi.xhjb.pojo.vo;

import com.maoqi.xhjb.pojo.dbbean.Tale;
import com.maoqi.xhjb.util.StringUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaleVO implements Serializable {

    private String id;
    private String type;
    private String title;
    private String[] paragraphs;
    private String narrator;
    private String recorder;

    public TaleVO() {}

    public TaleVO(Tale tale) {
        this.type = tale.getType();
        this.title = tale.getTitle();
        this.narrator = tale.getNarrator();
        this.recorder = tale.getRecorder();

        // 对内容按章节切分
        if (StringUtils.isNotBlank(tale.getContent())) {
            this.paragraphs = tale.getContent().replaceAll(" ", "").split("\\n");
            if ("神话".equals(this.type) || "故事".equals(this.type) || "传说".equals(this.type)) {
                for (int i=0; i<this.paragraphs.length; i++) {
                    this.paragraphs[i] = "　　" + this.paragraphs[i];
                }
            }
        }
    }

    public TaleVO(Object id, Object title) {
        this.id = String.valueOf(id);
        this.title = String.valueOf(title);
    }

}
