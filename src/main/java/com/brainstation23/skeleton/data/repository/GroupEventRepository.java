package com.brainstation23.skeleton.data.repository;

import com.brainstation23.skeleton.data.entity.GroupEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupEventRepository extends JpaRepository<GroupEvent, Long> {

    List<GroupEvent> findByGroupId(String groupId);

    List<GroupEvent> findByUserIdentity(String userIdentity);

    Optional<GroupEvent> findFirstByEventId(String eventId);
}
