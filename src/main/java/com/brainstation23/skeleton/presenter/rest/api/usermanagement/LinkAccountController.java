package com.brainstation23.skeleton.presenter.rest.api.usermanagement;

import com.brainstation23.skeleton.common.utils.AppUtils;
import com.brainstation23.skeleton.common.utils.ResponseUtils;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.model.ApiResponse;
import com.brainstation23.skeleton.core.service.BaseService;
import com.brainstation23.skeleton.core.service.impl.UserLinkAccountsService;
import com.brainstation23.skeleton.presenter.domain.request.user.LinkRequest;
import com.brainstation23.skeleton.presenter.domain.request.user.UpdateLinkRequest;
import com.brainstation23.skeleton.presenter.domain.response.user.LinkAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL+"/link-account")
public class LinkAccountController extends BaseService {

    private final UserLinkAccountsService userLinkAccountsService;

    @PostMapping("/create")
    public ApiResponse<?> createLinkAccounts(@RequestBody LinkRequest linkRequest) {
        userLinkAccountsService.createLinkAccounts(linkRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL));
    }

    @PutMapping("/update-priority")
    public ApiResponse<?> updateLinkAccountPriority(@RequestBody UpdateLinkRequest linkRequest) {
        userLinkAccountsService.updateLinkAccountPriority(linkRequest);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL));
    }

    @GetMapping("/get-link-accounts")
    public ApiResponse<List<LinkAccountResponse>> getUserLinkAccounts() {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL),userLinkAccountsService.getUserLinkAccounts());
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteLinkAccounts(@PathVariable Long id) {
        userLinkAccountsService.deleteLinkAccounts(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL));
    }


}
