package com.atstudio.eyfofalafel.backend.controller

import com.atstudio.eyfofalafel.backend.controller.auth.UserRestDto
import com.atstudio.eyfofalafel.backend.controller.location.LocationRestDTO
import com.atstudio.eyfofalafel.backend.controller.review.ReviewRestDto
import com.atstudio.eyfofalafel.backend.entities.place.Location
import com.atstudio.eyfofalafel.backend.entities.review.Review
import com.atstudio.eyfofalafel.backend.entities.security.User
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import java.time.LocalDateTime

class MapperHelper {

    static Location testLocation() {
        Location location = new Location()
        location.setAddress("address")
        location.setCoordinates(
                new GeometryFactory().createPoint(
                        new Coordinate(1, 10)
                )
        )
        return location
    }

    static LocationRestDTO testLocationDto() {
        return [
                address: "address",
                latitude: 10,
                longitude: 1
        ] as LocationRestDTO
    }

    static void assertLocationsEquality(LocationRestDTO restDTO, Location location) {
        assert location.getAddress() == restDTO.getAddress() &&
                location.getLatitude() == restDTO.getLatitude() &&
                location.getLongitude() == restDTO.getLongitude()
    }

    static ReviewRestDto sampleRestReview() {
        return new ReviewRestDto(
                id: 10,
                placeId: 10,
                comment: 'some_comment',
                author: sampleRestUser(),
                rating: 4,
                creationDateTime: LocalDateTime.now()
        )
    }

    static Review sampleReview() {
        return new Review(
                id: 5,
                placeId: 5,
                comment: 'some_comment',
                author: sampleUser(),
                rating: 5,
                creationDateTime: LocalDateTime.now()
        )
    }

    static User sampleUser() {
        return new User(
                id: 1,
                password: 'hashed_password',
                lastName: 'LastName',
                firstName: 'FirstName',
                userName: 'UserName',
        )
    }

    static UserRestDto sampleRestUser() {
        return new UserRestDto(
                id: 1,
                password: 'password',
                lastName: 'LastName',
                firstName: 'FirstName',
                userName: 'UserName',
        )
    }

}