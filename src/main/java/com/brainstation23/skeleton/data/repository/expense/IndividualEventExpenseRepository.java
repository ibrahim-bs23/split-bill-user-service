package com.brainstation23.skeleton.data.repository.expense;

import com.brainstation23.skeleton.data.entity.expense.IndividualEventExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndividualEventExpenseRepository extends JpaRepository<IndividualEventExpense, Long> {
    // Add custom query methods if needed

    Optional<IndividualEventExpense> findByEventIdAndUserName(String eventId, String userName);
}
