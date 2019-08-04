@GrabConfig( systemClassLoader=true )
import groovy.sql.Sql
import org.postgresql.Driver
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.security.SecureRandom
import java.sql.Connection
import java.sql.DriverManager

import static java.lang.Integer.parseInt

Properties props = new Properties()

def loadProps = { String file ->
    InputStream is = this.class.classLoader.getResourceAsStream(file)
    try {
        props.load(is)
    } finally {
        is.close()
    }
}

loadProps("application.properties")
loadProps("./seeders/admin-user.properties")

def encoder = new BCryptPasswordEncoder(
        parseInt(props."security.password.strength" as String),
        new SecureRandom(((String)props."security.password.salt").getBytes())
)
def encPassw = encoder.encode(props."admin.password" as String)

DriverManager.registerDriver(new Driver())
Connection conn = DriverManager.getConnection(
        props."spring.datasource.url" as String,
        props."spring.datasource.username" as String,
        props."spring.datasource.password" as String,
)

def sql = Sql.newInstance(conn)

sql.execute(
        "INSERT INTO t_user (id, user_name, password) " +
                " VALUES ( " +
                "   ( SELECT COALESCE (MAX(id) + 1, 1) FROM t_user ), :userName, :encPassw " +
                " )  " +
                " ON CONFLICT ON CONSTRAINT unq_login" +
                " DO NOTHING ",
        userName : props."admin.username",
        encPassw : encPassw
)

sql.close()