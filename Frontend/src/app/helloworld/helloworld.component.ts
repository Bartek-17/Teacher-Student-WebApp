import {Component, inject} from '@angular/core';
import {NgIf} from '@angular/common';
import {TokenStorageService} from '../auth/token-storage.service';

@Component({
  selector: 'app-helloworld',
  imports: [NgIf],
  templateUrl: './helloworld.component.html',
  standalone: true,
  styleUrl: './helloworld.component.css'
})
export class HelloworldComponent {

  info: any;

  private tokenStorage = inject(TokenStorageService);

  ngOnInit() {
    this.info = {
      token: this.tokenStorage.getToken(),
      username: this.tokenStorage.getUsername(),
      authorities: this.tokenStorage.getAuthorities()
    };
  }

  logout() {
    this.tokenStorage.signOut();
    window.location.reload();
  }
}

