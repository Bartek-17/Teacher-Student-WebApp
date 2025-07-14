import { Component, inject, OnInit } from '@angular/core';
import { LoginInfo } from '../auth/login-info';
import { AuthService } from '../auth/auth.service';
import { TokenStorageService } from '../auth/token-storage.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [NgIf, FormsModule],
  standalone: true,
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  token?: string;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private loginInfo?: LoginInfo;

  private authService = inject(AuthService);
  private tokenStorage = inject(TokenStorageService);
  private router = inject(Router);

  ngOnInit() {
    if (this.tokenStorage.getToken() != null && this.tokenStorage.getToken() != '{}') {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getAuthorities();
      this.redirectUserByRole();
    }
  }

  onSubmit() {
    this.loginInfo = new LoginInfo(this.form.username, this.form.password);

    this.authService.attemptAuth(this.loginInfo).subscribe({
      next: (data) => {
        this.tokenStorage.saveToken(data.accessToken || '{}');
        this.tokenStorage.saveUsername(data.username || '{}');
        this.tokenStorage.saveAuthorities(data.authorities || []);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getAuthorities();

        this.redirectUserByRole();
        this.reloadPage();
      },
      error: (error) => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    });
  }

  redirectUserByRole() {
    if (this.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/admin']);
    } else if (this.roles.includes('ROLE_USER')) {
      this.router.navigate(['/user']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  reloadPage() {
    window.location.reload(); // reload the page after logging in so that menu is displayed
  }
}
