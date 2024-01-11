package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



@Setter
@Getter

public class MensajeJDBCRepo implements IMensajeRepository {
    private String db_url;

    @Autowired
    IUsuarioRepository repo;

    @Override
    public Mensaje crear(Mensaje mensaje) throws SQLException {
        String sql = "INSERT INTO mensaje values (NULL,?,?,?,?)";

        try (
                Connection conn = DriverManager.getConnection(db_url);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            mensaje.valido();


            stmt.setString(1, mensaje.getCuerpo());
            stmt.setDate(2, Date.valueOf(mensaje.getFecha()));
            stmt.setInt(3, mensaje.getRemitente().getId());
            stmt.setInt(4, mensaje.getDestinatario().getId());


            int rows = stmt.executeUpdate();

            ResultSet genKeys = stmt.getGeneratedKeys();
            if (genKeys.next()) {
                mensaje.setId(genKeys.getInt(1));
            } else {
                throw new SQLException("No id asignado");
            }
        } catch (MensajeException e) {
            e.printStackTrace();
            throw new MensajeException("Mensaje no valido");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
        return mensaje;
    }

    @Override
    public List<Mensaje> obtener(Usuario usuario) throws Exception {
        List<Mensaje> mensajes = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(db_url);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mensaje m WHERE m.from_user = " + usuario.getId() +" ");

        ) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                mensajes.add(
                        new Mensaje(
                                rs.getInt("id"),
                                repo.getUsuario(rs.getInt("from_user")),
                                repo.getUsuario(rs.getInt("to_user")),
                                rs.getString("cuerpo"),
                                rs.getDate("fecha").toLocalDate()
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mensajes;

    }

    @Override
    public boolean borrarTodos(Usuario usuario) throws SQLException {
        return false;
    }
}
