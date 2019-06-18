import {Component, OnDestroy, OnInit} from '@angular/core';
import {PlacesService} from '../../../services/places.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Place} from "../../../models/place";
import {ActivatedRoute, Router} from "@angular/router";
import {logAndReturn} from "../../../utils/logging";

@Component({
  selector: 'app-edit-place',
  templateUrl: './edit-place.component.html',
  styleUrls: ['./edit-place.component.css']
})
export class EditPlaceComponent implements OnInit {
  placeForm: FormGroup;
  placeId: number;

  constructor(private placesService: PlacesService,
              private router: Router,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder) {
  }

  editMode(): boolean {
    return !!this.placeId;
  }

  ngOnInit() {
    this.route.url.subscribe((urls) => {
      const edit = urls.some(url => url.path == 'edit');
      if (edit) {
        this.placeId = parseInt(urls.map(url => url.path)
          .find(path  => /^\d+$/.test(path)));
        this.placesService.getPlaceById(this.placeId)
          .then((place) => {
            const {name, location} = place;
            this.placeForm.setValue({name, location});
          });
      }
    });

    this.placeForm = this.formBuilder.group({
      name: ['', Validators.required],
      location: new FormControl()
    });
  }

  get frm() {
    return this.placeForm.controls;
  }

  hasErrors = (controlName: string) : boolean =>{
    const  control = this.frm[controlName];
    return control.touched && !control.valid;
  };

  submitForm() {
    if (!this.placeForm.valid) return;

    const place: Place = this.placeForm.value as Place;
    place.id = this.placeId;
    const saveAction: Promise<Place> = this.editMode()
                                      ? this.placesService.patchPlace(logAndReturn(place, 'Create new place'))
                                      : this.placesService.createPlace(logAndReturn(place, 'Create new place'));
    saveAction.then( () => {
      this.router.navigate(['places']);
    });
  }
}
