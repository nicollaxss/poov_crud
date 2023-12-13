package poov.vacinadbcjavafx.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import poov.vacinadbcjavafx.modelo.Pessoa;
import poov.vacinadbcjavafx.modelo.Vacina;

public class AplicacaoDAO {

    private List<Vacina> vacina;
    private List<Pessoa> pessoa;

    private final Connection conexao;

    public AplicacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void gravar(Vacina vacina, Pessoa pessoa) throws SQLException {

        String sql = "INSERT INTO aplicacao(data, pessoa_codigo, vacina_codigo) VALUES (?, ?, ?);";
        PreparedStatement pstmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        Date dataAgora = new Date(System.currentTimeMillis());
        pstmt.setDate(1, dataAgora);
        pstmt.setLong(2, pessoa.getCodigo());
        pstmt.setLong(3, vacina.getCodigo());

        if (pstmt.executeUpdate() == 1) {
            System.out.println("Insercao da aplicacao feita com sucesso");
        } else {
            System.out.println("Erro ao inserir a vacina");
        }
        pstmt.close();

    }

    public void setVacina(List<Vacina> vacina) {
        this.vacina = vacina;
    }

    public List<Vacina> getVacina() {
        return vacina;
    }

    public void setPessoa(List<Pessoa> pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getPessoa() {
        return pessoa;
    }


    // public void gravar(Vacina vacina) throws SQLException {

    //     String sql = "INSERT INTO vacina(nome, descricao) VALUES (?, ?);";
    //     PreparedStatement pstmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

    //     pstmt.setString(1, vacina.getNome());
    //     pstmt.setString(2, vacina.getDescricao());

    //     if (pstmt.executeUpdate() == 1) {
    //         System.out.println("Insercao da vacina feita com sucesso");
    //         ResultSet rs = pstmt.getGeneratedKeys();
    //         if (rs.next()) {
    //             vacina.setCodigo(rs.getLong(1));
    //         } else {
    //             System.out.println("Erro ao obter o codigo gerado pelo BD para a vacina");
    //         }
    //         rs.close();
    //     } else {
    //         System.out.println("Erro ao inserir a vacina");
    //     }
    //     pstmt.close();

    // }

    // /*
    //  * adaptação do codigo me baseando na atividade jdbc4 e jdbc5 para pegar o complemento de sql ("?" e "like")
    //  * usando String no metodo pq o textField so retorna string 
    //  * 
    //  */
    // public List<Vacina> buscar(String codigo, String descricao, String nome) throws SQLException {
    //     Vacina v = null; // pegar um tipo de vacina do banco
    //     List<Vacina> vacinas = new ArrayList<>();
    //     // pesquisar se os itens estao no bdd de acordo com o exemplo do professor no quadro
    //     // String sql = "SELECT * FROM vacina WHERE codigo = ? AND situacao = 'ATIVO';"; --> modelo antigo
    //     String sql = "SELECT * FROM vacina WHERE";

    //     // setando o WHERE de acordo com os textField
    //     if (!codigo.isBlank()) {
    //         sql += " codigo = ? AND"; // adicionei and pq se cod n tiver o and no começo dos outros estraga tudo
    //     }

    //     if (!descricao.isBlank()) {
    //         sql += " descricao like ? AND";
    //     }

    //     if (!nome.isBlank()) {
    //         sql += " nome like ? AND";
    //     }

    //     sql += " situacao = 'ATIVO';"; //fim da query

    //     PreparedStatement pstmt = conexao.prepareStatement(sql);
    //     /*
    //      * antes de executar as queries tenho que transformar as Strings nos seus devidos dados
    //      * usando o exemplo do quadro:
    //      */
    //     int cont = 1;
    //     if (!codigo.isBlank()) {
    //         pstmt.setLong(cont, Long.parseLong(codigo)); // setLong so aceita tipo Long no segundo parametro, fazer a conversao
    //         cont++;
    //     }

    //     /*
    //      * para preencher a parte do like não lembrava como fazer: 
    //      * https://www.guj.com.br/t/prepared-statement-com-like/86765
    //      */
    //     if (!descricao.isBlank()) {
    //         pstmt.setString(cont, '%' + descricao.toLowerCase() + '%');
    //         cont++;
    //     }

    //     if (!nome.isBlank()) {
    //         pstmt.setString(cont, '%' + nome.toLowerCase() + '%');
    //         cont++;
    //     }

    //     ResultSet rs = pstmt.executeQuery();
    //     // enq tiver vacinas ele vai rodar o looping, conforme um exemplo que vimos na aula mas n lembro em qual material esta
    //     while (rs.next()) {
    //         v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
    //         vacinas.add(v);
    //         System.out.println("\n\n"+ v + "\nEssa vacina que foi add\n\n");
    //     } 
    //     // else {
    //     //     System.out.println("Nao foi encontrada uma vacina com o codigo " + codigo);
    //     // }
    //     rs.close();
    //     pstmt.close();
    //     return vacinas;
    // }

    // // public Vacina buscarCodigo(long codigo) throws SQLException {
    // //     Vacina v = null;
    // //     String sql = "SELECT * FROM vacina WHERE codigo = ? AND situacao = 'ATIVO';";
    // //     PreparedStatement pstmt = conexao.prepareStatement(sql);
    // //     pstmt.setLong(1, codigo);
    // //     ResultSet rs = pstmt.executeQuery();
    // //     if (rs.next()) {
    // //         v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
    // //     } else {
    // //         System.out.println("Nao foi encontrada uma vacina com o codigo " + codigo);
    // //     }
    // //     rs.close();
    // //     pstmt.close();
    // //     return v;
    // // }

    // public List<Vacina> buscarTodas() throws SQLException {
    //     Vacina v;
    //     List<Vacina> vacinas = new ArrayList<>();
    //     String sql = "SELECT * FROM vacina WHERE situacao = 'ATIVO';";
    //     Statement stmt = conexao.createStatement();
    //     ResultSet rs = stmt.executeQuery(sql);
    //     while (rs.next()) {
    //         v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
    //         vacinas.add(v);
    //     }
    //     rs.close();
    //     stmt.close();
    //     return vacinas;
    // }

    // public boolean remover(Vacina vacina) throws SQLException {
    //     boolean retorno = false;
    //     String sqlUpdate = "UPDATE vacina SET situacao = 'INATIVO' WHERE codigo = ?;";
    //     PreparedStatement pstmtUpd = conexao.prepareStatement(sqlUpdate);

    //     pstmtUpd.setLong(1, vacina.getCodigo());

    //     int resultado = pstmtUpd.executeUpdate();

    //     if (resultado == 1) {
    //         System.out.println("Remocao da vacina executada com sucesso");
    //         retorno = true;
    //     } else {
    //         System.out.println("Erro removendo a vacina com codigo: " + vacina.getCodigo());
    //     }

    //     pstmtUpd.close();

    //     return retorno;
    // }

    // public boolean atualizar(Vacina vacina) throws SQLException {
    //     boolean retorno = false;

    //     System.out.println("Entrei no atualizar e essa é a minha vacina: " + vacina);

    //     String sqlUpdate = "UPDATE vacina SET nome = ?, descricao = ?, situacao = ? WHERE codigo = ?;";
    //     PreparedStatement pstmtUpd = conexao.prepareStatement(sqlUpdate);
    //     pstmtUpd.setString(1, vacina.getNome());
    //     pstmtUpd.setString(2, vacina.getDescricao());
    //     pstmtUpd.setString(3, vacina.getSituacao().toString());
    //     pstmtUpd.setLong(4, vacina.getCodigo());

    //     int resultado = pstmtUpd.executeUpdate();
    //     if (resultado == 1) {
    //         System.out.println("Alteracao da vacina executada com sucesso");
    //         retorno = true;
    //     } else {
    //         System.out.println("Erro alterando a vacina com codigo: " + vacina.getCodigo());
    //     }
    //     pstmtUpd.close();

    //     return retorno;
    // }

}
