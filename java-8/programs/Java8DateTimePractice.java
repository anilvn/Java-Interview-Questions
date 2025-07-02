package javaBasics;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Java8DateTimePractice {

    public static void main(String[] args) {
        // ===================== 1. BASIC OBJECT CREATION =====================
        LocalDate today = LocalDate.now();  // current date
        LocalDate specificDate = LocalDate.of(2022, 12, 25); // custom date
        LocalDate parsedDate = LocalDate.parse("2022-12-25"); // from ISO string

        LocalDateTime currentDateTime = LocalDateTime.now(); // current date and time
        LocalDateTime specificDateTime = LocalDateTime.of(2022, 12, 25, 10, 30);

        Date legacyDate = new Date(); // java.util.Date

        System.out.println("Today: " + today); // Today: 2025-06-28
        System.out.println("Specific Date: " + specificDate); // Specific Date: 2022-12-25
        System.out.println("Parsed Date: " + parsedDate); // Parsed Date: 2022-12-25
        System.out.println("Current DateTime: " + currentDateTime); // Current DateTime: 2025-06-28T15:30:33.223583300
        System.out.println("Legacy Date: " + legacyDate); // Legacy Date: Sat Jun 28 15:30:33 IST 2025

        // ===================== 2. FORMATTER AND PARSER =====================
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // LocalDate ➡️ String
        String formatted = today.format(formatter);
        System.out.println("\nFormatted LocalDate: " + formatted); // Formatted LocalDate: 28-06-2025

        // String ➡️ LocalDate
        String inputDateStr = "25-12-2022";
        LocalDate parsedFromString = LocalDate.parse(inputDateStr, formatter);
        System.out.println("Parsed LocalDate: " + parsedFromString); // Parsed LocalDate: 2022-12-25

        // ===================== 3. LOCALDATETIME ↔️ LEGACY DATE =====================
        ZoneId zoneId = ZoneId.systemDefault();

        // LocalDateTime ➡️ Date
        Date dateFromLDT = Date.from(currentDateTime.atZone(zoneId).toInstant());
        System.out.println("\nDate from LocalDateTime: " + dateFromLDT); // Date from LocalDateTime: Sat Jun 28 15:30:33 IST 2025

        // Date ➡️ LocalDateTime
        LocalDateTime ldtFromDate = Instant.ofEpochMilli(legacyDate.getTime())
                                           .atZone(zoneId)
                                           .toLocalDateTime();
        System.out.println("LocalDateTime from Date: " + ldtFromDate); // LocalDateTime from Date: 2025-06-28T15:30:33.223

        // ===================== 4. COMMON METHODS =====================
        System.out.println("\nYear: " + today.getYear()); // Year: 2025
        System.out.println("Month: " + today.getMonth()); // Month: JUNE
        System.out.println("Day of Week: " + today.getDayOfWeek()); // Day of Week: SATURDAY
        System.out.println("First Day of Month: " + today.withDayOfMonth(1)); // First Day of Month: 2025-06-01
        System.out.println("Plus 10 Days: " + today.plusDays(10)); // Plus 10 Days: 2025-07-08
        System.out.println("Minus 2 Months: " + today.minusMonths(2)); // Minus 2 Months: 2025-04-28

        System.out.println("\nCurrent Time: " + currentDateTime.toLocalTime()); // Current Time: 15:30:33.223583300
        System.out.println("Plus 3 Hours: " + currentDateTime.plusHours(3)); // Plus 3 Hours: 2025-06-28T18:30:33.223583300

        // ===================== 5. COMPARISON =====================
        LocalDate d1 = LocalDate.of(2023, 1, 1);
        LocalDate d2 = LocalDate.of(2024, 1, 1);

        System.out.println("\nIs d1 before d2? " + d1.isBefore(d2)); // true
        System.out.println("Is d1 after d2? " + d1.isAfter(d2)); // false
        System.out.println("Is d1 equal to d2? " + d1.equals(d2)); // false

        // ===================== 6. DATE DIFFERENCE =====================

        // Using ChronoUnit
        long days = ChronoUnit.DAYS.between(d1, d2);
        long months = ChronoUnit.MONTHS.between(d1, d2);
        long years = ChronoUnit.YEARS.between(d1, d2);

        System.out.println("\nDays between: " + days); // Days between: 365
        System.out.println("Months between: " + months); // Months between: 12
        System.out.println("Years between: " + years); // Years between: 1

        // Using Period (breakdown)
        Period period = Period.between(d1, d2);
        System.out.println("Period: " + period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days"); 
        // Period: 1 years, 0 months, 0 days

        // ===================== 7. UTILITIES =====================
        System.out.println("\nIs leap year? " + today.isLeapYear()); // false
        System.out.println("Day of Year: " + today.getDayOfYear()); // Day of Year: 179
        System.out.println("Day of Month: " + today.getDayOfMonth()); // Day of Month: 28

        // ===================== 8. PRACTICE TASKS =====================
        // 1. Parse a date string "15/08/2023" into LocalDate, then format it to "yyyy.MM.dd"
        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate independenceDay = LocalDate.parse("15/08/2023", inputFmt);
        System.out.println("\nFormatted Independence Day: " + independenceDay.format(outputFmt)); // 2023.08.15

        // 2. Days between today and birthday
        LocalDate birthday = LocalDate.of(1990, 8, 15);
        long ageInDays = ChronoUnit.DAYS.between(birthday, today);
        System.out.println("Your age in days: " + ageInDays); // 12736

        // 3. Convert LocalDateTime to Date and back (already done above)

        // 4. Get first and last day of the month
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
        System.out.println("First day of this month: " + firstDay); // 2025-06-01
        System.out.println("Last day of this month: " + lastDay); // 2025-06-30

        // 5. Add 100 days and format nicely
        LocalDateTime after100Days = currentDateTime.plusDays(100);
        DateTimeFormatter niceFmt = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm");
        System.out.println("DateTime after 100 days: " + after100Days.format(niceFmt)); // Mon, 06 Oct 2025 15:30

        // ===================== 9. ZONED DATETIME =====================
        ZonedDateTime zdt = ZonedDateTime.now();
        ZonedDateTime usTime = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("\nLocal ZonedDateTime: " + zdt); // 2025-06-28T15:30:33.402594400+05:30[Asia/Calcutta]
        System.out.println("New York Time: " + usTime); // 2025-06-28T06:00:33.402594400-04:00[America/New_York]
    }
}

