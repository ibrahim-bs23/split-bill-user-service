package com.brainstation23.skeleton.core.service.impl;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.enums.TransferType;
import com.brainstation23.skeleton.core.domain.exceptions.AlreadyExists;
import com.brainstation23.skeleton.core.domain.exceptions.InvalidRequestDataException;
import com.brainstation23.skeleton.core.domain.exceptions.RecordNotFoundException;
import com.brainstation23.skeleton.core.domain.exceptions.UnauthorizedResourceException;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.data.entity.user.UserLinkAccounts;
import com.brainstation23.skeleton.data.repository.user.UserLinkAccountsRepository;
import com.brainstation23.skeleton.presenter.domain.request.user.LinkRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.PreferableLinkAccountRequests;
import com.brainstation23.skeleton.presenter.domain.request.user.UpdateLinkRequest;
import com.brainstation23.skeleton.presenter.domain.response.user.LinkAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLinkAccountsService extends BaseService {
    private final UserLinkAccountsRepository userLinkAccountsRepository;

    public void createLinkAccounts(LinkRequest linkRequest) {
        validateLinkRequest(linkRequest);
        checkPriority(linkRequest.getPriority());
        UserLinkAccounts userLinkAccounts =createNewLinkAccounts(linkRequest);
        userLinkAccountsRepository.save(userLinkAccounts);
    }

   public void updateLinkAccountPriority(UpdateLinkRequest linkRequest) {
        checkValidUser(linkRequest.getId());
        UserLinkAccounts userLinkAccounts = validatePrevLinkAccount(linkRequest.getId());
        checkPriority(linkRequest.getPriority());
        userLinkAccounts.setPriority(linkRequest.getPriority());
        userLinkAccountsRepository.save(userLinkAccounts);
    }

    public String getPreferablePaymentType(PreferableLinkAccountRequests preferableLinkAccountRequests) {
        List<LinkAccountResponse> senderLinkAccounts=getUserLinkAccountsViaUserName(preferableLinkAccountRequests.getSenderUsername());
        List<LinkAccountResponse> receiverLinkAccounts=getUserLinkAccountsViaUserName(preferableLinkAccountRequests.getReceiverUsername());

        for (LinkAccountResponse senderLinkAccount : senderLinkAccounts) {
            for (LinkAccountResponse receiverLinkAccount : receiverLinkAccounts) {
                if (senderLinkAccount.getTransferType().equals(receiverLinkAccount.getTransferType())) {
                    return senderLinkAccount.getTransferType();
                }
            }
        }
        throw new UnauthorizedResourceException(ResponseMessage.INCOMPATIBLE_PAYMENT_TYPE);
    }

    public List<LinkAccountResponse> getUserLinkAccountsViaUserName(String userName) {
        return userLinkAccountsRepository.findAllByUserName(userName, Sort.by(Sort.Order.asc("priority")))
                .stream()
                .map(userLinkAccounts -> LinkAccountResponse.builder()
                        .id(userLinkAccounts.getId())
                        .fromAccount(userLinkAccounts.getFromAccount())
                        .priority(userLinkAccounts.getPriority())
                        .transferType(userLinkAccounts.getTransferType().name())
                        .build())
                .collect(Collectors.toList());
    }

   public List<LinkAccountResponse> getUserLinkAccounts() {
        return userLinkAccountsRepository.findAllByUserName(getUserName(), Sort.by(Sort.Order.asc("priority")))
                .stream()
                .map(userLinkAccounts -> LinkAccountResponse.builder()
                        .id(userLinkAccounts.getId())
                        .fromAccount(userLinkAccounts.getFromAccount())
                        .priority(userLinkAccounts.getPriority())
                        .transferType(userLinkAccounts.getTransferType().name())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteLinkAccounts(Long id) {
       checkValidUser(id);
       userLinkAccountsRepository.deleteById(id);
    }

    private void checkValidUser(Long id) {
        userLinkAccountsRepository.findById(id).ifPresentOrElse(userLinkAccounts -> {
            if (!userLinkAccounts.getUserName().equals(getUserName())) {
                throw new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS);
            }
        }, () -> {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        });
    }


    private UserLinkAccounts validatePrevLinkAccount(Long id) {
        return userLinkAccountsRepository.findById(id).orElseThrow(
                ()->new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND)
        );
    }

    private UserLinkAccounts createNewLinkAccounts(LinkRequest linkRequest) {
        return UserLinkAccounts.builder()
                .fromAccount(linkRequest.getFromAccount())
                .userIdentity(getUserIdentity())
                .userName(getUserName())
                .priority(linkRequest.getPriority())
                .transferType(TransferType.valueOf(linkRequest.getTransferType()))
                .build();
    }

    private void validateLinkRequest(LinkRequest linkRequest) {
        String userName = getCurrentUserContext().getUsername();
        TransferType transferType = TransferType.valueOf(linkRequest.getTransferType());
        Optional<UserLinkAccounts> byUserNameAndFromAccountAndTransferType = userLinkAccountsRepository.findByUserNameAndFromAccountAndTransferType(userName, linkRequest.getFromAccount(), transferType);
        if (byUserNameAndFromAccountAndTransferType.isPresent()) {
            throw new AlreadyExists(ResponseMessage.RECORD_ALREADY_EXIST);
        }
    }

    private void checkPriority(Long priority) {
        if(priority<=0)
            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);

        List<UserLinkAccounts> allByUserName = userLinkAccountsRepository.findAllByUserName(getUserName());
        for (UserLinkAccounts userLinkAccounts : allByUserName) {
            if (userLinkAccounts.getPriority().equals(priority)) {
                throw new AlreadyExists(ResponseMessage.PRIORITY_ALREADY_EXIST);
            }
        }
    }
}
