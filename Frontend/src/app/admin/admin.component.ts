import {Component, inject, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  standalone: true,
  imports: [NgIf],
  styleUrls: ['./admin.component.css']
})

export class AdminComponent implements OnInit {
  board?: string;
  errorMessage?: string;

  private userService = inject(UserService);

  ngOnInit() {
    this.userService.getAdminPage().subscribe({
      next:(data) => {
        this.board = data;
      },
      error: (error) => {
        this.errorMessage = `${error.status}: ${JSON.parse(error.error).message}`;
      }
  });
  }
}

