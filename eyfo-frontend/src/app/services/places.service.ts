import { Injectable } from '@angular/core';
import {Place} from "../models/place";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {logAndReturn} from "../utils/logging";

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private http: HttpClient) { }

  public getPlaces(): Promise<Place[]> {
    return this.http.get('places/').pipe(
      map((data: Place[]) => {
        return logAndReturn(data, 'Place list');
      })
    ).toPromise();
  }

  public getPlaceById(id: number): Promise<Place> {
    return this.http.get(`places/${id}`).pipe(
      map((data: Place) => {
        return logAndReturn(data, `Place id=${id}`);
      })
    ).toPromise();
  }

  public createPlace(place: Place): Promise<Place> {
    return this.http.post('places/new', place).pipe(
      map((data: Place) => {
        return logAndReturn(data, 'Created place');
      })
    ).toPromise();
  }

  public patchPlace(place: Place): Promise<Place> {
    const {id} = place;
    if (Number.isNaN(id)) return;
    return this.http.post(`places/${id}`, place).pipe(
      map((data: Place) => {
        return logAndReturn(data, 'Patched place');
      })
    ).toPromise();
  }
}
