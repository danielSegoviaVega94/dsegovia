package ds.evaluacion.dsegovia.service;

import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioResponseDTO obtenerUsuarioPorId(UUID  id);
    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    UsuarioResponseDTO actualizarUsuario(UUID  id, UsuarioDTO usuarioDTO);
    UsuarioResponseDTO actualizarParcialUsuario(UUID  id, UsuarioDTO usuarioDTO);
    String eliminarUsuario(UUID id);
}