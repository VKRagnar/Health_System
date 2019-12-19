package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.QueryPageBean;
import com.itheima.pojo.Result;
import com.itheima.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 体检组
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 增加体检组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_ADD')")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询当前页数据
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value="/findPage",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
                );
    }

    /**
     * 删除体检组
     * @param id
     * @return
     */
    @RequestMapping(value="/delete",method=RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result delete(int id){
        try {
            checkGroupService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据ID查询检查组
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findById(Integer groupId){
        try {
            CheckGroup checkGroup=checkGroupService.findById(groupId);
            return new Result(true,"",checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 修改检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组ID查询关联的检查项
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/findCheckitemIds",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public List<Integer> findCheckitemIds(Integer groupId){
        return checkGroupService.findCheckitemIds(groupId);
    }

    /**
     * 查询所有检查组的方法
     * @return
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findAll(){
        try {
            List<CheckGroup> list=checkGroupService.findAll();
            return new Result(true,"",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
