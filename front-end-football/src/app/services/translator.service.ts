import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TranslatorService {
  private countries: { [key: string]: string } = {
    Polska: 'Poland',
    Niemcy: 'Germany',
    Francja: 'France',
    Hiszpania: 'Spain',
    Włochy: 'Italy',
    'Wielka Brytania': 'United Kingdom',
    Czechy: 'Czech Republic',
    Słowacja: 'Slovakia',
    Węgry: 'Hungary',
    Rosja: 'Russia',
    Norwegia: 'Norway',
    Szwecja: 'Sweden',
    Finlandia: 'Finland',
    Grecja: 'Greece',
    Portugalia: 'Portugal',
    Holandia: 'Netherlands',
    Belgia: 'Belgium',
    Szwajcaria: 'Switzerland',
    Austria: 'Austria',
    Irlandia: 'Ireland',
    Dania: 'Denmark',

    Chiny: 'China',
    Japonia: 'Japan',
    Indie: 'India',
    'Korea Południowa': 'South Korea',
    Indonezja: 'Indonesia',
    'Arabia Saudyjska': 'Saudi Arabia',
    Turcja: 'Turkey',
    Izrael: 'Israel',
    Filipiny: 'Philippines',
    Malezja: 'Malaysia',
    Iran: 'Iran',
    Tajlandia: 'Thailand',
    Wietnam: 'Vietnam',

    'Stany Zjednoczone': 'United States',
    Kanada: 'Canada',
    Meksyk: 'Mexico',
    Brazylia: 'Brazil',
    Argentyna: 'Argentina',
    Kolumbia: 'Colombia',
    Peru: 'Peru',
    Wenezuela: 'Venezuela',
    Chile: 'Chile',
    Ekwador: 'Ecuador',

    Egipt: 'Egypt',
    Nigeria: 'Nigeria',
    'Południowa Afryka': 'South Africa',
    Maroko: 'Morocco',
    Etiopia: 'Ethiopia',
    Ghana: 'Ghana',
    Kenia: 'Kenya',
    Algieria: 'Algeria',
    Tanzania: 'Tanzania',
    Angola: 'Angola',
  };

  constructor() {}

  translateCountryName(name: string): string {
    return this.countries[name] || name;
  }
}
