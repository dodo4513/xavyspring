package study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import study.model.entity.Member;

@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM member WhERE no = #{no}")
    Member selectMember(long no);

    // 쿼리를 분리한 메소드
    Member selectMember2(long no);
}
