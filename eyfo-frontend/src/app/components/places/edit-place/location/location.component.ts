import {Component, forwardRef, OnInit, ViewChild} from '@angular/core';
import {
  ControlValueAccessor,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  Validators,
  Validator, ValidationErrors
} from "@angular/forms";
import {LocationService} from "../../../../services/location.service";
import {debounceTime, finalize, switchMap, tap} from "rxjs/operators";
import {AgmMap, LatLngLiteral} from "@agm/core";
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
export class LocationComponent implements OnInit, ControlValueAccessor, Validator {

  zoom = 8;
  public selectedLocation: ILocation = {longitude: 0, latitude: 0};
  private addressSuggestions: string[];
  isLoading = false;

  constructor(private locationService: LocationService, private formBuilder: FormBuilder) { }

  public addressSearch: FormControl = new FormControl();
  public locationForm: FormGroup = this.formBuilder.group({
    address: ['', [Validators.required]],
    latitude: ['', [Validators.required]],
    longitude: ['', [Validators.required]],
  });

  ngOnInit() {
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
    this.locationService.locationSubject
      .subscribe(coords => {
        this.setSelectedLocation(coords);
        this.zoom = 14;
      });
    this.locationService.refreshLocation();
  }

  get locationControls() {
    return this.locationForm.controls;
  }

  public formValue(name: string): any {
    const control = this.locationControls[name];
    return control ? control.value : ''
  }

  onAddressSelection(address: string) {
    this.patch('address', address);
    this.locationService.getLocationByAddress(address)
      .then(res => {
        console.log(res);
        this.setSelectedLocation(res);
        this.zoom = 17;
      });
    this.addressSearch.patchValue('', {emitEvent: false});
  }

  private patch(control: string, value: any) : void {
    this.locationControls[control].patchValue(value);
  }

  private setSelectedLocation(location: ILocation) : void {
    if (!location) return;
    this.selectedLocation = location;
    this.patch('latitude', location.latitude);
    this.patch('longitude', location.longitude);
  }

  markerDragEnd(coords: LatLngLiteral) {
    console.log('dragEnd', coords);
    this.setSelectedLocation({ latitude: coords.lat,
                               longitude: coords.lng });
    this.locationService.getAddressByLocation(coords)
      .then(addr => this.patch('address', addr));
  }

  registerOnChange(fn: any): void {
    this.locationForm.valueChanges.subscribe(fn);
  }

  public onTouched: () => void = () => {};

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    isDisabled ? this.locationForm.disable() : this.locationForm.enable();
  }

  writeValue(val: any): void {
    val && this.locationForm.setValue(val, {emitEvent: false});
  }

  validate(control: FormControl): ValidationErrors | null {
    return this.locationForm.valid ? null : {
      locationFormError: {
        valid: false,
      }
    };
  }

}
