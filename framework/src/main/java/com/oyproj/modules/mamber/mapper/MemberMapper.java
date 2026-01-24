package com.oyproj.modules.mamber.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.oyproj.modules.mamber.entity.dos.Member;
import com.oyproj.modules.mamber.entity.vo.MemberVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员数据处理层
 *
 * @author oywq3000
 * @since 2026-01-24
 */
public interface MemberMapper extends BaseMapper<Member> {
    @Select("select m.mobile from member m")
    List<String> getAllMemberMobile();

    @Select("select * from member ${ew.customSqlSegment}")
    IPage<MemberVO> pageByMemberVO(IPage<MemberVO> page, @Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);
}
