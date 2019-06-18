import {AfterViewInit, Component, forwardRef, OnInit} from '@angular/core';
import {
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
  Validators
} from "@angular/forms";
import {LocationService} from "../../../../services/location.service";
import {debounceTime, finalize, switchMap, tap} from "rxjs/operators";
import {LatLngLiteral} from "@agm/core";
import {ILocation} from "../../../../models/location";


@Component({
  selector: 'location-picker',
  templateUrl: './location.component.html',
  styleUrls: ['./location.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LocationComponent),
      multi: true
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => LocationComponent),
      multi: true
    }
  ]
})
export class LocationComponent implements OnInit, AfterViewInit, ControlValueAccessor, Validator {

  ZOOM_FAR: number = 8;
  ZOOM_CLOSE: number = 15;
  zoom = this.ZOOM_FAR;

  userLocation: ILocation = {longitude: 0, latitude: 0};
  addressSuggestions: string[];
  isLoading: boolean = false;

  constructor(private locationService: LocationService,
              private formBuilder: FormBuilder) {
  }

  addressSearch: FormControl = new FormControl();
  locationForm: FormGroup = this.formBuilder.group({
    address: ['', [Validators.required]],
    latitude: ['', [Validators.required]],
    longitude: ['', [Validators.required]],
  });

  ngOnInit() {
    this.locationService.userLocationSubject.subscribe(coords => {
        this.setCenterLocation(coords);
        this.zoom = this.ZOOM_CLOSE;
      });

    this.addressSearch.valueChanges
      .pipe(
        debounceTime(500),
        tap(() => this.isLoading = true),
        switchMap(value => this.locationService
          .getAddressSuggestions(value)
          .pipe(
            finalize(() => this.isLoading = false),
          )
        )).subscribe(results => {
      console.log(results);
      this.addressSuggestions = results;
    });
  }

  ngAfterViewInit(): void {
    this.locationService.requestUserLocation();
  }

  get locationControls() {
    return this.locationForm.controls;
  }

  getFormValue(name: string): any {
    const control = this.locationControls[name];
    return control ? control.value : ''
  }

  onAddressSelection(address: string) {
    this.patchFormValue('address', address);
    this.locationService.getLocationByAddress(address)
      .then((location) => {
        this.setCenterLocation(location);
        this.zoom = this.ZOOM_CLOSE;
        const {latitude, longitude} = location;
        this.patchFormValue('latitude', latitude);
        this.patchFormValue('longitude', longitude);
      });
    this.addressSearch.patchValue('', {emitEvent: false});
  }

  private patchFormValue(control: string, value: any) : void {
    this.locationControls[control].patchValue(value);
  }

  private setCenterLocation(location: ILocation) : void {
    if (!location) return;
    this.userLocation = location;
  }

  markerDragEnd(coords: LatLngLiteral) {
    console.log('dragEnd', coords);

    this.locationService.getLocationByCoords(coords)
      .then(loc => this.patchFormValue('address', loc.address));

    const {latitude, longitude} = { latitude: coords.lat,
                                    longitude: coords.lng };

    this.patchFormValue('latitude', latitude);
    this.patchFormValue('longitude', longitude);

    this.setCenterLocation({ latitude, longitude });
  }

  registerOnChange(fn: any): void {
    this.locationForm.valueChanges.subscribe(fn);
  }

  onTouched: () => void = () => {};

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    isDisabled ? this.locationForm.disable() : this.locationForm.enable();
  }

  writeValue(val: any): void {
    if (!val) return;
    const location = {...val} as ILocation;
    this.setCenterLocation(location);
    this.zoom = this.ZOOM_CLOSE;
    this.locationForm.setValue({...location}, {emitEvent: false});
  }

  validate(control: FormControl): ValidationErrors | null {
    return this.locationForm.valid ? null : {
      locationFormError: {
        valid: false,
      }
    };
  }

}
