package com.maoqi.xhjb.pojo.dbbean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class VisitLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String method;
  private String request;
  private String response;
  private String ip;
  private Date createdate;
  private String createby;

}