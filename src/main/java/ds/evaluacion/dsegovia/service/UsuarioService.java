package ds.evaluacion.dsegovia.service;

import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import java.util.List;
import java.util.UUID;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioResponseDTO obtenerUsuarioPorId(Long  id);
    List<UsuarioResponseDTO> obtenerTodosLosUsuarios();
    UsuarioResponseDTO actualizarUsuario(Long  id, UsuarioDTO usuarioDTO);
    UsuarioResponseDTO actualizarParcialUsuario(Long  id, UsuarioDTO usuarioDTO);
    String eliminarUsuario(Long id);
}