

import groovy.sql.Sql
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.security.SecureRandom

import static java.lang.Integer.parseInt

class AdminCreate {

    private static Properties props;

    static void main(String[] args) {
        readProps()

        def encoder = new BCryptPasswordEncoder(
                parseInt(props."security.password.strength" as String),
                new SecureRandom(((String)props."security.password.salt").getBytes())
        )
        def encPassw = encoder.encode(props."spring.datasource.password" as String)

        def sql = Sql.newInstance(
                props."spring.datasource.url",
                props."spring.datasource.username",
                props."spring.datasource.password",
                props."spring.datasource.driverClassName"
        )

        sql.execute(
                "INSERT INTO t_user (id, user_name, password) " +
                        " VALUES ( " +
                        "   ( SELECT COALESCE (MAX(id) + 1, 1) FROM t_user ), :userName, :encPassw " +
                        " )  " +
                        " ON CONFLICT ON CONSTRAINT unq_login" +
                        " DO NOTHING ",
                userName : props."spring.datasource.username",
                encPasswL: encPassw
        )

        sql.close()
    }

    private static void readProps() {
        props = new Properties()
        File propsFile = new File("application.properties")
        propsFile.withInputStream {
            props.load it
        }
    }
 }