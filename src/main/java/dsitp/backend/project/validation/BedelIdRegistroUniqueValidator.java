package dsitp.backend.project.validation;

import dsitp.backend.project.service.BedelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;

class BedelIdRegistroUniqueValidator implements ConstraintValidator<BedelIdRegistroUnique, String> {

    private final BedelService bedelService;
    private final HttpServletRequest request;

    public BedelIdRegistroUniqueValidator(final BedelService bedelService,
            final HttpServletRequest request) {
        this.bedelService = bedelService;
        this.request = request;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
        if (value == null) {

            return true;
        }
        @SuppressWarnings("unchecked")
        final Map<String, String> pathVariables
                = ((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        final String currentId = pathVariables.get("id");
        if (currentId != null && value.equalsIgnoreCase(bedelService.get(Integer.valueOf(currentId)).getIdRegistro())) {

            return true;
        }
        return !bedelService.idRegistroExists(value);
    }

}
