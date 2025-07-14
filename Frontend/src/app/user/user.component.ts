import {Component, OnInit, inject} from '@angular/core';
import {UserService} from '../services/user.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  standalone: true,
  imports: [NgIf],
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  board?: string;
  errorMessage?: string;

  private userService = inject(UserService);

  ngOnInit() {
    this.userService.getUserPage().subscribe({
      next: (data: string) => {
        this.board = data;
      },
      error: (error: any) => {
        this.errorMessage = `${error.status}: ${JSON.parse(error.error).message}`;
      }
    });
  }
}
