import {Component, OnInit} from '@angular/core';
import {PlacesService} from '../../../services/places.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IImgAttachment, IPlace} from '../../../models/model-interfaces';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../../environments/environment';
import {DropzoneConfigInterface} from "ngx-dropzone-wrapper";
import {LoginService} from "../../../services/login.service";
import {getUrlWithHost} from "../../../util/http";

@Component({
  selector: 'app-edit-place',
  templateUrl: './edit-place.component.html',
  styleUrls: ['./edit-place.component.css']
})
export class EditPlaceComponent implements OnInit {

  placeForm: FormGroup;
  placeId: number;
  images: IImgAttachment[] = [];
  uploading: boolean;

  constructor(private placesService: PlacesService,
              private router: Router,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder) {
  }

  dzConfig: DropzoneConfigInterface = {
      url: `${environment.apiUrl}files/upload-temp`,
      maxFilesize: 10,
      acceptedFiles: 'image/*',
      createImageThumbnails: false,
      previewTemplate: '<div></div>',
      headers: LoginService.getAuthHeader()
  };

  editMode(): boolean {
    return !!this.placeId;
  }

  ngOnInit() {
    this.route.url.subscribe((urls) => {
      const edit = urls.some(url => url.path == 'edit');
      if (edit) {
        this.placeId = parseInt(
          urls.map(url => url.path).find(path  => /^\d+$/.test(path))
        );
        this.placesService.getPlaceById(this.placeId)
          .then((place) => {
            const {name, location} = place;
            this.placeForm.setValue({name, location});
            this.images = place.attachments || [];
          });
      }
    });

    this.placeForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.pattern(/[а-яa-z]{3}/i)]],
      location: new FormControl()
    });
  }

  removeImage(removal: IImgAttachment) {
    this.images = this.images.filter(img => img != removal);
  }

  startUpload() {
    this.uploading = true;
  }

  onImageUpload = (event) => {
    this.uploading = false;
    this.images.push(event[1] as IImgAttachment);
  }

  hasErrors = (controlName: string): boolean => {
    const  control = this.frm[controlName];
    return control.touched && !control.valid;
  }

  submitForm() {
    if (!this.placeForm.valid) { return; }

    const place: IPlace = this.placeForm.value as IPlace;
    place.id = this.placeId;
    place.attachments = this.images;
    const saveAction: Promise<IPlace> = this.editMode()
                                      ? this.placesService.patchPlace(place)
                                      : this.placesService.createPlace(place);
    saveAction.then( () => {
      this.router.navigate(['places/list']);
    });
  }

  get frm() {
    return this.placeForm.controls;
  }

  withHost(fullPath: string) {
    return getUrlWithHost(fullPath);
  }
}
