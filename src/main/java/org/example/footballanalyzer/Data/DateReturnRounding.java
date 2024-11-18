package org.example.footballanalyzer.Data;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DateReturnRounding {
    LocalDate startDate;
    LocalDate endDate;
    String rounding;
    boolean compareToAll;
}
