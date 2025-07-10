package ds.evaluacion.dsegovia.mapper;

import ds.evaluacion.dsegovia.dto.TelefonoDTO;
import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import ds.evaluacion.dsegovia.entity.Telefono;
import ds.evaluacion.dsegovia.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario",   ignore = true)
    @Mapping(target = "creado",      ignore = true)
    @Mapping(target = "modificado",  ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "token",       ignore = true)
    @Mapping(target = "activo",      ignore = true)
    @Mapping(target = "telefonos",   ignore = true)
    Usuario toEntity(UsuarioDTO dto);


    UsuarioResponseDTO toResponseDTO(Usuario usuario);



    @Mapping(target = "usuario", ignore = true)
    Telefono toTelefonoEntity(TelefonoDTO dto);


    TelefonoDTO toTelefonoDTO(Telefono telefono);


    List<Telefono>      toTelefonoEntityList(List<TelefonoDTO> dtos);
    List<TelefonoDTO>   toTelefonoDTOList(List<Telefono> telefonos);

    @Mapping(target = "idUsuario",   ignore = true)
    @Mapping(target = "creado",      ignore = true)
    @Mapping(target = "modificado",  ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "token",       ignore = true)
    @Mapping(target = "activo",      ignore = true)
    @Mapping(target = "telefonos",   ignore = true)
    void updateUserFromDTO(UsuarioDTO dto, @MappingTarget Usuario usuario);
}
