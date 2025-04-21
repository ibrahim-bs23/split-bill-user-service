package com.brainstation23.skeleton.data.repository.user;

import com.brainstation23.skeleton.core.domain.enums.ConnectionStatus;
import com.brainstation23.skeleton.data.entity.user.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Optional<Connection> findByUserNameAndConnectedUser(String userName, String connectedUser);

    List<Connection> findByConnectedUserAndConnectionStatus(String user, ConnectionStatus connectionStatus);

    Optional<Connection> findByUserNameAndConnectedUserAndConnectionStatus(String userName, String connectedUser, ConnectionStatus connectionStatus);

    Optional<Connection> findByUserNameAndConnectedUserAndConnectionStatusIn(String userName, String connectedUser, List<ConnectionStatus> connectionStatuses);

    List<Connection> findByUserNameOrConnectedUserAndConnectionStatus(String user, String connectedUser, ConnectionStatus connectionStatus);

}
