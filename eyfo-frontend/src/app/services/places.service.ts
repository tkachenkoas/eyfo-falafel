import { Injectable } from '@angular/core';
import {IImgAttachment, IPlace, Pageable} from '../models/model-interfaces';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {logAndReturn} from '../utils/logging';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private http: HttpClient) { }

  public getPlaces(searchText: string): Promise<IPlace[]> {
    const params = new HttpParams()
      .set('searchText', searchText || '');

    return this.http.get('places/', {params})
      .pipe(
        map((data: Pageable<IPlace>) => {
          return logAndReturn(data.content, 'Place list');
        })
      ).toPromise();
  }

  public getPlaceById(id: number): Promise<IPlace> {
    return this.http.get(`places/${id}`).pipe(
      map((data: IPlace) => {
        return logAndReturn(data, `Place id=${id}`);
      })
    ).toPromise();
  }

  public createPlace(place: IPlace): Promise<IPlace> {
    return this.http.post('places/new', place).pipe(
      map((data: IPlace) => {
        return logAndReturn(data, 'Created place');
      })
    ).toPromise();
  }

  public uploadImage(file: File): Promise<IImgAttachment> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post('files/upload-temp', formData)
      .pipe(map((data: IImgAttachment) => {
        data.fullPath = environment.serverHost + data.fullPath;
        return data;
      }))
      .toPromise();
  }

  public patchPlace(place: IPlace): Promise<IPlace> {
    const {id} = place;
    if (!id || Number.isNaN(id)) { return; }
    return this.http.put(`places/${id}`, place).pipe(
      map((data: IPlace) => {
        return logAndReturn(data, 'Patched place');
      })
    ).toPromise();
  }

  deletePlace(id: number) {
    return this.http.delete(`places/${id}`).toPromise();
  }
}
