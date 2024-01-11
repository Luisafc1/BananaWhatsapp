package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import com.banana.bananawhatsapp.servicios.IServicioMensajeria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Setter
@Getter
public class ControladorMensajes {

    @Autowired
    private IServicioMensajeria servicioMensajeria;

    @Autowired
    IUsuarioRepository usuario;


    public boolean enviarMensaje(Integer remitente, Integer destinatario, String texto) throws Exception {
        try {
            Mensaje mensaje = servicioMensajeria.enviarMensaje(usuario.getUsuario(remitente), usuario.getUsuario(destinatario), texto);
            System.out.println("Mensaje enviado: " + mensaje);
            return true;
        } catch (Exception e) {
            System.out.println("Ha habido un error: " + e.getMessage());
            throw e;
        }

    }

    public boolean mostrarChat(Integer remitente, Integer destinatario) throws Exception {
        try {
            List<Mensaje> mensajes = servicioMensajeria.mostrarChatConUsuario(usuario.getUsuario(remitente), usuario.getUsuario(destinatario));
            if (mensajes != null && mensajes.size() > 0) {
                System.out.println("Mensajes entre: " + remitente + " y " + destinatario);
                for (Mensaje mensaje : mensajes) {
                    System.out.println(mensaje);
                }
            } else {
                System.out.println("NO hay mensajes entre: " + remitente + " y " + destinatario);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Ha habido un error: " + e.getMessage());
            throw e;
        }

    }

    public boolean eliminarChatConUsuario(Integer remitente, Integer destinatario) {
        try {
            Usuario uRemitente = new Usuario();
            uRemitente.setId(remitente);
            Usuario uDestinatario = new Usuario();
            uDestinatario.setId(destinatario);

            boolean isOK = servicioMensajeria.borrarChatConUsuario(uRemitente, uDestinatario);
            if (isOK) {
                System.out.println("Mensajes borrados entre: " + remitente + " y " + destinatario);
            } else {
                System.out.println("NO se han borrado mensajes entre: " + remitente + " y " + destinatario);
            }
            return isOK;
        } catch (Exception e) {
            System.out.println("Ha habido un error: " + e.getMessage());
            throw e;
        }

    }


}