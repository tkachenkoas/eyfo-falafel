import {AfterViewInit, Component, OnInit} from '@angular/core';
import {LocationService} from "../../../services/location.service";
import {ILocation, IPlace} from "../../../models/model-interfaces";
import {debounce, isEqual} from 'lodash';
import {AgmInfoWindow, LatLngLiteral} from "@agm/core";
import {PlacesService} from "../../../services/places.service";

@Component({
  selector: 'app-nearby-places',
  templateUrl: './nearby-places.component.html',
  styleUrls: ['./nearby-places.component.css']
})
export class NearbyPlacesComponent implements AfterViewInit, OnInit {

  ZOOM_FAR: number = 8;
  ZOOM_CLOSE: number = 15;
  zoom = this.ZOOM_FAR;

  defaultCenter: ILocation = {longitude: 0, latitude: 0};
  places: IPlace[];
  selectedPlace: IPlace;

  constructor(
    private locationService: LocationService,
    private placesService: PlacesService
  ) {
  }

  ngAfterViewInit(): void {
    this.locationService.requestUserLocation();
  }

  ngOnInit() {
    this.locationService.userLocationSubject.subscribe(coords => {
      this.defaultCenter = coords;
      this.zoom = this.ZOOM_CLOSE;
    });

  }

  centerChanged = debounce( (coords: LatLngLiteral): void => {
    this.placesService.findNearby({longitude: coords.lng, latitude: coords.lat})
      .then(places => {
        if (!isEqual(this.places, places)) {
          this.places = places;
        }
      })
  }, 1000);

}
