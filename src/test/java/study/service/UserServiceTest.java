package study.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import study.dao.UserDao;
import study.dao.repository.UserRepository;
import study.model.User;
import study.util.CustomTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDao userDao;

    @Autowired
    CustomTransactionManager transactionManager;

    @PersistenceContext
    EntityManager em;

    EntityTransaction transaction;

    private User defaultUser;
    private final String defaultName = "도영";
    private final String alreadyCreatedUserName = "도도";
    private final String newPassword = "newPassword";

    // note - user의 equals
    @BeforeEach
    void before() {
        defaultUser = new User();
        defaultUser.setName(defaultName);
        defaultUser.setPassword("123");

        em.setFlushMode(FlushModeType.COMMIT);
//        transaction = em.getTransaction();
    }

    @Test
    void userDaoTest() throws SQLException, ClassNotFoundException {
        // NOTE 1 : JDBD의 add, get(id), get(name) 잘 동작함.
        long id = userDao.add(defaultUser);
        Assertions.assertEquals(userDao.get(id), defaultUser); // 동등함
        Assertions.assertEquals(userDao.get(defaultName), defaultUser);

        Assertions.assertTrue(userDao.get(id) != defaultUser); // 동일하지는 않음
    }

    @Test
    void userRepositoryTest() {
        // NOTE 2 : JPA도 테스트
        User user = userRepository.save(defaultUser);
        Assertions.assertEquals(userRepository.selectByName(defaultName), defaultUser);
        Assertions.assertEquals(userRepository.findById(user.getId()).orElse(null), defaultUser); // CrudRepository - findById
    }

    @Test
    void identityTest() {
        // NOTE 3 : 동일성 테스트
        User user = userRepository.save(defaultUser);
        Assertions.assertTrue(userRepository.selectByName(defaultName) == user && user == defaultUser); // 동일함

        // 영속성 컨텍스트의 라이프사이클?? userRepository.getOne(1L);
    } // t

    @Test
    void persistTest() {
        // NOTE 4 : INSERT 쓰기 지연 테스트 (95P)
        transaction.begin();
        userRepository.save(defaultUser);
        transaction.commit();
    }

    @Test
    @Transactional
    void persist2Test() {
        // NOTE 5 : INSERT 쓰기 지연 테스트2
        userRepository.save(defaultUser);
        log.info("Hmm");
    }

    @Test
    void persist3Test() {
        // NOTE 6 : INSERT 쓰기 지연 테스트3
        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        userRepository.save(defaultUser);
        transactionManager.commit();
    }

    @Test
    @Transactional
    void persist4Test() {
        // NOTE 7 : INSERT 쓰기 지연 테스트4
        em.persist(defaultUser);
        log.info("Hmm");
    }

    @Test
    void persist5Test() {
        // NOTE 8 : UPDATE 쓰기 지연 테스트 1
        String newPassword = "newPassword";
        User user = userRepository.selectByName(alreadyCreatedUserName);
        user.setPassword(newPassword); // 쓰기 지연??

        Assertions.assertEquals(userRepository.findById(user.getId()).orElse(null), user);
    } // t

    @Test
    void persist6Test() throws SQLException, ClassNotFoundException {
        // NOTE 9 : UPDATE 쓰기 지연 테스트 2
        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRED);
        String newPassword = "newPassword";
        User user = userRepository.selectByName(alreadyCreatedUserName);
        user.setPassword(newPassword);
        transactionManager.commit(); // !!

        Assertions.assertEquals(userRepository.findById(user.getId()).orElse(null), user);
        Assertions.assertEquals(userDao.get(alreadyCreatedUserName), user); // 당연
    }

    @Test
    void persist7Test() throws SQLException, ClassNotFoundException {
        // NOTE 9 : UPDATE 쓰기 지연 테스트 3
        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRED);
        String newPassword = "newPassword";
        User user = userRepository.selectByName(alreadyCreatedUserName);
        user.setPassword(newPassword);

        Assertions.assertNotEquals(userDao.get(alreadyCreatedUserName), user); // 당연
        transactionManager.commit();
    }

    @Test
    void persist8Test() throws SQLException, ClassNotFoundException {
        // NOTE 10 : UPDATE 쓰기 지연 테스트 4 (flush 사용)
        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRED);
        User user = userRepository.selectByName(alreadyCreatedUserName);
        user.setPassword(newPassword);
        userRepository.flush();

        Assertions.assertEquals(userDao.get(alreadyCreatedUserName), user); // 왜?
        transactionManager.commit();
    }

    // 앗 그러면 반대 순서로 하면?
    @Test
    void Jpa1CacheTest() throws SQLException, ClassNotFoundException {
        // NOTE 11 : JPA 1차 캐시 테스트
        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRED);
        userDao.add(defaultUser);
        User user = userRepository.selectByName(defaultName);
        Assertions.assertEquals(defaultUser, user);
        transactionManager.commit();
    }

    @Test
    @Transactional
    void transactionTest() {
        // NOTE 12 : transaction propagation 테스트 1 (insert)
        User user = userRepository.save(defaultUser);

        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        {
            User innerUser = userRepository.selectByName(defaultName);
            Assertions.assertNull(innerUser); // ㅋㅋ
        }
        transactionManager.commit();
    }

    @Test
    @Transactional
    void transaction2Test() {
        // NOTE 12 : transaction propagation 테스트 2
        User user = userRepository.save(defaultUser);
        userRepository.flush(); // flush 하면?

        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        {
            User innerUser = userRepository.selectByName(defaultName);
            Assertions.assertNull(innerUser); // ?
        }
        transactionManager.commit();
    }

    @Test
    @Transactional
    void transaction3Test() {
        // NOTE 12 : transaction propagation 테스트 3 (update)
        User user = userRepository.selectByName(alreadyCreatedUserName);

        Assertions.assertEquals(user, userRepository.selectByName(alreadyCreatedUserName));
        user.setPassword(newPassword);
        userRepository.flush(); // flush 하면?

        transactionManager.start(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        {
            User innerUser = userRepository.selectByName(alreadyCreatedUserName);
            Assertions.assertNotEquals(user, innerUser); // ?
        }
        transactionManager.commit();
    }
}