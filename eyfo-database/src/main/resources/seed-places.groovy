@GrabConfig( systemClassLoader=true )
import groovy.sql.Sql
import groovy.json.JsonSlurper
import org.postgresql.Driver

import java.sql.Connection
import java.sql.DriverManager

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

DriverManager.registerDriver(new Driver())
Connection conn = DriverManager.getConnection(
        props."spring.datasource.url" as String,
        props."spring.datasource.username" as String,
        props."spring.datasource.password" as String,
)
def sql = Sql.newInstance(conn)

def data = new JsonSlurper().parse(this.class.classLoader.getResourceAsStream('./seeders/moscow-places.json'))

class Place {
    String name
    String description
    String address
    BigDecimal lat
    BigDecimal lng
    int priceFrom
    int priceTo
}

def places = [] as List<Place>

for (def jsonPlace: data['results']) {
    places << (new Place(
            name: (jsonPlace['name'] as String).replaceAll("'", "-"),
            description: String.join(',', jsonPlace['types'] as List),
            address: jsonPlace['vicinity'],
            lat: (jsonPlace.geometry.location.lat),
            lng: (jsonPlace.geometry.location.lng),
            priceFrom: (int)(jsonPlace['price_level']?:2) * 60,
            priceTo: (int)(jsonPlace['price_level']?:3) * 80
    ))
}

def randPlaces = [] as List<String>
5.times {
    def randPlace = places[ (int) (Math.random() * places.size() ) ]
    randPlaces << "'${randPlace.name + randPlace.address}'"
}

def existCount = sql.firstRow(
        "select count(*) as count " +
        "from t_places pl " +
        "where pl.name || pl.address in (${String.join(",", randPlaces)})  "
).count

if (existCount > 3) {
    println "Data already exist. Will insert nothing"
    sql.close()
    return
}

for (Place place: places) {
    try {
        sql.execute(
                "INSERT\n " +
                        "INTO t_places\n " +
                        "(name, description, address, latitude, longitude, price_from, price_to)\n " +
                        "values\n " +
                        "( '${place.name}', '${place.description}', '${place.address}', \n" +
                        " ${place.lat}, ${place.lng}, ${place.priceFrom}, ${place.priceTo} ) ;"
        )
    } catch (Exception e) {
        println "Exception on place ${place}"
    }
}

sql.close()