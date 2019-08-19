import {AfterViewInit, Component, OnInit} from '@angular/core';
import {IPlace, Pageable} from '../../../models/model-interfaces';
import {PlacesService} from '../../../services/places.service';
import {Router} from '@angular/router';
import {PageEvent} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';

@Component({
  selector: 'app-places-list',
  templateUrl: './places-list.component.html',
  styleUrls: ['./places-list.component.css']
})
export class PlacesListComponent implements AfterViewInit {

  displayedColumns: string[] = ['id', 'name', 'address', 'edit'];
  places: Pageable<IPlace>;
  isLoadingResults = true;
  searchText: string;

  pageEvent: PageEvent;

  constructor(private placeService: PlacesService,
              private router: Router) { }

  ngAfterViewInit() {
    this.loadPlaces();
  }

  loadPlaces(pageEvent: PageEvent = {} as PageEvent): void {
    this.isLoadingResults = true;
    this.placeService.getPlaces(this.searchText, pageEvent.pageIndex, pageEvent.pageSize)
                     .then(page => {
                        this.places = page;
                        this.isLoadingResults = false;
                      });
  }

  editPlace(place: IPlace) {
    this.router.navigate([`places/${place.id}/edit`]);
  }

  deletePlace(place: IPlace) {
    this.placeService.deletePlace(place.id).then(() => this.loadPlaces());
  }

}
