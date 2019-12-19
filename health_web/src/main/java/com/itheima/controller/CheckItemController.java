package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.QueryPageBean;
import com.itheima.pojo.Result;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 体验检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 增加体检项
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            if(checkItem.getSex()==null){checkItem.setSex("0");}
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有体检项
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findAll(){
        try {
            List<CheckItem> list=checkItemService.findAll();
            return new Result(true,"",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * 查询当前页体检项
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
    }

    /**
     * 删除体检项
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result delete(int id){
        try {
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FALL+":"+e.getMessage());
        }

    }

    /**
     * 根据ID查找体检项
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findById(int id){
        try {
            CheckItem checkItem=checkItemService.findById(id);
            return new Result(true,"",checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SYSTEM_ERROR);
        }
    }

    /**
     * 编辑体检项
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }
}
