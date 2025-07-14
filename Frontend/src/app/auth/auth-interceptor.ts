import {inject, Injectable} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {TokenStorageService} from './token-storage.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private tokenStorage = inject(TokenStorageService);

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log("class-based-interceptor");
    let authReq = req;
    const token = this.tokenStorage.getToken();
    if (token != "{}") {
      authReq = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
    }
    return next.handle(authReq);
  }
}

