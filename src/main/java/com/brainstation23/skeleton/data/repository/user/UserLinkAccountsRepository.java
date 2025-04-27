package com.brainstation23.skeleton.data.repository.user;

import com.brainstation23.skeleton.core.domain.enums.TransferType;
import com.brainstation23.skeleton.data.entity.user.UserLinkAccounts;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLinkAccountsRepository extends JpaRepository<UserLinkAccounts, Long> {
    Optional<UserLinkAccounts> findByUserName(String userName);

    Optional<UserLinkAccounts> findByUserNameAndFromAccountAndTransferType(String userName, String fromAccount, TransferType transferType);

    List<UserLinkAccounts> findAllByUserName(String userName);

    List<UserLinkAccounts> findAllByUserName(String userName, Sort sort);

}
