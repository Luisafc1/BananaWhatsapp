package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MensajeServicioImpl implements IServicioMensajeria {
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;

    @Override
    public Mensaje enviarMensaje(Usuario remitente, Usuario destinatario, String texto) throws UsuarioException, MensajeException {
        try {
            Mensaje mensaje = new Mensaje(null, remitente, destinatario, texto, LocalDate.now());
            mensajeRepo.crear(mensaje);
            mensaje.valido();
            return mensaje;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MensajeException("Error en la creación: " + e.getMessage());
        }

    }

    @Override
    public List<Mensaje> mostrarChatConUsuario(Usuario remitente, Usuario destinatario) throws UsuarioException, MensajeException {
        List<Mensaje> chats = new ArrayList<>();
        try {
            List<Mensaje> mensajes = new ArrayList<>();
            mensajes = mensajeRepo.obtener(remitente);
            for (Mensaje mensaje : mensajes) {
                mensaje.valido();
                int i = 1;
                if (mensaje.getDestinatario().getId().equals(destinatario.getId())) {
                    chats.add(mensaje);
                    i++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MensajeException("Error en la consulta del chat con usuario: " + e.getMessage());

        }
        return chats;
    }

    @Override
    public boolean borrarChatConUsuario(Usuario remitente, Usuario destinatario) throws
            UsuarioException, MensajeException {
        return false;
    }
}
