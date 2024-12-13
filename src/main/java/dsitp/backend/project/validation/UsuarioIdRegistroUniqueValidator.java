package dsitp.backend.project.validation;

import dsitp.backend.project.service.AdministradorService;
import dsitp.backend.project.service.BedelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UsuarioIdRegistroUniqueValidator implements ConstraintValidator<UsuarioIdRegistroUnique, String> {

    private final BedelService bedelService;
    private final AdministradorService administradorService;
    private final HttpServletRequest request;

    @Autowired
    public UsuarioIdRegistroUniqueValidator(final BedelService bedelService, final AdministradorService administradorService, final HttpServletRequest request) {
        this.bedelService = bedelService;
        this.administradorService = administradorService;
        this.request = request;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {

            return true;
        }
//        @SuppressWarnings("unchecked")
//        final Map<String, String> pathVariables
//                = ((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
//        final String currentId = pathVariables.get("id");
//        if (currentId != null && value.equalsIgnoreCase(bedelService.getBedelByIdRegistro(currentId).getIdRegistro()) && value.equalsIgnoreCase(administradorService.getByIdRegistro(currentId).getIdRegistro())) {
//
//            return true;
//        }

        if (!(bedelService.idRegistroExists(value) || administradorService.idRegistroExists(value))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Ya existe usuario con mismo id.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
