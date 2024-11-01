package org.example.footballanalyzer.Data;

public enum Code {
    SUCCESS("Operacja zakończona sukcesem."),
    A1("Nie udało się zalogować."),
    A2("Nie ma takiego uzytkownika lub konto jest niekatywne."),
    A3("Podany token jest nieważny."),
    A4("Podany użytkownik już istnieje."),
    A5("Podane hasło jest nieprawidłowe."),
    A6("Niepoprawny email."),
    PERMIT("Przyznano dostep."),
    R1("Brak danej roli."),
    R2("Podana drużyna posiada już trenera.");
    public final String label;

    Code(String label) {
        this.label = label;
    }
}
