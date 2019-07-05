import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ILocation} from '../models/model-interfaces';
import {LatLngLiteral} from '@agm/core';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  userLocationSubject: Subject<Coordinates> = new Subject();
  constructor(private http: HttpClient) {
  }

  getAddressSuggestions(searchStr: string): Observable<string[]> {
    return this.http.get<string[]>('location/address-suggestions', {
      params: {
        searchStr: searchStr
      }
    });
  }

  getLocationByCoords(coords: LatLngLiteral): Promise<ILocation> {
    return this.http.get<ILocation>('location/address-by-location', {
      params: {
        lat: coords.lat.toString(),
        lng: coords.lng.toString()
      }
    }).toPromise();
  }

  getLocationByAddress(address: string): Promise<ILocation> {
    return this.http.get<ILocation>('location/location-by-address', {
      params: {
        address : address
      }
    }).toPromise();
  }

  requestUserLocation(): void {
    if (!window.navigator || !window.navigator.geolocation) {
      console.log('No navigation available');
      return;
    }
    window.navigator.geolocation.getCurrentPosition(
      position => {
        this.userLocationSubject.next(position.coords);
        console.log(position);
      },
      error => {
        switch (error.code) {
          case 1:
            console.log('Permission Denied');
            break;
          case 2:
            console.log('Position Unavailable');
            break;
          case 3:
            console.log('Timeout');
            break;
        }
      }
    );
  }
}
