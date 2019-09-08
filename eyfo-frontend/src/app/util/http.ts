import {environment} from "../../environments/environment";

export const HttpStatus = require('http-status-codes');

export const getUrlWithHost = (url: string) => `${environment.serverHost}${url}`;
