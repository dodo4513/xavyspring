package study.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@Alias("Member")
public class Member {
    private long no;
    private String name;
}
