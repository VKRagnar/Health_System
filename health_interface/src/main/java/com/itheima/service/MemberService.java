package com.itheima.service;

import com.itheima.pojo.Member;

/**
 * 登录
 */
public interface MemberService {

    /**
     * 根据手机号查找会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 增加会员的方法
     * @param member
     */
    void add(Member member);
}
