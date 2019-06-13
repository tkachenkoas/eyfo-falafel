export interface RestResponse<T> {
  success: boolean;
  body? : T;
  errorMessage?: string;
  status: number;
}
