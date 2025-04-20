package com.brainstation23.skeleton.data.repository;

import com.brainstation23.skeleton.data.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroupId(String groupId);
    List<GroupMember> findByUserIdentity(UUID userIdentity);
    void deleteByGroupIdAndUserIdentity(String groupId, UUID userIdentity);
    long countByGroupIdAndRole(String groupId, String role);
}
