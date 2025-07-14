import { Component, inject, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet, Router } from '@angular/router';
import { TokenStorageService } from './auth/token-storage.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, NgIf],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'WebAppProject';
  private roles?: string[];
  authority?: string;

  private tokenStorage = inject(TokenStorageService);
  private router = inject(Router);

  ngOnInit() {
    this.checkLoggedIn();
  }

  private checkLoggedIn(): void {
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every(role => {
        if (role === 'ROLE_ADMIN') {
          this.authority = 'admin';
          return false;
        }
        this.authority = 'user';
        return true;
      });
    }
  }

  logout(): void {
    this.tokenStorage.signOut();
    this.authority = undefined;
    this.router.navigate(['/auth/login']);
  }
}
