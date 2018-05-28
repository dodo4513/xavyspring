package study.dao.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import study.model.QUser;
import study.model.User;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    QUser user = QUser.user;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User selectByName(String name) {
        return from(user).where(user.name.eq(name)).fetchOne();
    }
}
