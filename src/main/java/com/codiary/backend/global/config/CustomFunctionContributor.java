package com.codiary.backend.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {

    public void contributeFunctions(FunctionContributions functionContributions) {
        BasicType<Double> resultType = functionContributions
                .getTypeConfiguration()
                .getBasicTypeRegistry()
                .resolve(StandardBasicTypes.DOUBLE);

        functionContributions.getFunctionRegistry()
                .registerPattern("match", "match(?1,?2) against (?3 in natural language mode)", resultType);
    }
}
