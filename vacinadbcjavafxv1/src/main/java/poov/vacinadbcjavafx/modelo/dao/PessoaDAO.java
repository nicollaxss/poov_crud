package poov.vacinadbcjavafx.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import poov.vacinadbcjavafx.modelo.Pessoa;

public class PessoaDAO {

    private final Connection conexao;

    public PessoaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public List<Pessoa> buscar(String codigo, String nome, String cpf, LocalDate dataInicial, LocalDate dataFinal) throws SQLException {
        Pessoa p = null; // pegar um tipo de vacina do banco
        List<Pessoa> pessoas = new ArrayList<>();
        // pesquisar se os itens estao no bdd de acordo com o exemplo do professor no quadro
        // String sql = "SELECT * FROM vacina WHERE codigo = ? AND situacao = 'ATIVO';"; --> modelo antigo
        String sql = "SELECT * FROM pessoa WHERE";

        // setando o WHERE de acordo com os textField
        if (!codigo.isBlank()) {
            sql += " codigo = ? AND"; // adicionei and pq se cod n tiver o and no começo dos outros estraga tudo
        }

        if (!nome.isBlank()) {
            sql += " nome like ? AND";
        }

        if (!cpf.isBlank()) {
            sql += " cpf like ? AND";
        }

        // colocar agora o periodo das datas selecionadas

        if (dataInicial != null && dataFinal != null) {
            sql += " datanascimento BETWEEN ? AND ? AND";
        }

        sql += " situacao = 'ATIVO';"; //fim da query

        PreparedStatement pstmt = conexao.prepareStatement(sql);
        /*
         * antes de executar as queries tenho que transformar as Strings nos seus devidos dados
         * usando o exemplo do quadro:
         */
        int cont = 1;
        if (!codigo.isBlank()) {
            pstmt.setLong(cont, Long.parseLong(codigo)); // setLong so aceita tipo Long no segundo parametro, fazer a conversao
            cont++;
        }

        /*
         * para preencher a parte do like não lembrava como fazer: 
         * https://www.guj.com.br/t/prepared-statement-com-like/86765
         */
        if (!nome.isBlank()) {
            pstmt.setString(cont, '%' + nome.toLowerCase() + '%');
            cont++;
        }

        if (!cpf.isBlank()) {
            pstmt.setString(cont, '%' + cpf.toLowerCase() + '%');
            cont++;
        }

        if (dataInicial != null && dataFinal != null) {
            pstmt.setDate(cont, Date.valueOf(dataInicial));
            cont++;
            pstmt.setDate(cont, Date.valueOf(dataFinal));
            cont++;
        }

        ResultSet rs = pstmt.executeQuery();
        // enq tiver vacinas ele vai rodar o looping, conforme um exemplo que vimos na aula mas n lembro em qual material esta
        while (rs.next()) {
            p = new Pessoa(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
            pessoas.add(p);
            // System.out.println("\n\n"+ p + "\nEssa pessoa que foi add\n\n");
        } 
        // else {
        //     System.out.println("Nao foi encontrada uma vacina com o codigo " + codigo);
        // }
        rs.close();
        pstmt.close();
        return pessoas;
    }

    public List<Pessoa> buscarTodas() throws SQLException {
        Pessoa p;
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM pessoa WHERE situacao = 'ATIVO';";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            p = new Pessoa(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
            pessoas.add(p);
        }
        rs.close();
        stmt.close();
        return pessoas;
    }


}
