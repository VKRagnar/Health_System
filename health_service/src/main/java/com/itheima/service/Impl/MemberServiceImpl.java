package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.MemberService;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录实现层
 */
@Service(interfaceClass=MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据手机号查找会员
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 增加会员的方法
     * @param member
     */
    public void add(Member member) {
        memberDao.add(member);
    }
}
