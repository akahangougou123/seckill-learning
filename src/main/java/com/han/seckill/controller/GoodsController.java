package com.han.seckill.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.han.seckill.pojo.Goods;
import com.han.seckill.pojo.Page;
import com.han.seckill.pojo.User;
import com.han.seckill.service.IGoodsService;
import com.han.seckill.service.ISeckillGoodsService;
import com.han.seckill.service.IUserService;
import com.han.seckill.vo.DetailVo;
import com.han.seckill.vo.GoodsVo;
import com.han.seckill.vo.RespBean;
import com.han.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiajian_han
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user){
//        System.out.println(ticket);
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        User user = (User) session.getAttribute(ticket);
//        User user =  userService.getUserByCookie(ticket, request, response);
//        System.out.println("user:"+user);
//        if(null == user){
//            return "login";
//        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodVo());
        return "goodsList";
    }

    /**
     * 跳转商品详情页
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
    @ResponseBody
    public RespBean toDetail(String goodsId){
//        model.addAttribute("user",user);
        System.out.println(goodsId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态 0:倒计时 1:进行中 2:已结束
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if(nowDate.before(startDate)){
            seckillStatus =  0;
            remainSeconds = ( (int) ((startDate.getTime()-nowDate.getTime())/1000) );
        } else if (nowDate.after(endDate)) {
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 1;
        }
        log.info("{}",seckillStatus);
//        model.addAttribute("seckillStatus",seckillStatus);
//        model.addAttribute("remainSeconds",remainSeconds);
//        model.addAttribute("goods",goodsVo );
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }


    /**
     * 分页查询
     * @return
     */
    @GetMapping("/query/list")
    public RespBean queryArticle(int pages,int pagesize,int type){
        if(pages<0 || pagesize<0){
            pages= Math.abs(pages);
            pagesize=Math.abs(pagesize);
        }
        System.out.println(goodsService.queryAllGoods((pages-1)*pagesize, pagesize,type));
       // return articleMapper.queryList((pages-1)*pagesize, pagesize,type);
       // return goodsService.queryAllGoods((pages-1)*pagesize, pagesize,type);
        List<GoodsVo> goodsList = goodsService.queryAllGoods((pages-1)*pagesize, pagesize,type);
        int goodsTotal = seckillGoodsService.count();
        if(goodsList.size() == 0){
            return RespBean.error(RespBeanEnum.ERROR);
        }
//        int total = goodsList.toArray().length;
//         int size = goodsList.size();
        Map<String,Object> goodsMap = new HashMap<>();
        goodsMap.put("goodsList",goodsList);
        goodsMap.put("total",goodsTotal);
        return RespBean.success(goodsMap);
    }

    @RequestMapping(value = "/queryGood",method = RequestMethod.POST)
    @ResponseBody
    public RespBean queryGood(@RequestBody Page page){
        if (page.getQuery() == null){
            page.setQuery("%");
        }else{
            page.setQuery("%"+page.getQuery()+"%");
        }
        page.setNum((page.getNum()-1)*page.getSize());
        Map<String, Object> goodList = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<GoodsVo> goods = goodsService.queryGoods(page);
        if(goods.size() == 0){
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }
        dataMap.put("goods",goods);
        goodList.put("data",dataMap);
        return RespBean.success(goodList);
    }

    /**
     * 获取商品详情图
     */
    @RequestMapping(value = "/detail/getDetailImg",method = RequestMethod.GET)
    public RespBean getDetailImg(String goodsId){
        System.out.println(goodsId);

        List<Goods> imgList = goodsService.getDetailImg(goodsId);
        return RespBean.success(imgList);
    }
}
