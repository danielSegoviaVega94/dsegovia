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

        String token = jwtTokenProvider.generarToken(usuario.getCorreo());
        usuario.setToken(token);
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setActivo(true);

        usuario.setTelefonos(new ArrayList<>());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (usuarioDTO.getTelefonos() != null && !usuarioDTO.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = usuarioMapper.toTelefonoEntityListWithUsuario(
                    usuarioDTO.getTelefonos(),
                    usuarioGuardado
            );
            usuarioGuardado.setTelefonos(telefonos);
            usuarioGuardado = usuarioRepository.save(usuarioGuardado);
        }

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long  id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long  id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!usuario.getCorreo().equals(usuarioDTO.getCorreo()) &&
                usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailYaExisteException("El correo ya está registrado");
        }

        usuarioMapper.updateUserFromDTO(usuarioDTO, usuario);
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));

        usuario.getTelefonos().clear();
        List<Telefono> telefonos = usuarioMapper.toTelefonoEntityListWithUsuario(
                usuarioDTO.getTelefonos(),
                usuario
        );
        usuario.getTelefonos().addAll(telefonos);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarParcialUsuario(Long  id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (usuarioDTO.getCorreo() != null &&
                !usuario.getCorreo().equals(usuarioDTO.getCorreo()) &&
                usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new EmailYaExisteException("El correo ya está registrado");
        }

        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }

        if (usuarioDTO.getCorreo() != null) {
            usuario.setCorreo(usuarioDTO.getCorreo());
        }

        if (usuarioDTO.getContrasena() != null) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        if (usuarioDTO.getTelefonos() != null) {
            usuario.getTelefonos().clear();
            usuario.getTelefonos().addAll(usuarioMapper.toTelefonoEntityList(usuarioDTO.getTelefonos()));
        }

        if (usuarioDTO.getTelefonos() != null) {
            usuario.getTelefonos().clear();
            List<Telefono> telefonos = usuarioMapper.toTelefonoEntityListWithUsuario(
                    usuarioDTO.getTelefonos(),
                    usuario
            );
            usuario.getTelefonos().addAll(telefonos);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public String eliminarUsuario(Long id) {
        boolean existia = usuarioRepository.existsById(id);

        usuarioRepository.deleteById(id);

        if (existia) {
            return "El Usuario solicitado fue eliminado exitosamente";
        } else {
            return "La operación de eliminación se completó sin cambios en el sistema";
        }
    }
}