import { Routes } from '@angular/router';
import {HelloworldComponent} from './helloworld/helloworld.component';
import {StudentsComponent} from './students/students.component';
import {UserComponent} from './user/user.component';
import {AdminComponent} from './admin/admin.component';
import {LoginComponent} from './login/login.component';
import {RoleGuard} from './guards/role.guard';
import {authGuard} from './guards/auth.guard';

import { MyGradesComponent } from './grades/my-grades/my-grades.component';
import { AllGradesComponent } from './grades/all-grades/all-grades.component';

import { SubjectListComponent } from './subjects/subject-list/subject-list.component';
import { SubjectFormComponent } from './subjects/subject-form/subject-form.component';

import { AddGradeComponent } from './grades/add-grade/add-grade.component';

export const routes: Routes = [
  { path: 'hello', component: HelloworldComponent },
  { path: 'students', component: StudentsComponent },

  { path: 'my-grades',
    component: MyGradesComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_USER'] }
  },

  { path: 'grades',
    component: AllGradesComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },

  {
    path: 'add-grade',
    component: AddGradeComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_TEACHER'] }
  },


  { path: 'user',
    component: UserComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] }
  },

  { path: 'admin',
    component: AdminComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },

  {
    path: 'subjects',
    component: SubjectListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] },
    title: 'Subjects'
  },
  {
    path: 'admin/subjects',
    component: SubjectListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    title: 'Manage Subjects'
  },
  {
    path: 'admin/subjects/add',
    component: SubjectFormComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    title: 'Add Subject'
  },


  { path: 'auth/login', component: LoginComponent },

  // Go to login by default
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
];
