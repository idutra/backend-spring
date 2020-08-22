package com.idutra.api.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MensagemComponente {

    @Value("${app.mensagem.appI18nEnable}")
    private boolean appI18nEnable;
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public MensagemComponente(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
        this.messageSource.setAlwaysUseMessageFormat(true);
    }

    public static final Locale LOCALE_DEFAULT = new Locale("pt","br");

    public String get(String code) {
        return messageSource.getMessage(code, new Object[]{}, this.getLocale());
    }

    public String get(String code, Object[] args) {
        return messageSource.getMessage(code, args, this.getLocale());
    }

    public String get(String code, String arg) {
        return messageSource.getMessage(code, new Object[]{arg}, this.getLocale());
    }

    public Locale getLocale(){
        if(!appI18nEnable)
            return LOCALE_DEFAULT;
        return LocaleContextHolder.getLocale();
    }

}
