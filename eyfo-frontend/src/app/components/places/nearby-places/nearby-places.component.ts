import {AfterViewInit, Component, OnInit} from '@angular/core';
import {LocationService} from "../../../services/location.service";
import {ILocation, IPlace} from "../../../models/model-interfaces";
import {debounceTime, finalize, switchMap, tap} from "rxjs/operators";
import {LatLngLiteral} from "@agm/core";

@Component({
  selector: 'app-nearby-places',
  templateUrl: './nearby-places.component.html',
  styleUrls: ['./nearby-places.component.css']
})
export class NearbyPlacesComponent implements AfterViewInit, OnInit {

  ZOOM_FAR: number = 8;
  ZOOM_CLOSE: number = 15;
  zoom = this.ZOOM_FAR;

  userLocation: ILocation = {longitude: 0, latitude: 0};
  places: IPlace[];

  constructor(private locationService: LocationService) { }

  ngAfterViewInit(): void {
    this.locationService.requestUserLocation();
  }

  ngOnInit() {
    this.locationService.userLocationSubject.subscribe(coords => {
      this.userLocation = coords;
      this.zoom = this.ZOOM_CLOSE;
    });

  }

  centerChanged(coords: LatLngLiteral) {
    console.log(event);
  }
}
