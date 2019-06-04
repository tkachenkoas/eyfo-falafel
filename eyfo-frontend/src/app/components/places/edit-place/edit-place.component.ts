import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PlacesService} from '../../../services/places.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {debounceTime, finalize, switchMap, tap} from 'rxjs/operators';
import {LocationsService} from '../../../services/locations.service';
import {AgmMap, LatLngLiteral} from '@agm/core';

@Component({
  selector: 'app-edit-place',
  templateUrl: './edit-place.component.html',
  styleUrls: ['./edit-place.component.css']
})
export class EditPlaceComponent implements OnInit, OnDestroy {
  zoom = 8;
  lat: number;
  lng: number;
  @ViewChild('googleMap', {static: true}) agmMap: AgmMap;

  addrSearch: FormControl = new FormControl();

  private addressSuggestions: string[];
  placeForm: FormGroup;
  isLoading = false;

  constructor(private placesService: PlacesService
            , private locationsService: LocationsService
            , private formBuilder: FormBuilder) {
  }

  ngOnDestroy(): void {
    this.locationsService.locationSubject.unsubscribe();
  }

  ngOnInit() {
    this.placeForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      location: this.formBuilder.group({
        address: ['', [Validators.required]],
        latitude: ['', [Validators.required]],
        longitude: ['', [Validators.required]],
      })
    });
    this.addrSearch.valueChanges
      .pipe(
        debounceTime(1000),
        tap(() => this.isLoading = true),
        switchMap(value => this.locationsService
          .getAddressSuggestions(value)
          .pipe(
            finalize(() => this.isLoading = false),
          )
        )).subscribe(results => {
      console.log(results);
      this.addressSuggestions = results;
    });
    this.locationsService.locationSubject
      .subscribe(coords => {
        this.lat = coords ? coords.latitude : 0;
        this.lng = coords ? coords.longitude : 0;
        this.zoom = 14;
      });
    this.locationsService.refreshLocation();
  }

  onAddressSelection(address: string) {
    this.loc['address'].patchValue(address);
    this.locationsService.getLocationByAddress(address)
      .subscribe(res => {
        console.log(res);
        this.lat = res.latitude;
        this.loc['latitude'].patchValue(res.latitude);
        this.lng = res.longitude;
        this.loc['longitude'].patchValue(res.longitude);

        this.zoom = 17;
      });
    this.addrSearch.patchValue('');
  }

  markerDragEnd(coords: LatLngLiteral) {
    console.log('dragEnd', coords);

    this.lat = coords.lat;
    this.loc['latitude'].patchValue(coords.lat);
    this.lng = coords.lng;
    this.loc['longitude'].patchValue(coords.lng);

    this.locationsService.getAddresByLocation(coords)
      .subscribe(addr => this.loc['address'].patchValue(addr));
  }


  get frm() {
    return this.placeForm.controls;
  }

  get loc(): FormGroup {
    return this.frm.location['controls'];
  }

}
