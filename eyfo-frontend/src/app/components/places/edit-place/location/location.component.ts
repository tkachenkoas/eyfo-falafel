import {AfterViewInit, Component, forwardRef, OnDestroy, OnInit} from '@angular/core';
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
export class LocationComponent implements OnInit, AfterViewInit, OnDestroy, ControlValueAccessor, Validator {

  zoom = 8;
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
    this.locationService.userLocationSubject
      .subscribe(coords => {
        this.setSelectedLocation(coords);
        this.zoom = 14;
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

  ngOnDestroy(): void {
    this.locationService.userLocationSubject.unsubscribe();
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
      .then(res => {
        console.log(res);
        this.setSelectedLocation(res);
        this.zoom = 17;
      });
    this.addressSearch.patchValue('', {emitEvent: false});
  }

  private patchFormValue(control: string, value: any) : void {
    this.locationControls[control].patchValue(value);
  }

  private setSelectedLocation(location: ILocation) : void {
    if (!location) return;
    this.userLocation = location;

  }

  markerDragEnd(coords: LatLngLiteral) {
    console.log('dragEnd', coords);

    this.locationService.getAddressByLocation(coords)
      .then(addr => this.patchFormValue('address', addr));

    const {latitude, longitude} = { latitude: coords.lat,
                                    longitude: coords.lng };

    this.patchFormValue('latitude', latitude);
    this.patchFormValue('longitude', longitude);

    this.setSelectedLocation({ latitude, longitude });
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
    if (val) {
      const location = {...val} as ILocation;
      delete location.id;
      this.locationForm.setValue({...location}, {emitEvent: false});
    }
  }

  validate(control: FormControl): ValidationErrors | null {
    return this.locationForm.valid ? null : {
      locationFormError: {
        valid: false,
      }
    };
  }

}
