package org.example.footballanalyzer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.footballanalyzer.Data.Dto.GroupRecord;
import org.example.footballanalyzer.Data.Dto.RatingRecord;
import org.example.footballanalyzer.Data.Entity.FixtureStatsTeam;
import org.example.footballanalyzer.Data.Entity.FixturesStats;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {

    public double[] calculateWeights(double[] normalizedSums) {
        double[] weights = new double[normalizedSums.length + 1];
        for (int i = 0; i < normalizedSums.length; i++) {
            weights[i] = 90 / ((normalizedSums[i] < 0.000001 ? 0.0001 : normalizedSums[i]));
        }

        return weights;
    }

    public void normalizeSums(Map<String, Double> sumValues, int fixturesCount) {
        sumValues.replaceAll((k, v) -> v / fixturesCount);
    }

    public void updateSumValues(Map<String, Double> sumValues, FixtureStatsTeam fixture) {
        sumValues.put("sumOffsides", sumValues.get("sumOffsides") + fixture.getOffsides());
        sumValues.put("sumShotsTotal", sumValues.get("sumShotsTotal") + fixture.getShotsTotal());
        sumValues.put("sumShootsOnGoal", sumValues.get("sumShootsOnGoal") + fixture.getShotsOnGoal());
        sumValues.put("sumGoals", sumValues.get("sumGoals") + fixture.getGoalsTotal());
        sumValues.put("sumGoalsConceded", sumValues.get("sumGoalsConceded") + fixture.getGoalsConceded());
        sumValues.put("sumAssists", sumValues.get("sumAssists") + fixture.getAssists());
        sumValues.put("sumSaves", sumValues.get("sumSaves") + fixture.getSaves());
        sumValues.put("sumPasses", sumValues.get("sumPasses") + fixture.getPassesTotal());
        sumValues.put("sumKeyPasses", sumValues.get("sumKeyPasses") + fixture.getPassesKey());
        sumValues.put("sumAccuratePasses", sumValues.get("sumAccuratePasses") + fixture.getPassesAccuracy());
        sumValues.put("sumTackles", sumValues.get("sumTackles") + fixture.getTacklesTotal());
        sumValues.put("sumTacklesBlocks", sumValues.get("sumTacklesBlocks") + fixture.getTacklesBlocks());
        sumValues.put("sumTacklesInterceptions", sumValues.get("sumTacklesInterceptions") + fixture.getTacklesInterceptions());
        sumValues.put("sumDuelsTotal", sumValues.get("sumDuelsTotal") + fixture.getDuelsTotal());
        sumValues.put("sumDuelsWon", sumValues.get("sumDuelsWon") + fixture.getDuelsWon());
        sumValues.put("sumDribblesAttempts", sumValues.get("sumDribblesAttempts") + fixture.getDribblesAttempts());
        sumValues.put("sumDribblesSuccess", sumValues.get("sumDribblesSuccess") + fixture.getDribblesSuccess());
        sumValues.put("sumFoulsDrawn", sumValues.get("sumFoulsDrawn") + fixture.getFoulsDrawn());
        sumValues.put("sumFoulsCommited", sumValues.get("sumFoulsCommited") + fixture.getFoulsCommitted());
        sumValues.put("sumCardYellow", sumValues.get("sumCardYellow") + fixture.getCardsYellow());
        sumValues.put("sumCardRed", sumValues.get("sumCardRed") + fixture.getCardsRed());
    }

    public void updateMaxValues(Map<String, Double> maxValues, FixtureStatsTeam fixture) {
        maxValues.put("maxOffsides", Math.max(maxValues.get("maxOffsides"), fixture.getOffsides()));
        maxValues.put("maxShotsTotal", Math.max(maxValues.get("maxShotsTotal"), fixture.getShotsTotal()));
        maxValues.put("maxShootsOnGoal", Math.max(maxValues.get("maxShootsOnGoal"), fixture.getShotsOnGoal()));
        maxValues.put("maxGoals", Math.max(maxValues.get("maxGoals"), fixture.getGoalsTotal()));
        maxValues.put("maxGoalsConceded", Math.max(maxValues.get("maxGoalsConceded"), fixture.getGoalsConceded()));
        maxValues.put("maxAssists", Math.max(maxValues.get("maxAssists"), fixture.getAssists()));
        maxValues.put("maxSaves", Math.max(maxValues.get("maxSaves"), fixture.getSaves()));
        maxValues.put("maxPasses", Math.max(maxValues.get("maxPasses"), fixture.getPassesTotal()));
        maxValues.put("maxKeyPasses", Math.max(maxValues.get("maxKeyPasses"), fixture.getPassesKey()));
        maxValues.put("maxAccuratePasses", Math.max(maxValues.get("maxAccuratePasses"), fixture.getPassesAccuracy()));
        maxValues.put("maxTackles", Math.max(maxValues.get("maxTackles"), fixture.getTacklesTotal()));
        maxValues.put("maxTacklesBlocks", Math.max(maxValues.get("maxTacklesBlocks"), fixture.getTacklesBlocks()));
        maxValues.put("maxTacklesInterceptions", Math.max(maxValues.get("maxTacklesInterceptions"), fixture.getTacklesInterceptions()));
        maxValues.put("maxDuelsTotal", Math.max(maxValues.get("maxDuelsTotal"), fixture.getDuelsTotal()));
        maxValues.put("maxDuelsWon", Math.max(maxValues.get("maxDuelsWon"), fixture.getDuelsWon()));
        maxValues.put("maxDribblesAttempts", Math.max(maxValues.get("maxDribblesAttempts"), fixture.getDribblesAttempts()));
        maxValues.put("maxDribblesSuccess", Math.max(maxValues.get("maxDribblesSuccess"), fixture.getDribblesSuccess()));
        maxValues.put("maxFoulsDrawn", Math.max(maxValues.get("maxFoulsDrawn"), fixture.getFoulsDrawn()));
        maxValues.put("maxFoulsCommited", Math.max(maxValues.get("maxFoulsCommited"), fixture.getFoulsCommitted()));
        maxValues.put("maxCardYellow", Math.max(maxValues.get("maxCardYellow"), fixture.getCardsYellow()));
        maxValues.put("maxCardRed", Math.max(maxValues.get("maxCardRed"), fixture.getCardsRed()));
    }

    public void updateSumValuesPlayers(Map<String, Double> sumValues, FixturesStats fixture) {
        sumValues.put("sumOffsides", sumValues.get("sumOffsides") + fixture.getOffsides());
        sumValues.put("sumShotsTotal", sumValues.get("sumShotsTotal") + fixture.getShotsTotal());
        sumValues.put("sumShootsOnGoal", sumValues.get("sumShootsOnGoal") + fixture.getShotsOnGoal());
        sumValues.put("sumGoals", sumValues.get("sumGoals") + fixture.getGoalsTotal());
        sumValues.put("sumGoalsConceded", sumValues.get("sumGoalsConceded") + fixture.getGoalsConceded());
        sumValues.put("sumAssists", sumValues.get("sumAssists") + fixture.getAssists());
        sumValues.put("sumSaves", sumValues.get("sumSaves") + fixture.getSaves());
        sumValues.put("sumPasses", sumValues.get("sumPasses") + fixture.getPassesTotal());
        sumValues.put("sumKeyPasses", sumValues.get("sumKeyPasses") + fixture.getPassesKey());
        sumValues.put("sumAccuratePasses", sumValues.get("sumAccuratePasses") + fixture.getPassesAccuracy());
        sumValues.put("sumTackles", sumValues.get("sumTackles") + fixture.getTacklesTotal());
        sumValues.put("sumTacklesBlocks", sumValues.get("sumTacklesBlocks") + fixture.getTacklesBlocks());
        sumValues.put("sumTacklesInterceptions", sumValues.get("sumTacklesInterceptions") + fixture.getTacklesInterceptions());
        sumValues.put("sumDuelsTotal", sumValues.get("sumDuelsTotal") + fixture.getDuelsTotal());
        sumValues.put("sumDuelsWon", sumValues.get("sumDuelsWon") + fixture.getDuelsWon());
        sumValues.put("sumDribblesAttempts", sumValues.get("sumDribblesAttempts") + fixture.getDribblesAttempts());
        sumValues.put("sumDribblesSuccess", sumValues.get("sumDribblesSuccess") + fixture.getDribblesSuccess());
        sumValues.put("sumFoulsDrawn", sumValues.get("sumFoulsDrawn") + fixture.getFoulsDrawn());
        sumValues.put("sumFoulsCommited", sumValues.get("sumFoulsCommited") + fixture.getFoulsCommitted());
        sumValues.put("sumCardYellow", sumValues.get("sumCardYellow") + fixture.getCardsYellow());
        sumValues.put("sumCardRed", sumValues.get("sumCardRed") + fixture.getCardsRed());
    }

    public void updateMaxValuesPlayers(Map<String, Double> maxValues, FixturesStats fixture) {
        maxValues.put("maxOffsides", Math.max(maxValues.get("maxOffsides"), fixture.getOffsides()));
        maxValues.put("maxShotsTotal", Math.max(maxValues.get("maxShotsTotal"), fixture.getShotsTotal()));
        maxValues.put("maxShootsOnGoal", Math.max(maxValues.get("maxShootsOnGoal"), fixture.getShotsOnGoal()));
        maxValues.put("maxGoals", Math.max(maxValues.get("maxGoals"), fixture.getGoalsTotal()));
        maxValues.put("maxGoalsConceded", Math.max(maxValues.get("maxGoalsConceded"), fixture.getGoalsConceded()));
        maxValues.put("maxAssists", Math.max(maxValues.get("maxAssists"), fixture.getAssists()));
        maxValues.put("maxSaves", Math.max(maxValues.get("maxSaves"), fixture.getSaves()));
        maxValues.put("maxPasses", Math.max(maxValues.get("maxPasses"), fixture.getPassesTotal()));
        maxValues.put("maxKeyPasses", Math.max(maxValues.get("maxKeyPasses"), fixture.getPassesKey()));
        maxValues.put("maxAccuratePasses", Math.max(maxValues.get("maxAccuratePasses"), fixture.getPassesAccuracy()));
        maxValues.put("maxTackles", Math.max(maxValues.get("maxTackles"), fixture.getTacklesTotal()));
        maxValues.put("maxTacklesBlocks", Math.max(maxValues.get("maxTacklesBlocks"), fixture.getTacklesBlocks()));
        maxValues.put("maxTacklesInterceptions", Math.max(maxValues.get("maxTacklesInterceptions"), fixture.getTacklesInterceptions()));
        maxValues.put("maxDuelsTotal", Math.max(maxValues.get("maxDuelsTotal"), fixture.getDuelsTotal()));
        maxValues.put("maxDuelsWon", Math.max(maxValues.get("maxDuelsWon"), fixture.getDuelsWon()));
        maxValues.put("maxDribblesAttempts", Math.max(maxValues.get("maxDribblesAttempts"), fixture.getDribblesAttempts()));
        maxValues.put("maxDribblesSuccess", Math.max(maxValues.get("maxDribblesSuccess"), fixture.getDribblesSuccess()));
        maxValues.put("maxFoulsDrawn", Math.max(maxValues.get("maxFoulsDrawn"), fixture.getFoulsDrawn()));
        maxValues.put("maxFoulsCommited", Math.max(maxValues.get("maxFoulsCommited"), fixture.getFoulsCommitted()));
        maxValues.put("maxCardYellow", Math.max(maxValues.get("maxCardYellow"), fixture.getCardsYellow()));
        maxValues.put("maxCardRed", Math.max(maxValues.get("maxCardRed"), fixture.getCardsRed()));
    }


    public Map<String, Double> initializeSumValues() {
        Map<String, Double> sumValues = new HashMap<>();
        sumValues.put("sumOffsides", 0.0);
        sumValues.put("sumShotsTotal", 0.0);
        sumValues.put("sumShootsOnGoal", 0.0);
        sumValues.put("sumGoals", 0.0);
        sumValues.put("sumGoalsConceded", 0.0);
        sumValues.put("sumAssists", 0.0);
        sumValues.put("sumSaves", 0.0);
        sumValues.put("sumPasses", 0.0);
        sumValues.put("sumKeyPasses", 0.0);
        sumValues.put("sumAccuratePasses", 0.0);
        sumValues.put("sumTackles", 0.0);
        sumValues.put("sumTacklesBlocks", 0.0);
        sumValues.put("sumTacklesInterceptions", 0.0);
        sumValues.put("sumDuelsTotal", 0.0);
        sumValues.put("sumDuelsWon", 0.0);
        sumValues.put("sumDribblesAttempts", 0.0);
        sumValues.put("sumDribblesSuccess", 0.0);
        sumValues.put("sumFoulsDrawn", 0.0);
        sumValues.put("sumFoulsCommited", 0.0);
        sumValues.put("sumCardYellow", 0.0);
        sumValues.put("sumCardRed", 0.0);
        return sumValues;
    }

    public Map<String, Double> initializeMaxValues() {
        Map<String, Double> maxValues = new HashMap<>();
        maxValues.put("maxOffsides", 0.001);
        maxValues.put("maxShotsTotal", 0.001);
        maxValues.put("maxShootsOnGoal", 0.001);
        maxValues.put("maxGoals", 0.001);
        maxValues.put("maxGoalsConceded", 0.001);
        maxValues.put("maxAssists", 0.001);
        maxValues.put("maxSaves", 0.001);
        maxValues.put("maxPasses", 0.001);
        maxValues.put("maxKeyPasses", 0.001);
        maxValues.put("maxAccuratePasses", 0.001);
        maxValues.put("maxTackles", 0.001);
        maxValues.put("maxTacklesBlocks", 0.001);
        maxValues.put("maxTacklesInterceptions", 0.001);
        maxValues.put("maxDuelsTotal", 0.001);
        maxValues.put("maxDuelsWon", 0.001);
        maxValues.put("maxDribblesAttempts", 0.001);
        maxValues.put("maxDribblesSuccess", 0.001);
        maxValues.put("maxFoulsDrawn", 0.001);
        maxValues.put("maxFoulsCommited", 0.001);
        maxValues.put("maxCardYellow", 0.001);
        maxValues.put("maxCardRed", 0.001);
        return maxValues;
    }

    public double setAttacking(FixtureStatsTeam teamStats, Map<String, Double> maxValues, double[] weights) {
        double shootsEffective = teamStats.getShotsOnGoal() / (maxValues.get("maxShootsOnGoal"));
        double dribblesEffective = teamStats.getDribblesSuccess() / (maxValues.get("maxDribblesSuccess"));
        double goals = teamStats.getGoalsTotal() / maxValues.get("maxGoals");

        return (shootsEffective * weights[2] + dribblesEffective * weights[16] + goals * weights[3])
                / (weights[2] + weights[16] + weights[3]);
    }

    public double setDefending(FixtureStatsTeam teamStats, Map<String, Double> maxValues, double[] weights) {
        double duelsWon = teamStats.getDuelsWon() / maxValues.get("maxDuelsWon");
        double tacklesInterp = teamStats.getTacklesInterceptions() / maxValues.get("maxTacklesInterceptions");
        double tacklesBlocks = teamStats.getTacklesBlocks() / maxValues.get("maxTacklesBlocks");
        double foulsDrawn = teamStats.getFoulsDrawn() / maxValues.get("maxFoulsDrawn");

        return (duelsWon * weights[14] + tacklesInterp * weights[12] + tacklesBlocks * weights[11] + foulsDrawn * weights[17])
                / (weights[14] + weights[12] + weights[11] + weights[17]);

    }

    public double setAgression(FixtureStatsTeam teamStats, Map<String, Double> maxValues, double[] weights) {
        double foulsCommitted = (teamStats.getFoulsCommitted() / maxValues.get("maxFoulsCommited"));
        double normRedCards = teamStats.getCardsRed() / maxValues.get("maxCardRed");
        double normYellowCards = teamStats.getCardsYellow() / maxValues.get("maxCardYellow");

        return (foulsCommitted * weights[18] + normRedCards * weights[20] + normYellowCards * weights[19])
                / (weights[18] + weights[20] + weights[19]);
    }

    public double setCreativity(FixtureStatsTeam teamStats, Map<String, Double> maxValues, double[] weights) {
        double pass = teamStats.getPassesAccuracy() / maxValues.get("maxAccuratePasses");
        double assists = teamStats.getAssists() / maxValues.get("maxAssists");
        double keyPass = teamStats.getPassesKey() / maxValues.get("maxKeyPasses");

        return (pass * weights[9] + assists * weights[5] + keyPass * weights[8])
                / (weights[9] + weights[5] + weights[8]);
    }

    public double setAttackingPlayers(FixturesStats teamStats, Map<String, Double> maxValues, double[] weights) {
        double shootsEffective = teamStats.getShotsOnGoal() / (maxValues.get("maxShootsOnGoal"));
        double dribblesEffective = teamStats.getDribblesSuccess() / (maxValues.get("maxDribblesSuccess"));
        double goals = teamStats.getGoalsTotal() / maxValues.get("maxGoals");

        return (shootsEffective * weights[2] + dribblesEffective * weights[16] + goals * weights[3])
                / (weights[2] + weights[16] + weights[3]);
    }

    public double setDefendingPlayers(FixturesStats teamStats, Map<String, Double> maxValues, double[] weights) {
        double duelsWon = teamStats.getDuelsWon() / maxValues.get("maxDuelsWon");
        double tacklesInterp = teamStats.getTacklesInterceptions() / maxValues.get("maxTacklesInterceptions");
        double tacklesBlocks = teamStats.getTacklesBlocks() / maxValues.get("maxTacklesBlocks");
        double foulsDrawn = teamStats.getFoulsDrawn() / maxValues.get("maxFoulsDrawn");

        return (duelsWon * weights[14] + tacklesInterp * weights[12] + tacklesBlocks * weights[11] + foulsDrawn * weights[17])
                / (weights[14] + weights[12] + weights[11] + weights[17]);

    }

    public double setAgressionPlayers(FixturesStats teamStats, Map<String, Double> maxValues, double[] weights) {
        double foulsCommitted = (teamStats.getFoulsCommitted() / maxValues.get("maxFoulsCommited"));
        double normRedCards = teamStats.getCardsRed() / maxValues.get("maxCardRed");
        double normYellowCards = teamStats.getCardsYellow() / maxValues.get("maxCardYellow");

        return (foulsCommitted * weights[18] + normRedCards * weights[20] + normYellowCards * weights[19])
                / (weights[18] + weights[20] + weights[19]);
    }

    public double setCreativityPlayers(FixturesStats teamStats, Map<String, Double> maxValues, double[] weights) {
        double pass = teamStats.getPassesAccuracy() / maxValues.get("maxAccuratePasses");
        double assists = teamStats.getAssists() / maxValues.get("maxAssists");
        double keyPass = teamStats.getPassesKey() / maxValues.get("maxKeyPasses");

        return (pass * weights[9] + assists * weights[5] + keyPass * weights[8])
                / (weights[9] + weights[5] + weights[8]);
    }

    public Map<String, ?> getAvgOfList(String responseName, List<GroupRecord> groupedStats, String name) {
        int i = 0;
        double aggression = 0.0, attacking = 0.0, defending = 0.0, creativity = 0.0;
        for (GroupRecord record : groupedStats) {
            aggression += record.aggression();
            attacking += record.attacking();
            defending += record.defending();
            creativity += record.creativity();
            i++;
        }

        GroupRecord record = new GroupRecord(
                0, name, null, attacking / i, defending / i, aggression / i, creativity / i
        );

        return Map.of(responseName, record);

    }

    public Map<String, RatingRecord> getAvgByDates(String label, List<GroupRecord> groupedStats, String rounding, LocalDate startDate, LocalDate endDate, String name, Map<String, Double> averageForPeriod) {
        Map<String, RatingRecord> result = new HashMap<>();
        Map<String, Double> periodRatings = new HashMap<>();

        int intervalDays = 0;
        if ("week".equals(rounding)) {
            intervalDays = 7;
        } else if ("month".equals(rounding)) {
            intervalDays = 30;
        }
        if (intervalDays == 0 || ChronoUnit.DAYS.between(startDate, endDate) <= intervalDays) {
            periodRatings.put(startDate.toString(), teamRating(groupedStats, averageForPeriod));
        } else {
            LocalDate currentStart = startDate;
            while (!currentStart.isAfter(endDate)) {
                LocalDate finalCurrentStart = currentStart;
                LocalDate currentEnd = currentStart.plusDays(intervalDays - 1);
                LocalDate finalCurrentEnd;
                if (currentEnd.isAfter(endDate)) {
                    currentEnd = endDate;
                    finalCurrentEnd = currentEnd;
                } else {
                    finalCurrentEnd = currentEnd;
                }
                List<GroupRecord> filteredStats = groupedStats.stream()
                        .filter(record -> {
                            LocalDate recordDate = record.date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return !recordDate.isBefore(finalCurrentStart) && !recordDate.isAfter(finalCurrentEnd);
                        })
                        .toList();
                periodRatings.put(currentStart.toString(), teamRating(filteredStats, averageForPeriod));
                currentStart = currentStart.plusDays(intervalDays);
            }
        }

        RatingRecord ratingRecord = new RatingRecord(name, periodRatings);
        result.put(label, ratingRecord);
        return result;
    }

    public Map<String, Double> calculateAverageForPeriod(List<GroupRecord> groupedStats, LocalDate start, LocalDate end) {
        List<GroupRecord> filteredStats = groupedStats.stream()
                .filter(record -> {
                    LocalDate recordDate = record.date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !recordDate.isBefore(start) && !recordDate.isAfter(end);
                })
                .toList();

        return sumRatings(filteredStats);
    }

    private Map<String, Double> sumRatings(List<GroupRecord> filteredStats) {
        var aggressionAvg = filteredStats.stream().mapToDouble(GroupRecord::aggression).sum() / filteredStats.size();
        var attackingAvg = filteredStats.stream().mapToDouble(GroupRecord::attacking).sum() / filteredStats.size();
        var defendingAvg = filteredStats.stream().mapToDouble(GroupRecord::defending).sum() / filteredStats.size();
        var creativityAvg = filteredStats.stream().mapToDouble(GroupRecord::creativity).sum() / filteredStats.size();

        Map<String, Double> sumValues = new HashMap<>();

        sumValues.put("aggression", aggressionAvg);
        sumValues.put("attacking", attackingAvg);
        sumValues.put("defending", defendingAvg);
        sumValues.put("creativity", creativityAvg);

        return sumValues;
    }

    private Double teamRating(List<GroupRecord> filteredStats, Map<String, Double> sumValues) {

        double rating = 0.0;

        rating += filteredStats.stream().mapToDouble(GroupRecord::attacking).sum() * sumValues.get("attacking");
        rating += filteredStats.stream().mapToDouble(GroupRecord::defending).sum() * sumValues.get("defending");
        rating += filteredStats.stream().mapToDouble(GroupRecord::creativity).sum() * sumValues.get("creativity");
        rating -= filteredStats.stream().mapToDouble(GroupRecord::aggression).sum() * sumValues.get("aggression");

        double weights = sumValues.get("attacking") + sumValues.get("defending") + sumValues.get("creativity") - sumValues.get("aggression");

        rating /= weights;
        rating /=  filteredStats.size();
        rating = Double.isNaN(rating) ? 0.0 : rating;
        rating = Math.round(rating * 100000.0) / 1000.0;

        return rating;
    }
}
