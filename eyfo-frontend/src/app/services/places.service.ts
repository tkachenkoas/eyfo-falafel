import { Injectable } from '@angular/core';
import {Place} from "../models/place";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private http: HttpClient) { }

  public getPlaces(): Promise<Place[]> {
    return this.http.get('places/').pipe(
      map(data => {
        console.log(`Places list: ${JSON.stringify(data)}`)
        return data;
      })
    ).toPromise() as Promise<Place[]>;
  }

  public createPlace(place: Place) {
    this.http.post('places/new', place).pipe(
      map(data => {
        console.log(`Created place: ${JSON.stringify(data)}`)
        return data;
      })
    ).toPromise();
  }
}
