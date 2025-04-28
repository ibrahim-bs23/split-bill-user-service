package com.brainstation23.skeleton.data.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseTransaction {
    private String from;
    private String to;
    private BigDecimal amount;
}
