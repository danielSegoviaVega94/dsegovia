package ds.evaluacion.dsegovia.service;

import ds.evaluacion.dsegovia.dto.TelefonoDTO;
import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import ds.evaluacion.dsegovia.entity.Usuario;
import ds.evaluacion.dsegovia.entity.Telefono;
import ds.evaluacion.dsegovia.exception.EmailYaExisteException;
import ds.evaluacion.dsegovia.exception.UsuarioNoEncontradoException;
import ds.evaluacion.dsegovia.mapper.UsuarioMapper;
import ds.evaluacion.dsegovia.repository.UsuarioRepository;
import ds.evaluacion.dsegovia.security.JwtTokenProvider;
import ds.evaluacion.dsegovia.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    private TelefonoDTO telefonoDTO;

    @BeforeEach
    void setUp() {
        telefonoDTO = TelefonoDTO.builder()
                .numero("912345678")
                .codigoCiudad("1")
                .codigoPais("56")
                .build();

        usuarioDTO = UsuarioDTO.builder()
                .nombre("Juan Pérez")
                .correo("juan@ejemplo.cl")
                .contrasena("Contrasena123!")
                .telefonos(Collections.singletonList(telefonoDTO))
                .build();

        usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Juan Pérez")
                .correo("juan@ejemplo.cl")
                .activo(true)
                .creado(LocalDateTime.now())
                .modificado(LocalDateTime.now())
                .ultimoLogin(LocalDateTime.now())
                .telefonos(new ArrayList<>())
                .build();

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .activo(true)
                .creado(usuario.getCreado())
                .modificado(usuario.getModificado())
                .ultimoLogin(usuario.getUltimoLogin())
                .token("token")
                .build();
    }

    @Test
    void crearUsuario_Exitoso() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(usuarioMapper.toEntity(any(UsuarioDTO.class))).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("contrasenaEncriptada");
        when(jwtTokenProvider.generarToken(anyString())).thenReturn("token");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals(usuarioResponseDTO.getIdUsuario(), resultado.getIdUsuario());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_EmailYaExiste() {
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(true);

        assertThrows(EmailYaExisteException.class, () -> usuarioService.crearUsuario(usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void obtenerUsuarioPorId_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals(usuarioResponseDTO.getIdUsuario(), resultado.getIdUsuario());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void obtenerUsuarioPorId_NoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.obtenerUsuarioPorId(1L));
    }

    @Test
    void obtenerTodosLosUsuarios_Exitoso() {
        List<Usuario> usuarios = Collections.singletonList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void actualizarUsuario_Exitoso() {
        usuarioDTO.setCorreo("juan@ejemplo.cl");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("nuevaContrasenaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.actualizarUsuario(1L, usuarioDTO);

        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).updateUserFromDTO(any(UsuarioDTO.class), any(Usuario.class));
    }

    @Test
    void actualizarUsuario_UsuarioNoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.actualizarUsuario(1L, usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_EmailYaExiste() {
        // Configuramos un escenario donde el usuario intenta cambiar su correo a uno que ya existe
        Usuario usuarioExistente = Usuario.builder()
                .idUsuario(1L)
                .correo("otro@ejemplo.cl")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(true);

        assertThrows(EmailYaExisteException.class, () -> usuarioService.actualizarUsuario(1L, usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarParcialUsuario_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(any(Usuario.class))).thenReturn(usuarioResponseDTO);

        UsuarioDTO usuarioParcialDTO = UsuarioDTO.builder()
                .nombre("Nuevo Nombre")
                .build();

        UsuarioResponseDTO resultado = usuarioService.actualizarParcialUsuario(1L, usuarioParcialDTO);

        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_Exitoso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        String resultado = usuarioService.eliminarUsuario(1L);

        assertEquals("El Usuario solicitado fue eliminado exitosamente", resultado);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void eliminarUsuario_NoExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);
        doNothing().when(usuarioRepository).deleteById(1L);

        String resultado = usuarioService.eliminarUsuario(1L);

        assertEquals("La operación de eliminación se completó sin cambios en el sistema", resultado);
        verify(usuarioRepository).deleteById(1L);
    }
}