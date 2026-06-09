import { validatePassword } from './password.utils';

describe('validatePassword', () => {
  it('should reject empty password', () => {
    expect(validatePassword('')).toBe('Введіть пароль.');
  });

  it('should reject short password', () => {
    expect(validatePassword('short1!')).toBe('Пароль має містити щонайменше 12 символів.');
  });

  it('should reject password without special character', () => {
    expect(validatePassword('verylongpassword')).toBe('Пароль має містити хоча б один спеціальний символ.');
  });

  it('should accept valid password', () => {
    expect(validatePassword('verylongpass!')).toBeNull();
  });
});
