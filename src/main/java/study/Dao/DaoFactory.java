package study.Dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        return new UserDao(getConnectionMaker());
    }

    @Bean
    public SimpleConnectionMaker getConnectionMaker() {
        return new SimpleConnectionMaker();
    }
}
