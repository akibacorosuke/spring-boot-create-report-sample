package root.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    @Autowired
    private MessageSource messageSource;

    public String format(final String key, final String... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
