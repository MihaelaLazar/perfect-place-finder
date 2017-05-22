package com.sgbd.model;

//import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

public class TestValidator {

//    @Test
    public  void test() {
        User user = new User("fdsfds", "", "", "", "");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();

        System.out.println(validator.validate(user).size());
        for (ConstraintViolation<User> constraintViolation: validator.validate(user)) {
            System.out.println(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
        }
    }
}
