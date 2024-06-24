package com.salam.spring_security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String ressourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String ressourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", ressourceName, fieldName, fieldValue));
        this.ressourceName = ressourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRessourceName() {
        return ressourceName;
    }
}
