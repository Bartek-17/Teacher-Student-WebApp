import { Student } from './student';

describe('Student', () => {
  it('should create an instance', () => {
    expect(new Student('John', 'Doe', 'john.doe@example.com', '123-456-7890')).toBeTruthy();
  });
});
