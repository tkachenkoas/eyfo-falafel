import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Place} from "../../../models/place";
import {PlacesService} from "../../../services/places.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-places-list',
  templateUrl: './places-list.component.html',
  styleUrls: ['./places-list.component.css']
})
export class PlacesListComponent implements AfterViewInit  {

  displayedColumns: string[] = ['id', 'name', 'address', 'edit'];
  data: Place[] = [];
  isLoadingResults = true;

  constructor(private placeService: PlacesService,
              private router: Router) { }

  ngAfterViewInit() {
    this.loadPlaces();
  }

  private loadPlaces(): void {
    this.isLoadingResults = true;
    this.placeService.getPlaces()
                     .then(res => {
                        this.data = res;
                        this.isLoadingResults = false;
                      });
  }

  public editPlace(place: Place) {
    this.router.navigate([`places/${place.id}/edit`]);
  }

}
