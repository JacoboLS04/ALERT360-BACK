package co.edu.uniquindio.alert360_BACK.security.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {


    private ModelMapper modelMapper;

    @Autowired
    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <D, T> D mapToDTO(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <T, D> T mapToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

}
