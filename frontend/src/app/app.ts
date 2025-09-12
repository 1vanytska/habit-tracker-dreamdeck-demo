import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menu } from "./menu/menu";
import { Footer } from "./footer/footer";
import { Header } from "./header/header";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Menu, Footer, Header],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'frontend';
}
