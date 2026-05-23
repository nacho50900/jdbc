package uo.ri.cws.application.service.contract.crud.commands;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Computes the termination settlement for a contract.
 *
 * Settlement = avgDailyGross * compensationDaysPerYear * fullYears
 *
 * The mechanic is only entitled to settlement when at least 365 days
 * have elapsed between startDate and endDate.
 */
final class SettlementCalculator {

    private SettlementCalculator() { }

    /**
     * @param startDate           contract start date
     * @param endDate             contract end date (last day of current month)
     * @param lastGrossSalaries   gross salaries of the last 12 payrolls
     * @param compensationDays    days per year from the contract type
     * @return settlement amount, 0.0 if less than 365 days elapsed
     */
    static double calculate(
            LocalDate startDate,
            LocalDate endDate,
            List<Double> lastGrossSalaries,
            double compensationDays) {

        long daysElapsed = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysElapsed < 365) {
            return 0.0;
        }

        long fullYears = ChronoUnit.YEARS.between(startDate, endDate);
        if (fullYears == 0) {
            return 0.0;
        }

        double sumGross = 0.0;
        for (double g : lastGrossSalaries) {
            sumGross += g;
        }
        double avgDailyGross = sumGross / 365.0;

        return avgDailyGross * compensationDays * fullYears;
    }

}
