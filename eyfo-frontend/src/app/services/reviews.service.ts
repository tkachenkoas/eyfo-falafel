import {Injectable} from '@angular/core';
import {IReview} from '../models/model-interfaces';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {

  constructor(private http: HttpClient) { }

  public getReviews(placeId: number): Promise<IReview[]> {
    return this.http.get(`places/${placeId}/reviews/`).toPromise() as Promise<IReview[]>;
  }

  public createReview(placeId: number, review: IReview): Promise<IReview> {
    return this.http.post(`places/${placeId}/reviews/new`, review).toPromise() as Promise<IReview>;
  }

}
