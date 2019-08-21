import {AfterViewInit, Component} from '@angular/core';
import {IPageable, IPaging, IPlace} from '../../../models/model-interfaces';
import {PlacesService} from '../../../services/places.service';
import {Router} from '@angular/router';
import {PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-places-list',
  templateUrl: './places-list.component.html',
  styleUrls: ['./places-list.component.css']
})
export class PlacesListComponent implements AfterViewInit {

  displayedColumns: string[] = ['id', 'name', 'address', 'edit'];
  places: IPageable<IPlace>;
  isLoadingResults = true;
  searchText: string;

  paging: IPaging;

  constructor(private placeService: PlacesService,
              private router: Router) { }

  ngAfterViewInit() {
    this.loadPlaces();
  }

  onPageSelect(pageEvent: PageEvent) {
    this.paging = {
      pageNumber: pageEvent.pageIndex,
      pageSize: pageEvent.pageSize
    }
    this.loadPlaces();
  }

  loadPlaces(): void {
    this.isLoadingResults = true;
    this.placeService.getPlaces(this.searchText, this.paging)
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

  search() {
    this.paging = {
      pageSize: this.places.size,
      pageNumber: 0
    };
    this.loadPlaces();
  }
}
