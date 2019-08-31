package com.atstudio.eyfofalafel.backend.domain.place;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.function.Function;

import static java.math.BigDecimal.valueOf;
import static java.util.Optional.ofNullable;

@Embeddable
@Data
public class Location {

    @Column(name = "address")
    private String address;

    @Column(name = "coordinates", columnDefinition = "geography(Point,4326)")
    private Point coordinates;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Transient
    public BigDecimal getLatitude() {
        return getCoord(Point::getY);
    }

    private BigDecimal getCoord(Function<Point, Double> getter) {
        return ofNullable(coordinates).map(coords -> valueOf(getter.apply(coords))).orElse(null);
    }

    @Transient
    public BigDecimal getLongitude() {
        return getCoord(Point::getX);
    }

}
