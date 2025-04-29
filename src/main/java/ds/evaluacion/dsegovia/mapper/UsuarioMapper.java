package ds.evaluacion.dsegovia.mapper;

import ds.evaluacion.dsegovia.dto.TelefonoDTO;
import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import ds.evaluacion.dsegovia.entity.Telefono;
import ds.evaluacion.dsegovia.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "creado", ignore = true)
    @Mapping(target = "modificado", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
    @Mapping(source = "telefonos", target = "telefonos")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    Telefono toTelefonoEntity(TelefonoDTO dto);

    TelefonoDTO toTelefonoDTO(Telefono telefono);

    List<Telefono> toTelefonoEntityList(List<TelefonoDTO> dtos);

    List<TelefonoDTO> toTelefonoDTOList(List<Telefono> telefonos);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "creado", ignore = true)
    @Mapping(target = "modificado", ignore = true)
    @Mapping(target = "ultimoLogin", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "activo", ignore = true)
    void updateUserFromDTO(UsuarioDTO dto, @MappingTarget Usuario usuario);

    default List<Telefono> toTelefonoEntityListWithUsuario(List<TelefonoDTO> dtos, Usuario usuario) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        List<Telefono> telefonos = new ArrayList<>();

        for (TelefonoDTO dto : dtos) {
            Telefono telefono = toTelefonoEntity(dto);
            telefono.setUsuario(usuario);
            telefonos.add(telefono);
        }

        return telefonos;
    }
}
