package com.pkk.peakrabbitmq.base;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 消息处理
 * @author: peikunkun
 * @create: 2019-04-19 18:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

  private static final long serialVersionUID = 6750713499236484656L;

  private String msg;
}
