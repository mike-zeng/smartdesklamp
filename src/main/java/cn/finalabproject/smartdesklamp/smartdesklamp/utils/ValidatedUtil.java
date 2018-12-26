package cn.finalabproject.smartdesklamp.smartdesklamp.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatedUtil {
    private static Validator validator= (Validator) Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    public static<T> Boolean validate(T v){
        Set<ConstraintViolation<T>> set= validator.validate(v);
        if (set.size()==0){
            return true;
        }else{
            return false;
        }
    }

}
