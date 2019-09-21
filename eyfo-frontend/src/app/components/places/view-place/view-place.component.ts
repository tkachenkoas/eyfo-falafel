import {Component, OnInit} from '@angular/core';
import {IPlace, IReview} from "../../../models/model-interfaces";
import {PlacesService} from "../../../services/places.service";
import {ActivatedRoute} from "@angular/router";
import {getUrlWithHost} from "../../../util/http";
import {ReviewsService} from "../../../services/reviews.service";

@Component({
  selector: 'app-view-place',
  templateUrl: './view-place.component.html',
  styleUrls: ['./view-place.component.css']
})
export class ViewPlaceComponent implements OnInit {
  place: IPlace;
  placeId: number;

  imagesObj;
  reviews: IReview[];
  newReview: IReview = {} as IReview;
  private submittedReview: boolean;

  constructor(private placesService: PlacesService,
              private reviewsService: ReviewsService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.url.subscribe((urls) => {
      this.placeId = parseInt(urls[urls.length - 1].path);
      this.placesService.getPlaceById(this.placeId)
        .then((place) => {
          this.place = place;
        });
      this.reviewsService.getReviews(this.placeId)
        .then( (reviews) => this.reviews = reviews || []);
    });
  }

  getImages() {
    if (!this.imagesObj) {
      this.imagesObj = (this.place.attachments || []).map(att => {
        const url = getUrlWithHost(att.fullPath);
        return {
          width: 200,
          height: 200,
          image: url,
          thumbImage: url
        }
      });
    }
    return this.imagesObj;
  }

  onReviewRate({newValue}) {
    this.newReview.rating = newValue;
    this.submittedReview = false;
  }

  addReview() {
    this.submittedReview = true;
    if (!this.newReviewValid()) {
      return;
    }
    this.reviewsService.createReview(this.placeId, this.newReview)
      .then((review) => {
        this.newReview = {} as IReview;
        this.reviews.push(review);
      })
  }

  private newReviewValid() {
    return this.newReview.rating > 0 && !!this.newReview.comment;
  }

  getAvgRating() {
    if (this.reviews.length == 0) return null;
    return this.reviews.map(rv => rv.rating).reduce( (a,b) => a +b ) / this.reviews.length;
  }
}
