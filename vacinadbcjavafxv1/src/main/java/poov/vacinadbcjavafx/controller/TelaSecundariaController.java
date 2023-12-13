package poov.vacinadbcjavafx.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import poov.vacinadbcjavafx.modelo.Vacina;
import poov.vacinadbcjavafx.modelo.dao.DAOFactory;
import poov.vacinadbcjavafx.modelo.dao.VacinaDAO;


public class TelaSecundariaController {

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonOk;

    @FXML
    private Label labelTextCodigo;

    @FXML
    private TextArea textAreaDescricao2;

    @FXML
    private TextField textFieldCodigo2;

    @FXML
    private TextField textFieldNome2;

        // variaveis
    public DAOFactory daoFactory;

    private boolean editar = false;
    private Vacina vacina;
    // private Long codigoNovo;

    public TelaSecundariaController() {
        System.out.println("TelaSecundariaController criado");
    }

    public void setTextFieldNonEditable(Boolean opcao) {
        // https://stackoverflow.com/questions/20205145/javafx-how-to-show-read-only-text
        textFieldCodigo2.setEditable(opcao);
    }

    public void setTextFieldVisible(Boolean opcao) {
        // https://stackoverflow.com/questions/32451986/how-to-hide-or-deactivate-a-textfield-and-a-label-with-javafx
        textFieldCodigo2.setVisible(opcao);
        labelTextCodigo.setVisible(opcao);
    }

    @FXML
    void buttonCancelarClicado(ActionEvent event) {
        if (!editar) {
            textAreaDescricao2.setText(null);
            textFieldCodigo2.setText(null); 
            textFieldNome2.setText(null);
            ((Button)event.getSource()).getScene().getWindow().hide();
        } else {
            /*
             * Se for apenas para editar, manter o codigo, nome e descricao da vacina
             */
            textFieldCodigo2.setText(String.valueOf(vacina.getCodigo()));
            textFieldNome2.setText(vacina.getNome());
            // System.out.println("Essa é a descricao " + this.descricao);
            textAreaDescricao2.setText(vacina.getDescricao());        
        }
    }

    public void atualizarVacina() {
        try {
            // como eu ja tenho a vacina
            vacina.setNome(textFieldNome2.getText());
            vacina.setDescricao(textAreaDescricao2.getText());
            // System.out.println("Vacina detro do buttonOkClicado: \n" + vacina);
            DAOFactory daoFactory = new DAOFactory();
            daoFactory.abrirConexao();
            VacinaDAO dao = daoFactory.criarVacinaDAO();
            dao.atualizar(vacina);
            // System.out.println("Vacina depois de buscarCodigo: \n" + vacina);
        } catch (SQLException ex) {
            DAOFactory.mostrarSQLException(ex);
        } finally {
            // daoFactory.fecharConexao();
        }
    }

    public void criarVacina(String nome, String descricao) {
        
        try {
            Vacina v = new Vacina(nome, descricao);
            DAOFactory daoFactory = new DAOFactory();
            daoFactory.abrirConexao();
            VacinaDAO dao = daoFactory.criarVacinaDAO();
            // System.out.println("ESSA É A VACINA CRIADA: "+ v);
            dao.gravar(v);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Vacina criada com sucesso!");
            alert.show();    
        } catch (SQLException ex) {
            DAOFactory.mostrarSQLException(ex);
        } finally {
            // daoFactory.fecharConexao();
        }
    }

    @FXML
    void buttonOkClicado(ActionEvent event) {
        /*
         * Ideias para verificar: 
         * 1 - colocar um try catch depois q descobrir o tipo de erro q retorna V
         * 2 - so funciona o enviar se estiver valido V
         * 3 - se tiver valido eu altero no banco de dados as informaçoes V
         * 4 - faço a vacina receber as novas informações para setar elas la no primario caso queiro ja editar de novo V
         * 5 - atualizar o tableView qnd editar
        */
            // System.out.println("Estou dentro de validar campos e eles estao validados");
            if (editar) {
                if (validarCampos()) {
                    atualizarVacina();
                    Alert editarVacina = new Alert(Alert.AlertType.INFORMATION);
                    editarVacina.setHeaderText("Vacina editada com sucesso!");
                    editarVacina.show();
                    // System.out.println("Vacina depois de buscarCodigo: \n" + vacina);
                    ((Button)event.getSource()).getScene().getWindow().hide();
                    // ((Button)event.getSource()).getScene().getWindow().hide();
                } else {
                    Alert confirmacao = new Alert(Alert.AlertType.INFORMATION);
                    confirmacao.setContentText("Um dos campos foi deixado em branco!");
                    confirmacao.show();
                    // if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                    //     atualizarVacina();
                    //     Alert editarVacina = new Alert(Alert.AlertType.INFORMATION);
                    //     editarVacina.setHeaderText("Vacina editada com sucesso!");
                    //     editarVacina.show();
                    //     ((Button)event.getSource()).getScene().getWindow().hide();
                    // } else {
                    //     buttonCancelarClicado(event);
                    // }
                } 
                
            } else {
                // ADD NOVA VACINA
                // System.out.println("\nENTRAMOS EM EDITAR = FALSE\n");
                
                String nome;
                String descricao;
                nome = textFieldNome2.getText();
                descricao = textAreaDescricao2.getText();

                if(nome.isBlank() || descricao.isBlank()) {
                    Alert confirmacao = new Alert(Alert.AlertType.INFORMATION);
                    confirmacao.setContentText("Um dos campos foi deixado em branco!");
                    confirmacao.show();

                    // confirmacao.setContentText("Um dos campos foi deixado em branco, deseja continuar?");
                    // Optional<ButtonType> buttonType = confirmacao.showAndWait(); 
                    // if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                    //     criarVacina(nome, descricao);
                    //     ((Button)event.getSource()).getScene().getWindow().hide();
                    // } else {
                    //     buttonCancelarClicado(event);
                    // } 
                } else {
                    criarVacina(nome, descricao);
                    ((Button)event.getSource()).getScene().getWindow().hide();
                }
            }  
    }

    // metodos

    public void editarVacina(boolean opcao) {
        ;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
        textFieldCodigo2.setText(String.valueOf(vacina.getCodigo()));
        textFieldNome2.setText(vacina.getNome());
        textAreaDescricao2.setText(vacina.getDescricao());
    }

    public void pegarLabels () {
        ;
    }

    public Vacina getVacina() {
        return vacina;
    }

    // public boolean isValido() {
    //     // return valido;
    // }
    
    // verificar se estou editando ou nao
    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public void limpar() {
        // valido = false;
        vacina = new Vacina();
        textFieldCodigo2.setText("");
        textFieldNome2.setText("");
        textAreaDescricao2.setText("");
    }

    private boolean validarCampos() {
        return !textFieldCodigo2.getText().isEmpty() &&
               !textFieldNome2.getText().isEmpty() &&
               !textAreaDescricao2.getText().isEmpty();
    }

    public void setConexaoDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    

}