package com.hoaxify.ws.shared;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { ProfileImageValidator.class })
public @interface ProfileImage {

    String message() default "{hoaxify.constraints.ProfileImage.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
