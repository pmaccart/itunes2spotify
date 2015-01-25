package com.itunes2spotify.api.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AccessTokenRequestConverter implements GenericConverter {

    private Set<ConvertiblePair> convertibleTypes = new HashSet<ConvertiblePair>(
            Arrays.asList(new ConvertiblePair(AccessTokenRequest.class, AccessTokenRequest.class)));

    public Set<ConvertiblePair> getConvertibleTypes() {
        return convertibleTypes;
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return source;
    }

}
