export function validatePassword(password: string): string | null {
  if (!password) {
    return 'Введіть пароль.';
  }
  if (password.length < 12) {
    return 'Пароль має містити щонайменше 12 символів.';
  }
  if (!/[!@#$%^&*(),.?":{}|<>_\-+=[\]\\/'`~;]/.test(password)) {
    return 'Пароль має містити хоча б один спеціальний символ.';
  }
  return null;
}
