import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService, UserProfile } from '../user/user.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './menu.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './menu.css'
})
export class Menu implements OnInit {
  userProfile: UserProfile | null = null;
  defaultAvatar: string = '/assets/user.svg'; 

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.userProfile = user;
      },
      error: (err) => {
        console.error('Не вдалося завантажити дані для меню:', err);
      }
    });
  }
}