package com.brainstation23.skeleton.data.repository;

import com.brainstation23.skeleton.data.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByGroupId(String groupId);
    Optional<Group> findByName(String name);

    List<Group> findAllByGroupIdIn(List<String> groupIds);
}
