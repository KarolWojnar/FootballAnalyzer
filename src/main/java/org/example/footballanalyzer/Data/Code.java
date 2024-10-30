package org.example.footballanalyzer.Data;

public enum Code {
    SUCCESS("Operacja zakończona sukcesem."),
    A1("Nie udało się zalogować."),
    A2("Nie ma takiego uzytkownika."),
    A3("Podany token jest nieważny."),
    PERMIT("Przyznano dostep.");
    public final String label;

    Code(String label) {
        this.label = label;
    }
}
