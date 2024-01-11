package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("prod")
class MensajeRepositoryTest {

    @Autowired
    IMensajeRepository repo;

    @Autowired
    IUsuarioRepository usuario;

    @Test
    void dadoUnMensajeValido_cuandoCrear_entoncesMensajeValido() throws Exception {

        Mensaje nuevo = new Mensaje(null,usuario.getUsuario(1), usuario.getUsuario(2), "Buenos dias", LocalDate.now());

        repo.crear(nuevo);
        System.out.println(nuevo);

        assertThat(nuevo, notNullValue());
        assertThat(nuevo.getId(), greaterThan(0));
    }

    @Test
    void dadoUnMensajeNOValido_cuandoCrear_entoncesExcepcion() throws Exception {

        Mensaje nuevo = new Mensaje(null,usuario.getUsuario(1), usuario.getUsuario(0), "Destinatario erroneo", LocalDate.now());
        assertThrows(Exception.class, () -> {
            repo.crear(nuevo);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoObtener_entoncesListaMensajes() throws Exception {
        List<Mensaje> mensajes = repo.obtener(usuario.getUsuario(1));

        System.out.println(mensajes);

        assertThat(mensajes.size(), greaterThan(0));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtener_entoncesExcepcion() {
    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrarTodos_entoncesOK() {
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrarTodos_entoncesExcepcion() {
    }

}