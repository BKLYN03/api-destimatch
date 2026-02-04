package com.destimatch.common.utils;

import lombok.Getter;
import lombok.Setter;

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
}
