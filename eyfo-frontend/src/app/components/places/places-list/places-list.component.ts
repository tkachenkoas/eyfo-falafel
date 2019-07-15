import {AfterViewInit, Component} from '@angular/core';
import {IPlace} from "../../../models/model-interfaces";
import {PlacesService} from "../../../services/places.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-places-list',
  templateUrl: './places-list.component.html',
  styleUrls: ['./places-list.component.css']
})
export class PlacesListComponent implements AfterViewInit  {

  displayedColumns: string[] = ['id', 'name', 'address', 'edit'];
  data: IPlace[] = [];
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

  editPlace(place: IPlace) {
    this.router.navigate([`places/${place.id}/edit`]);
  }

}
