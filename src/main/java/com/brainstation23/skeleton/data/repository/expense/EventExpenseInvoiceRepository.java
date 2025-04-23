package com.brainstation23.skeleton.data.repository.expense;

import com.brainstation23.skeleton.data.entity.expense.EventExpenseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventExpenseInvoiceRepository extends JpaRepository<EventExpenseInvoice, Long> {
    // Add custom query methods if needed
    List<EventExpenseInvoice> findAllByIndividualExpenseId(Long individualEventId);
}
