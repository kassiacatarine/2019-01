package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import br.edu.utfpr.dao.ConnectionFactory;
import lombok.extern.java.Log;

@Log
public class ClienteDAO {

    private Connection con;

    // Responsável por criar a tabela Cliente no banco.
    public ClienteDAO() {
        this.con = ConnectionFactory.getConnection();
        log.info("Criando tabela cliente ...");
        con.createStatement()
                .executeUpdate("CREATE TABLE cliente ("
                        + "id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_cliente_pk PRIMARY KEY,"
                        + "nome varchar(255)," + "telefone varchar(30)," + "idade int," + "limiteCredito double,"
                        + "id_pais int)");
    }

    public boolean create(ClienteDTO cliente) {
        PreparedStatement stmt = null;
        try {
            log.info("Adicionando Cliente");
            stmt = con.prepareStatement(
                    "INSERT INTO cliente (nome, telefone, idade, limiteCredito, id_pais) values(?,?,?,?,?)");
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getIdade());
            stmt.setString(4, cliente.getLimiteCredito());
            stmt.setString(5, cliente.getPais().getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao Criar: " + ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public boolean update(ClienteDTO cliente) {
        PreparedStatement stmt = null;
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, idade = ?, limiteCredito = ?, id_pais = ? WHERE id = ?";

        try {
            log.info("Atualizando Cliente");
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getIdade());
            stmt.setString(4, cliente.getLimiteCredito());
            stmt.setString(5, cliente.getPais().getId());
            stmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao Atualizar: " + ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public boolean delete(int idCliente) {
        PreparedStatement stmt = null;
        try {
            log.info("Excluindo Cliente");
            stmt = con.prepareStatement("DELETE FROM cliente WHERE id = ?");
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao Excluir: " + ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public ClienteDTO find(String nome) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            log.info("Encontrando Cliente");
            stmt = con.prepareStatement(
                    "SELECT id, nome, telefone, idade, limiteCredito, id_pais FROM cliente WHERE name like concat('%', ?,'%')");
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            ClienteDTO cliente;
            while (rs.next()) {
                cliente = ClienteDTO.builder().id(rs.getInt(1)).nome(rs.getString(2)).telefone(rs.getString(3))
                        .idade(rs.getInt(4)).limiteCredito(rs.getDouble(5))
                        .pais(PaisDTO.builder().id(rs.getInt(6)).build()).build();
            }
            return cliente;
        } catch (SQLException ex) {
            System.err.println("Erro ao encontrar: " + ex);
            return null;
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
    }

}