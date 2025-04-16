package com.brainstation23.skeleton.presenter.aspect;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.enums.SpecialCharsEnum;
import com.brainstation23.skeleton.core.domain.enums.UserTypeEnum;
import com.brainstation23.skeleton.core.domain.exceptions.UnauthorizedResourceException;
import com.brainstation23.skeleton.core.domain.model.CurrentUserContext;
import com.brainstation23.skeleton.presenter.annotations.AuthorizeScope;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Aspect
@Component
@Order(0)
public class AccessScopeControlAspect extends BaseAspect {

    @Before("@annotation(authorizeScope)")
    public void proceed(AuthorizeScope authorizeScope) {
        traceLogger.trace("Access control aspect executed");

        final List<String> scopeList = List.of(authorizeScope.scopes());

        final CurrentUserContext userContext = getCurrentUserContext();

        final Set<String> userScopes = new HashSet<>(userContext.getScopes());

        final Pattern pattern = Pattern.compile(SpecialCharsEnum.UNDERSCORE.getSign(), Pattern.LITERAL);

        scopeList.parallelStream()
                .filter(scope -> {
                    final String[] parts = pattern.split(scope, 2);
                    return (parts[0].equals(UserTypeEnum.getUserType(userContext.getUserType()).getText())
                            && (userScopes.contains(SpecialCharsEnum.ASTERISK.getSign()) || userScopes.contains(parts[1])));
                })
                .findFirst()
                .orElseThrow(() -> new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS));
    }
}
