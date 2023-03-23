package com.han.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Page implements Serializable {
    private Integer page;   //当前页数
    private Integer limit;  //每页显示数
    private Integer totalPage;  //总页数
    private Integer total;  //总记录数
    private List pageRecode;    //当前页面的数据集合
    private List pages;     //返回页数的集合，用于显示index页面的第n页
    private String query;
    private int num;
    private int size;

}
