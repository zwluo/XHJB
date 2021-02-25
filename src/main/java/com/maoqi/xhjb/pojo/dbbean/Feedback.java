package com.maoqi.xhjb.pojo.dbbean;

import com.maoqi.xhjb.pojo.vo.FeedbackVO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String type;
  private String title;
  private String content;
  private String ip;

  public Feedback() {
  }

  public Feedback(FeedbackVO feedbackVO) {
    this.type = feedbackVO.getType();
    this.title = feedbackVO.getTitle();
    this.content = feedbackVO.getContent();
    this.ip = feedbackVO.getIp();
  }

}
