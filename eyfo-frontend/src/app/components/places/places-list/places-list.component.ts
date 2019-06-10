import {Component, OnInit} from '@angular/core';
import {Place} from "../../../models/place";
import {PlacesService} from "../../../services/places.service";

@Component({
  selector: 'app-places-list',
  templateUrl: './places-list.component.html',
  styleUrls: ['./places-list.component.css']
})
export class PlacesListComponent implements OnInit {

  constructor(private placeService: PlacesService) { }

  placeList : Place[];

  ngOnInit() {
    this.loadPlaces();
  }

  private loadPlaces(): void {
    this.placeService.getPlaces().then(res => this.placeList = res);
  }

}
