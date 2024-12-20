package dsitp.backend.project.config;

import dsitp.backend.project.model.TipoTurno;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter de Enum a Integer
        Converter<TipoTurno, Integer> enumToIntegerConverter = new Converter<>() {
            @Override
            public Integer convert(MappingContext<TipoTurno, Integer> context) {
                return context.getSource() != null ? context.getSource().ordinal() : null;
            }
        };

        // Converter de Integer a Enum
        Converter<Integer, TipoTurno> integerToEnumConverter = new Converter<>() {
            @Override
            public TipoTurno convert(MappingContext<Integer, TipoTurno> context) {
                return context.getSource() != null ? TipoTurno.values()[context.getSource()] : null;
            }
        };

        // Registrar converters
        modelMapper.createTypeMap(TipoTurno.class, Integer.class).setConverter(enumToIntegerConverter);
        modelMapper.createTypeMap(Integer.class, TipoTurno.class).setConverter(integerToEnumConverter);

        return modelMapper;
    }
}
