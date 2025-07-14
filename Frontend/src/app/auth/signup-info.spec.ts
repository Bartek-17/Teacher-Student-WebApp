import { SignupInfo } from './signup-info';

describe('SignupInfo', () => {
  it('should create an instance', () => {
    const signupInfo = new SignupInfo('testUser', 'testPassword');
    expect(signupInfo).toBeTruthy();
  });
});
