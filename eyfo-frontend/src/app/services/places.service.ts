import { Injectable } from '@angular/core';
import {Place} from "../models/place";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private http: HttpClient) { }

  public createPlace(place: Place) {
    this.http.post('places/new', place).toPromise()
  }
}