import {Injectable} from '@angular/core';
import {GeoCoords, IImgAttachment, IPageable, IPaging, IPlace} from '../models/model-interfaces';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PlacesService {

  constructor(private http: HttpClient) { }

  public getPlaces(
    searchText: string = '',
    paging: IPaging = {pageNumber: 0, pageSize: 10}
  ): Promise<IPageable<IPlace>> {
    const params = new HttpParams()
      .set('searchText', searchText)
      .set('pageNumber', `${paging.pageNumber}`)
      .set('pageSize', `${paging.pageSize}`);

    return this.http.get('places/', {params}).toPromise() as Promise<IPageable<IPlace>>;
  }

  public findNearby(position: GeoCoords): Promise<IPlace[]> {
    const params = new HttpParams()
      .set('lat', `${position.latitude}`)
      .set('lng', `${position.longitude}`);

    return this.http.get('places/nearby', {params})
                    .toPromise() as Promise<IPlace[]>;
  }

  public getPlaceById(id: number): Promise<IPlace> {
    return this.http.get(`places/${id}`).toPromise() as Promise<IPlace>;
  }

  public createPlace(place: IPlace): Promise<IPlace> {
    return this.http.post('places/new', place).toPromise() as Promise<IPlace>;
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
    return this.http.put(`places/${id}`, place).toPromise() as Promise<IPlace>;
  }

  deletePlace(id: number) {
    return this.http.delete(`places/${id}`).toPromise();
  }
}
