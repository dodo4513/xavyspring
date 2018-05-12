package study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import study.model.entity.Member;

@Mapper
@Repository
public interface MemberMapper {

    Member selectByNo(long no);
}
