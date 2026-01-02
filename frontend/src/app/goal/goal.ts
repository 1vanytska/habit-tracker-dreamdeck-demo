import { Component, OnInit } from '@angular/core';

interface CalendarDay {
  dayNumber: number | null;
  isToday: boolean;
  isWeekend: boolean;
}

@Component({
  selector: 'app-goal',
  imports: [],
  templateUrl: './goal.html',
  styleUrl: './goal.css'
})

export class Goal implements OnInit {
  currentDate: Date = new Date();
  displayDate: Date = new Date();
  
  calendarDays: CalendarDay[] = [];
  currentMonthName: string = '';

  ngOnInit(): void {
    this.generateCalendar();
  }

  generateCalendar(): void {
    const year = this.displayDate.getFullYear();
    const month = this.displayDate.getMonth();

    this.currentMonthName = new Intl.DateTimeFormat('uk-UA', { month: 'long', year: 'numeric' }).format(this.displayDate);
    this.currentMonthName = this.currentMonthName.charAt(0).toUpperCase() + this.currentMonthName.slice(1);

    const firstDay = new Date(year, month, 1);
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    let startDayOfWeek = firstDay.getDay() - 1; 
    if (startDayOfWeek === -1) startDayOfWeek = 6;

    this.calendarDays = [];

    for (let i = 0; i < startDayOfWeek; i++) {
      this.calendarDays.push({ dayNumber: null, isToday: false, isWeekend: false });
    }

    const today = new Date();
    for (let i = 1; i <= daysInMonth; i++) {
      const isToday = 
        i === today.getDate() && 
        month === today.getMonth() && 
        year === today.getFullYear();
        
      const currentDayOfWeek = new Date(year, month, i).getDay();
      const isWeekend = currentDayOfWeek === 0 || currentDayOfWeek === 6;

      this.calendarDays.push({ 
        dayNumber: i, 
        isToday: isToday,
        isWeekend: isWeekend
      });
    }
  }

  changeMonth(offset: number): void {
    const newDate = new Date(this.displayDate);
    newDate.setMonth(newDate.getMonth() + offset);
    this.displayDate = newDate;
    this.generateCalendar();
  }
}
