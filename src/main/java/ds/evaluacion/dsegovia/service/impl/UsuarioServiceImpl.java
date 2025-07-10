package ds.evaluacion.dsegovia.service.impl;

import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import ds.evaluacion.dsegovia.entity.Telefono;
import ds.evaluacion.dsegovia.entity.Usuario;
import ds.evaluacion.dsegovia.exception.EmailYaExisteException;
import ds.evaluacion.dsegovia.exception.UsuarioNoEncontradoException;
import ds.evaluacion.dsegovia.mapper.UsuarioMapper;
import ds.evaluacion.dsegovia.repository.UsuarioRepository;
import ds.evaluacion.dsegovia.security.JwtTokenProvider;
import ds.evaluacion.dsegovia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailYaExisteException("El correo ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        usuario.setToken(jwtTokenProvider.generarToken(usuario.getCorreo()));
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setActivo(true);

        if (usuarioDTO.getTelefonos() != null && !usuarioDTO.getTelefonos().isEmpty()) {
            usuarioDTO.getTelefonos().forEach(t ->
                    usuario.addTelefono(usuarioMapper.toTelefonoEntity(t)));
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        validarCambioDeCorreo(usuario, usuarioDTO);

        usuarioMapper.updateUserFromDTO(usuarioDTO, usuario);
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));

        actualizarTelefonos(usuario, usuarioDTO);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarParcialUsuario(UUID id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (usuarioDTO.getCorreo() != null) {
            validarCambioDeCorreo(usuario, usuarioDTO);
            usuario.setCorreo(usuarioDTO.getCorreo());
        }

        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }

        if (usuarioDTO.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        if (usuarioDTO.getTelefonos() != null) {
            actualizarTelefonos(usuario, usuarioDTO);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    public String eliminarUsuario(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
        return "El usuario fue eliminado exitosamente";
    }


    private void validarCambioDeCorreo(Usuario usuarioExistente, UsuarioDTO usuarioDTO) {
        if (!usuarioExistente.getCorreo().equals(usuarioDTO.getCorreo()) &&
                usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailYaExisteException("El correo ya está registrado");
        }
    }

    private void actualizarTelefonos(Usuario usuario, UsuarioDTO dto) {
        usuario.clearTelefonos();
        if (dto.getTelefonos() != null && !dto.getTelefonos().isEmpty()) {
            dto.getTelefonos().forEach(t ->
                    usuario.addTelefono(usuarioMapper.toTelefonoEntity(t)));
        }
    }

}