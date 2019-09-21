export interface ILocation extends GeoCoords{
  address?: string;
}

export interface GeoCoords {
  latitude: number;
  longitude: number;
}

export interface IPlace {
  id?: number;
  location?: ILocation;
  name?: string;
  priceFrom?: number;
  priceTo?: number;
  attachments?: IImgAttachment[];
  averageRating: number;
}

export interface IReview {
  id: number;
  placeId: number;
  rating: number;
  comment: string;
  creationDateTime: Date;
}

export interface IImgAttachment {
  id?: number;
  fullPath: string;
}

export interface IPageable<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface IPaging {
  pageNumber: number;
  pageSize: number;
}
