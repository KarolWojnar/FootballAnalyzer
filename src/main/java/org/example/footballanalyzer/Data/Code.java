package org.example.footballanalyzer.Data;

public enum Code {
    SUCCESS("Operacja zakończona sukcesem."),
    ERROR("Wystąpił błąd podczas wykonania operacji."),
    A1("Nie udało się zalogować."),
    A2("Nie ma takiego uzytkownika lub konto jest niekatywne."),
    A3("Podany token jest nieważny."),
    A4("Podany użytkownik już istnieje."),
    A5("Podane hasło jest nieprawidłowe."),
    A6("Niepoprawny email."),
    A7("Nie udało się aktywować konta Spróbuj ponowinie później."),
    A8("Nie udało się zresetować hasła."),
    A9("Podany użytkownik nie istnieje."),
    PERMIT("Przyznano dostep."),
    R1("Brak danej roli."),
    R2("Podana drużyna posiada już trenera."),
    R3("Brak zgłoszeń."),
    T1("Brak drużyny."),
    T2("Brak danych w tym okresie."),
    T3("Dana drużyna już posiada trenera głównego."),
    C1("Brak danych o danym państwie."),
    C2("Brak danych o danej lidze."),
    NF("Brak danego dokumentu.");
    public final String label;

    Code(String label) {
        this.label = label;
    }
}
