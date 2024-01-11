package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("prod")
class ServicioMensajeriaTest {

    @Autowired
    IServicioMensajeria servicio;

    @Autowired
    IUsuarioRepository usuario;

    @Test
    void dadoRemitenteYDestinatarioYTextoValido_cuandoEnviarMensaje_entoncesMensajeValido() throws Exception {

        Mensaje newMensa = servicio.enviarMensaje(usuario.getUsuario(1), usuario.getUsuario(2), "Validamos servicio, enviar mensaje");
        System.out.println(newMensa);
        assertThat(newMensa, notNullValue());
        assertThat(newMensa.getId(), greaterThan(0));
    }

    @Test
    void dadoRemitenteYDestinatarioYTextoNOValido_cuandoEnviarMensaje_entoncesExcepcion() throws UsuarioException, Exception {

        assertThrows(MensajeException.class, () -> {
            Mensaje newMensa = servicio.enviarMensaje(usuario.getUsuario(0), usuario.getUsuario(2), "Val");
        });
    }

    @Test
    void dadoRemitenteYDestinatarioValido_cuandoMostrarChatConUsuario_entoncesListaMensajes() throws Exception {
        List<Mensaje> chats = servicio.mostrarChatConUsuario(usuario.getUsuario(3), usuario.getUsuario(4));
        System.out.println(chats);
        assertThat(chats, notNullValue());
    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoMostrarChatConUsuario_entoncesExcepcion() {
        assertThrows(MensajeException.class, () -> {
            List<Mensaje> chats = servicio.mostrarChatConUsuario(usuario.getUsuario(0), usuario.getUsuario(1));
        });
    }

    @Test
    void dadoCuerpoNOValido_cuandoMostrarChatConUsuario_entoncesExcepcion() {
        assertThrows(MensajeException.class, () -> {
            // el mensaje 4 de la base de datos tiene un cuerpo menor de 10
            List<Mensaje> chats = servicio.mostrarChatConUsuario(usuario.getUsuario(2), usuario.getUsuario(1));
        });
    }

    @Test
    void dadoRemitenteYDestinatarioValido_cuandoBorrarChatConUsuario_entoncesOK() {
    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoBorrarChatConUsuario_entoncesExcepcion() {
    }
}