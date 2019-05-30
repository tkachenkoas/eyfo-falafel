package com.atstudio.eyfofalafel.database.createadmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Slf4j
public class AdminUserCreator {

    @Autowired
    private BCryptPasswordEncoder encoder;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private Environment environment;

    public AdminUserCreator() {
    }

    @Transactional
    public void createAdminUser() {
        String userName = environment.getProperty("admin.username");
        String password = environment.getProperty("admin.password");

        String encPassw = encoder.encode(password);

        String insertSql = "INSERT INTO t_user (id, user_name, password) " +
                            " VALUES ( " +
                            "   ( SELECT COALESCE (MAX(id), 1) FROM t_user ), :userName, :encPassw " +
                            " )  " +
                            " ON CONFLICT ON CONSTRAINT unq_login" +
                            " DO NOTHING ";

        int inserted = em.createNativeQuery(insertSql)
                              .setParameter("userName", userName)
                              .setParameter("encPassw", encPassw).executeUpdate();

        if (inserted == 0) {
            log.info("No users were inserted");
        } else {
            log.info("Created user {} with password {}", userName, password);
        }

    }

}
