package io.dddbyexamples.delivery.planning.plan;

import io.dddbyexamples.delivery.planning.Amounts;
import io.dddbyexamples.delivery.planning.DeliveredAmountsChanged;
import io.dddbyexamples.delivery.planning.PlanningCompleted;
import io.dddbyexamples.demand.forecasting.DemandedLevelsChanged;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class PlanCompletenessProjection {
    private LocalDate date;
    private Amounts planned;
    private Amounts demanded;
    private Amounts yesterdaysReminder;

    public void apply(DeliveredAmountsChanged event) {
        if (!event.getDate().equals(date)) {
            return;
        }
        planned = planned.sum(event.getDiff());
    }

    public void apply(DemandedLevelsChanged event) {
        if (!event.getDate().equals(date)) {
            return;
        }
        demanded = demanded.with(event.getRefNo(), event.getCurrent());
    }

    public void apply(PlanningCompleted event) {
        if (!event.getDate().equals(date.minusDays(1))) {
            return;
        }
        yesterdaysReminder = yesterdaysReminder.sum(event.getReminder());
    }

    public PlanCompleteness get() {
        return new PlanCompleteness(planned, demanded, yesterdaysReminder);
    }
}
