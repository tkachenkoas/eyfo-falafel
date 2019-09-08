import {AfterViewInit, Component} from '@angular/core';
import {IPageable, IPaging, IPlace} from '../../../models/model-interfaces';
import {PlacesService} from '../../../services/places.service';
import {PageEvent} from '@angular/material/paginator';
import {MatDialog, MatDialogConfig} from "@angular/material";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogProps
} from "../../common/confirmation-dialog/confirmation-dialog.component";

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
              public dialog: MatDialog) { }

  ngAfterViewInit() {
    this.loadPlaces();
  }

  onPageSelect(pageEvent: PageEvent) {
    this.refreshList(pageEvent.pageSize, pageEvent.pageIndex);
  }

  loadPlaces(): void {
    this.isLoadingResults = true;
    this.placeService.getPlaces(this.searchText, this.paging)
                     .then(page => {
                        this.places = page;
                        this.isLoadingResults = false;
                      });
  }

  openDialog(place: IPlace): void {
    const dialogConfig: MatDialogConfig<ConfirmationDialogProps> = {
      width: '400px',
      height: '200px',
      data: {
        header: 'Подтвердите действие',
        message: `Точно удаляем точку "${place.name}"?`,
        payload: place.id
      }
    };
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(placeId => {
      if(placeId) {
        this.placeService.deletePlace(placeId)
                         .then(() => this.loadPlaces());
      }
    });
  }

  search() {
    this.refreshList(this.places.size, 0);
  }

  clear() {
    this.searchText ='';
    this.search();
  }

  private refreshList(pageSize: number, pageNumber: number) {
    this.paging = {
      pageSize,
      pageNumber
    };
    this.loadPlaces();
  }
}
