import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SubjectService } from '../subject.service';
import { Subject } from '../../models/subject';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-subject-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './subject-form.component.html',
  styleUrls: ['./subject-form.component.css']
})
export class SubjectFormComponent implements OnInit {
  subjectForm: FormGroup;
  errorMessage: string = '';
  isSubmitting: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private subjectService: SubjectService,
    private authService: AuthService,
    private router: Router
  ) {
    this.subjectForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.subjectForm.invalid) {
      return;
    }

    this.isSubmitting = true;

    // create new subject (will be assigned to logged in teacher)
    const newSubject: Subject = {
      name: this.subjectForm.value.name
    };

    this.subjectService.createSubject(newSubject).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/admin/subjects']);
      },
      error: (error: any) => {
        this.isSubmitting = false;
        this.errorMessage = 'An error occurred while adding the subject. Please try again.';
        console.error('Error adding subject:', error);
      }
    });
  }
}
