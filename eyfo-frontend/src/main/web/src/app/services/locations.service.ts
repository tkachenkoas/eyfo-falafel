import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Location} from '../models/location';
import {LatLngLiteral} from '@agm/core';

@Injectable({
  providedIn: 'root'
})
export class LocationsService {

  locationSubject: Subject<Coordinates> = new Subject();
  private position: Position;
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient) {
  }

  getAddressSuggestions(searchStr: string): Observable<string[]> {
    return this.http.get<string[]>(this.apiUrl + 'location/address-suggestions', {
      params: {
        searchStr: searchStr
      }
    });
  }

  getAddresByLocation(coords: LatLngLiteral): Observable<string> {
    return this.http.get<string>(this.apiUrl + 'location/address-by-location', {
      params: {
        lat: coords.lat.toString(),
        lng: coords.lng.toString()
      }
    });
  }

  getLocationByAddress(address: string): Observable<Location> {
    return this.http.get<Location>(this.apiUrl + 'location/location-by-address', {
      params: {
        address : address
      }
    });
  }

  refreshLocation(): Coordinates {
    if (!window.navigator || !window.navigator.geolocation) {
      console.log('No navigation available');
      return;
    }
    window.navigator.geolocation.getCurrentPosition(
      position => {
        this.locationSubject.next(position.coords);
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
