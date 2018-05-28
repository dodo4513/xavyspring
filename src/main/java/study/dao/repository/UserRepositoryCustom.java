package study.dao.repository;

import study.model.User;

public interface UserRepositoryCustom {

    User selectByName(String name);
}
