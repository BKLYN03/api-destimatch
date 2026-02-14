package com.destimatch.common.utils;

import lombok.Getter;

@Getter
public enum BudgetLevel {

    ECO(1), // Petits budgets - backpacks, auberges, street food.
    MODERATE(2), // Budgets moyens - Hôtels standards, quelques restos
    HIGH(3), // Budget élevé - Beaux hôtels, confort, vols directs
    LUXURY(4); // Budget "de luxe" – Palaces, tout inclus, privé

    private final int value;

    BudgetLevel(int value) {
        this.value = value;
    }

    public static BudgetLevel fromCost(Double cost) {
        if (cost == null)
            return MODERATE;

        if (cost < 60.0)
            return ECO;
        if (cost < 160.0)
            return MODERATE;
        if (cost < 400.0)
            return HIGH;
        return LUXURY;
    }
}
