package demo.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by reeco_000 on 2015/7/22.
 */
public class LoginValidator extends Validator {
    @Override
    protected void validate(Controller c) {
        validateRequiredString("username","nameError","username is null");
        validateRequiredString("password","passError","username is null");
    }

    @Override
    protected void handleError(Controller c) {

    }
}