import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contacts',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './contacts.html',
  styleUrl: './contacts.css'
})
export class Contacts {
  name = '';
  email = '';
  message = '';
  sent = false;

  sendMessage(): void {
    if (!this.name.trim() || !this.email.trim() || !this.message.trim()) {
      alert('Заповніть усі поля форми.');
      return;
    }
    this.sent = true;
    this.name = '';
    this.email = '';
    this.message = '';
  }
}
