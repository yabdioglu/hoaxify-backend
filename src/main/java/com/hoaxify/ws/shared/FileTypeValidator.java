package com.hoaxify.ws.shared;

import com.hoaxify.ws.file.FileService;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {

    @Autowired
    FileService fileService;

    String[] types;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.types = constraintAnnotation.types();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()){
            return true;
        }
        String fileType = fileService.detectType(value);
        for(String supportedType: this.types){
            if(fileType.contains(supportedType)){
                return true;
            }
        }
        String supportedTypes = Arrays.stream(this.types).collect(Collectors.joining(", "));

        context.disableDefaultConstraintViolation(); //Default oluşturacağı mesajı engelleme çağrısı yapıyor.
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateConstraintValidatorContext.addMessageParameter("types", supportedTypes); // ValidationMessagestaki types'ı bizim supportedTypes olarak kullan.
        hibernateConstraintValidatorContext.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();

        return false;
    }
}
