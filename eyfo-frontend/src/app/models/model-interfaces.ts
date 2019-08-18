export interface ILocation {
  address?: string;
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
}

export interface IImgAttachment {
  id?: number;
  fullPath: string;
}

export interface Pageable<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
