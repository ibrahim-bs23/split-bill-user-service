package com.brainstation23.skeleton.common.utils;

import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.ServiceDomainException;
import com.brainstation23.skeleton.core.service.BaseService;
import freemarker.template.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NotificationUtils extends BaseService {

    private static final String TEMPLATE_NAME = "Wallet_Notification_Template";

    public static String prepareNotificationContent(String content, Map<String, Object> templateParams) {
        //checking templateParams(Map) contains all the required parameters or not, then proceed accordingly
        NotificationUtils.checkTemplateParams(content, templateParams);

        try {
            freemarker.template.Template template = new freemarker.template.Template(TEMPLATE_NAME, new StringReader(content), new Configuration(Configuration.VERSION_2_3_30));

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateParams);
        } catch (Exception ex) {
            throw new ServiceDomainException(ResponseMessage.TEMPLATE_PROCESSING_ERROR.getResponseMessage());
        }

    }

    private static int getTotalPlaceholder(String messageContent) {
        String placeHolderRegex = "\\$\\{([a-zA-Z]+)\\}";
        Pattern placeHolderPattern = Pattern.compile(placeHolderRegex);
        Matcher m = placeHolderPattern.matcher(messageContent);
        Set<String> matches = new HashSet<>();
        while (m.find()) matches.add(m.group(1));
        return matches.size();
    }

    private static void checkTemplateParams(String content, Map<String, Object> templateParams) throws ServiceDomainException {
        int totalPlaceholderMatched = getTotalPlaceholder(content);
        int totalParams = templateParams.size();

        //total count check
        if (totalParams < totalPlaceholderMatched) {
            throw new ServiceDomainException(ResponseMessage.TEMPLATE_PARAM_COUNT_MISMATCH.getResponseMessage());
        }

        //extract required parameters that content holds
        List<String> requiredParamsList = getRequiredParamsFromTemplate(content);

        //Check that all required variables are present and not null in templateParams
        for (String param : requiredParamsList) {
            if (!templateParams.containsKey(param) || templateParams.get(param) == null) {
                throw new ServiceDomainException(ResponseMessage.TEMPLATE_PARAM_MISMATCH.getResponseMessage());
            }
        }
    }

    /**
     * regular expression pattern to match the ${} placeholders in the template string and captures the text between the
     * braces as a group.
     *
     * @param templateText
     * @return
     * @throws Exception
     */
    private static List<String> getRequiredParamsFromTemplate(String templateText) throws ServiceDomainException {
        Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(templateText);
        List<String> requiredParams = new ArrayList<>();
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (paramName.trim().isEmpty()) {
                throw new ServiceDomainException(ResponseMessage.TEMPLATE_PARAM_TYPO.getResponseMessage());
            }
            requiredParams.add(paramName);
        }
        return requiredParams;
    }
}
