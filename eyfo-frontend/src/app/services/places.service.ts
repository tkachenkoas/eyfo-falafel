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
      map((data: Place[]) => {
        console.log(`Places list: ${JSON.stringify(data)}`);
        return data;
      })
    ).toPromise();
  }

  public createPlace(place: Place): Promise<Place> {
    return this.http.post('places/new', place).pipe(
      map((data: Place)=> {
        console.log(`Created place: ${JSON.stringify(data)}`);
        return data;
      })
    ).toPromise();
  }
}
