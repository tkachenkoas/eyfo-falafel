import {Component, OnInit} from '@angular/core';
import {IPlace} from "../../../models/model-interfaces";
import {PlacesService} from "../../../services/places.service";
import {ActivatedRoute} from "@angular/router";
import {getUrlWithHost} from "../../../util/http";

@Component({
  selector: 'app-view-place',
  templateUrl: './view-place.component.html',
  styleUrls: ['./view-place.component.css']
})
export class ViewPlaceComponent implements OnInit {
  place: IPlace;
  placeId: number;

  constructor(private placesService: PlacesService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.url.subscribe((urls) => {
      this.placeId = parseInt(urls[urls.length-1].path);
      this.placesService.getPlaceById(this.placeId)
        .then((place) => {
          this.place = place;
        });
    });
  }

  getImages() {
    return (this.place.attachments || []).map(att => getUrlWithHost(att.fullPath));
  }
}
