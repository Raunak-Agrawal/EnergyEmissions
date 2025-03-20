package com.effix.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {
    private final TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateMailContent(Map<String, Object> variables, String mailTemplate) {
        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(mailTemplate, context);
    }
}
