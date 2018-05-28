package study;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import study.dao.DaoFactory;
import study.dao.UserDao;
import study.model.User;

import java.sql.SQLException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SpringApplication.run(Application.class, args);

//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
//        UserDao dao = applicationContext.getBean("userDao", UserDao.class);
//        User user = new User();
//        user.setId("whiteship");
//        user.setName("백기선");
//        user.setPassword("married");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + "등록 성공");
//
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getId() + " 조회 성공");
    }
}
