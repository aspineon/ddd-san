package io.dddbyexamples.delivery.planning.plan;

import io.dddbyexamples.delivery.planning.Amounts;
import io.dddbyexamples.delivery.planning.DeliveredAmountsChanged;
import io.dddbyexamples.delivery.planning.PlanningCompleted;
import io.dddbyexamples.demand.forecasting.DemandedLevelsChanged;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class PlanCompleteness {
    private final Amounts planned;
    private final Amounts demanded;
    private final Amounts yesterdaysReminder;

    public Amounts getDiff() {
        return planned.diff(demanded.sum(yesterdaysReminder));
    }

    public Amounts getPlanned() {
        return planned;
    }

    public boolean anyMissing() {
        return !getDiff().allMatch((productRefNo, amount) -> amount > 0);
    }
}
