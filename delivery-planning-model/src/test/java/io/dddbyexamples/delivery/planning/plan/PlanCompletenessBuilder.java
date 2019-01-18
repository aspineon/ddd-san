package io.dddbyexamples.delivery.planning.plan;

import io.dddbyexamples.delivery.planning.Amounts;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PlanCompletenessBuilder implements PlanCompletenessProvider {

    private Map<LocalDate, Part> parts = new HashMap<>();
    private Map<LocalDate, PlanCompletenessProjection> completeness = new HashMap<>();

    public PlanCompleteness get(LocalDate date) {
        return getProjection(date).get();
    }

    public PlanCompletenessProjection getProjection(LocalDate date) {
        return completeness.computeIfAbsent(date, key -> {
            Part part = parts.getOrDefault(key, new Part());
            return part.create(key);
        });
    }

    public void demanded(LocalDate date, Amounts amounts) {
        parts.computeIfAbsent(date, key -> new Part()).setDemanded(amounts);
    }

    public void planned(LocalDate date, Amounts amounts) {
        parts.computeIfAbsent(date, key -> new Part()).setPlanned(amounts);
    }

    public void reminder(LocalDate date, Amounts amounts) {
        parts.computeIfAbsent(date, key -> new Part()).setReminder(amounts);
    }

    @Data
    private class Part {
        private Amounts planned = Amounts.empty();
        private Amounts demanded = Amounts.empty();
        private Amounts reminder = Amounts.empty();

        private PlanCompletenessProjection create(LocalDate key) {
            return new PlanCompletenessProjection(
                    key,
                    planned,
                    demanded,
                    reminder
            );
        }
    }
}
