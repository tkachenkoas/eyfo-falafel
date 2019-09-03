@GrabConfig( systemClassLoader=true )
import groovy.sql.Sql
import groovy.json.JsonSlurper
import org.postgresql.Driver

import java.sql.Connection
import java.sql.DriverManager

import static java.math.RoundingMode.HALF_DOWN

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

def allPlaces = [] as List<Place>

for (def jsonPlace: data['results']) {
    allPlaces << (new Place(
            name: (jsonPlace['name'] as String).replaceAll("'", "-"),
            description: String.join(',', jsonPlace['types'] as List),
            address: jsonPlace['vicinity'],
            lat: ((jsonPlace.geometry.location.lat) as BigDecimal).setScale(5, HALF_DOWN),
            lng: ( (jsonPlace.geometry.location.lng) as BigDecimal).setScale(5, HALF_DOWN),
            priceFrom: (int)(jsonPlace['price_level']?:2) * 60,
            priceTo: (int)(jsonPlace['price_level']?:3) * 80
    ))
}

println("Loaded ${allPlaces.size()} places from json")

def placeSynthIds = allPlaces.collect { place ->
    return "'${place.name + place.address}'" as String
}

Set<String> placesToAddIds = new HashSet<>(placeSynthIds);

sql.eachRow(
        "select * " +
        "from t_places pl " +
        "where pl.name || pl.address in (${String.join(",", placeSynthIds)})  "
) { row ->
    placesToAddIds.remove("'${row['name'] + row['address']}'" as String)
}

if (placesToAddIds.isEmpty()) {
    println "No new places to add, will terminate"
    sql.close()
    return
}

def placesToAdd = allPlaces.findAll{ place ->
    placesToAddIds.contains("'${place.name + place.address}'" as String)
}

println("Will add new ${placesToAdd.size()} places to db")

int added = 0;

for (Place place: placesToAdd) {
    try {
        sql.execute(
                "INSERT\n " +
                        "INTO t_places\n " +
                        "(id, name, description, address, coordinates, price_from, price_to)\n " +
                        "values\n " +
                        "( nextval('t_places_id_seq'), '${place.name}', '${place.description}', '${place.address}', \n" +
                        " 'POINT(${place.lng} ${place.lat})',  ${place.priceFrom}, ${place.priceTo} ) ;"
        )
        added++;
    } catch (Exception e) {
        println "Exception on place ${place}"
    }
}

println "Successfully added ${added} places, failed ${placesToAddIds.size() - added} results"

sql.close()