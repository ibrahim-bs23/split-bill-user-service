package com.brainstation23.skeleton.data.repository;

import com.brainstation23.skeleton.data.entity.ExpenseSplit;
import com.brainstation23.skeleton.data.entity.ExpenseTransaction;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findAllByEventId(String eventId);

    Optional<ExpenseSplit> findByEventIdAndSenderUserNameAndReceiverUserName(String eventId, String senderUserName, String receiverUserName);

    List<ExpenseSplit> findAllBySenderUserIdentity(String userIdentity);

    List<ExpenseSplit> findAllByEventIdAndSenderUserIdentity(String eventId, String userIdentity);

    boolean existsByEventId(String eventId);
}
