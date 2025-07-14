import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Subject } from '../models/subject';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  private API_URL = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient) { }

  getSubjects(): Observable<Subject[]> {
    // Get data and ensure it contains all the data about teacher
    return this.http.get<Subject[]>(this.API_URL).pipe(
      map(subjects => {
        console.log('Original subjects from API:', subjects);
        return subjects;
      })
    );
  }

  getSubjectById(id: number): Observable<Subject> {
    return this.http.get<Subject>(`${this.API_URL}/${id}`);
  }

  createSubject(subject: Subject): Observable<Subject> {
    return this.http.post<Subject>(this.API_URL, subject);
  }

  updateSubject(id: number, subject: Subject): Observable<Subject> {
    return this.http.put<Subject>(`${this.API_URL}/${id}`, subject);
  }

  deleteSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
