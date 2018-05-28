package study.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Scope("prototype")
@Slf4j
public class CustomTransactionManager {

    private static ThreadLocal<TransactionStatus> status = new ThreadLocal<>();
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public CustomTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void start(int transactionPropagationBehavior) {
        log.info("TransactionManager - start / start");
        status.remove();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("ecsTransaction");
        def.setPropagationBehavior(transactionPropagationBehavior);
        int isolationLevel = TransactionDefinition.ISOLATION_DEFAULT;
        def.setIsolationLevel(isolationLevel);
        int timeout = DefaultTransactionDefinition.TIMEOUT_DEFAULT;
        def.setTimeout(timeout);
        status.set(transactionManager.getTransaction(def));
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
        log.info("TransactionManager - start / end");
    }


    public void rollback() {
        log.info("TransactionManager - rollback / start");
        transactionManager.rollback(status.get());
        status.remove();
        log.info("TransactionManager - rollback / end");
    }

    public void commit() {
        log.info("TransactionManager - commit / start");
        transactionManager.commit(status.get());
        status.remove();
        log.info("TransactionManager - commit / start");
    }
}
