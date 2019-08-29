package com.atstudio.eyfofalafel.backend.domain.place;

import lombok.Data;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@Embeddable
@Data
public class Location {

    @Column(name = "address")
    private String address;

    @Column(name = "coordinates")
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

    public void setLatLng(BigDecimal lat, BigDecimal lng) {
        coordinates = new Point(lat.doubleValue(), lng.doubleValue());
    }

    @Transient
    public BigDecimal getLatitude() {
        return valueOf(coordinates.getY());
    }

    @Transient
    public BigDecimal getLongitude() {
        return valueOf(coordinates.getX());
    }

    public static Location ofLatLng(Double lat, Double lng) {
        Location location = new Location();
        location.setCoordinates(new Point(lat, lng));
        return location;
    }

}
