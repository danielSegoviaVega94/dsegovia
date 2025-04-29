package ds.evaluacion.dsegovia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ds.evaluacion.dsegovia.config.ValidacionConfig;
import ds.evaluacion.dsegovia.dto.TelefonoDTO;
import ds.evaluacion.dsegovia.dto.UsuarioDTO;
import ds.evaluacion.dsegovia.dto.UsuarioResponseDTO;
import ds.evaluacion.dsegovia.exception.GlobalExceptionHandler;
import ds.evaluacion.dsegovia.exception.UsuarioNoEncontradoException;
import ds.evaluacion.dsegovia.service.UsuarioService;
import ds.evaluacion.dsegovia.validation.ValidPasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsuarioController.class)
@Import({GlobalExceptionHandler.class,ValidacionConfig.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UsuarioService usuarioService;
    private UsuarioDTO usuarioDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        TelefonoDTO telefonoDTO = TelefonoDTO.builder()
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

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .idUsuario(1L)
                .activo(true)
                .creado(LocalDateTime.now())
                .modificado(LocalDateTime.now())
                .ultimoLogin(LocalDateTime.now())
                .token("token")
                .build();
    }


    @Test
    @WithMockUser
    void crearUsuario_Exitoso() throws Exception {
        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuarioResponseDTO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    @WithMockUser
    void crearUsuario_EmailInvalido() throws Exception {
        usuarioDTO.setCorreo("correo-invalido");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    @WithMockUser
    void obtenerUsuarioPorId_Exitoso() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponseDTO);

        mockMvc.perform(get("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L));
    }

    @Test
    @WithMockUser
    void obtenerUsuarioPorId_NoEncontrado() throws Exception {
        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenThrow(new UsuarioNoEncontradoException("Usuario no encontrado"));

        mockMvc.perform(get("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario no encontrado"));
    }

    @Test
    @WithMockUser
    void obtenerTodosLosUsuarios_Exitoso() throws Exception {
        List<UsuarioResponseDTO> usuarios = Collections.singletonList(usuarioResponseDTO);
        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1L));
    }

    @Test
    @WithMockUser
    void actualizarUsuario_Exitoso() throws Exception {
        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioDTO.class)))
                .thenReturn(usuarioResponseDTO);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L));
    }

    @Test
    @WithMockUser
    void actualizarParcialUsuario_Exitoso() throws Exception {
        UsuarioDTO usuarioParcialDTO = UsuarioDTO.builder()
                .nombre("Nuevo Nombre")
                .build();

        when(usuarioService.actualizarParcialUsuario(eq(1L), any(UsuarioDTO.class)))
                .thenReturn(usuarioResponseDTO);

        mockMvc.perform(patch("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioParcialDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1L));
    }

    @Test
    @WithMockUser
    void eliminarUsuario_Exitoso() throws Exception {
        when(usuarioService.eliminarUsuario(1L))
                .thenReturn("El Usuario solicitado fue eliminado exitosamente");

        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje")
                        .value("El Usuario solicitado fue eliminado exitosamente"));
    }
}