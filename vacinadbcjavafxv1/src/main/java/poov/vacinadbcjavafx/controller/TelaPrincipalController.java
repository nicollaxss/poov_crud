package poov.vacinadbcjavafx.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import poov.vacinadbcjavafx.modelo.Pessoa;
import poov.vacinadbcjavafx.modelo.Vacina;
import poov.vacinadbcjavafx.modelo.dao.AplicacaoDAO;
import poov.vacinadbcjavafx.modelo.dao.DAOFactory;
import poov.vacinadbcjavafx.modelo.dao.PessoaDAO;
import poov.vacinadbcjavafx.modelo.dao.VacinaDAO;

public class TelaPrincipalController implements Initializable {

    @FXML
    private Button buttonCriarAplicacao;

    @FXML
    private Button buttonEditar;

    @FXML
    private Button buttonNova;

    @FXML
    private Button buttonPesquisar;

    @FXML
    private Button buttonPesquisarPessoa;

    @FXML
    private Button buttonRemover;

    // Pessoa
    @FXML
    private TextField textFieldCodigoPessoa;

    @FXML
    private TextField textFieldNomePessoa;

    @FXML
    private TextField textFieldCpf;

    @FXML
    private DatePicker datePickerInicio;

    @FXML
    private DatePicker datePickerFinal;

    @FXML
    private TableView<Pessoa> tableViewInformacoesPessoa;

    @FXML
    private TableColumn<Pessoa, Long> tableColumnCodigoPessoa;

    @FXML
    private TableColumn<Pessoa, String> tableColumnNomePessoa;

    @FXML
    private TableColumn<Pessoa, String> tableColumnCpf;

    @FXML
    private TableColumn<Pessoa, String> tableColumnNascimento;

    // Vacina
    @FXML
    private TextField textFieldCodigo;

    @FXML
    private TextField textFieldNome;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TableView<Vacina> tableViewInformacoes;

    @FXML
    private TableColumn<Vacina, Long> tableColumnCodigo;

    @FXML
    private TableColumn<Vacina, String> tableColumnNome;

    @FXML
    private TableColumn<Vacina, String> tableColumnDescricao;

    // variaveis
    private Stage stageTelaSecundaria;
    private TelaSecundariaController controllerTelaSecundaria;
    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private String codigo;
    // private String descricao;
    // private String nome;
    private Vacina vacinaTable = new Vacina();
    private Pessoa pessoaTable = new Pessoa();
    // criar as vacinas para poder editar e remover
    public List<Vacina> vacinas;
    public Vacina vacina;
    // criar as pessoas para poder editar e remover
    public List<Pessoa> pessoas;
    public Pessoa pessoa;
    // receber a conexao daoFactory do App
    public DAOFactory daoFactory;

    public TelaPrincipalController() {
        System.out.println("TelaPrincipalController criado");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // associando as colunas com os respectivos atributos da classe Cliente
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));
        
        // associando as colunas com os respectivos atributos da classe Pessoa
        tableColumnCodigoPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, Long>("codigo"));
        tableColumnNomePessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("nome"));
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("cpf"));
        tableColumnNascimento.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("dataNascimento"));

        /*
         * Jeito de formatar visto na internet.
         * Eu insiro no banco no formato br, ele salva no formato americano por padrão. Aí pra mostrar so converter pro br de novo
         * 1- formato usando padrao americano
         * 2- formato usando padrao br
         */

        tableColumnNascimento.setCellFactory(new Callback<TableColumn<Pessoa, String>, TableCell<Pessoa, String>>() {
        @Override
        public TableCell<Pessoa, String> call(TableColumn<Pessoa, String> param) {
            return new TableCell<Pessoa, String>() {
                private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(dateFormat.format(java.sql.Date.valueOf(item)));
                    }
                }
            };
        }
    });

        /*
         * Settando para que se aceite apenas numeros nos inputs. URL:
         * https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
         */

        textFieldCodigo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldCodigo.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textFieldCodigoPessoa.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldCodigoPessoa.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // carregar a cena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaSecundaria.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stageTelaSecundaria = new Stage();
            stageTelaSecundaria.setScene(scene);
            stageTelaSecundaria.setTitle("Vacinadbc - Tela 2");
            stageTelaSecundaria.getIcons().add(new Image(getClass().getResourceAsStream("/images/java.png")));
            controllerTelaSecundaria = loader.getController();
            // controllerTelaSecundaria.setConexaoDaoFactory(daoFactory);
            controllerTelaSecundaria.setTextFieldNonEditable(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FALHA AQYUUUUU");
        }
        

        tableViewInformacoes.getSortOrder().add(tableColumnCodigo);
        tableColumnCodigo.setSortType(TableColumn.SortType.ASCENDING);
        tableViewInformacoes.getItems().addAll(pesquisarVacinas(true));
        tableViewInformacoes.sort();

        tableViewInformacoesPessoa.getItems().addAll(pesquisarPessoas(true));
    }

    public List<Vacina> pesquisarVacinas(Boolean pesquisarTodas) {

        if (pesquisarTodas) {
            try {
                daoFactory = new DAOFactory();
                daoFactory.abrirConexao();
                VacinaDAO dao = daoFactory.criarVacinaDAO();
                vacinas = dao.buscarTodas();
            } catch (SQLException ex) {
                DAOFactory.mostrarSQLException(ex);
            } finally {
                // daoFactory.fecharConexao();
            }
            // return vacinas;

        } else {
            try {
                daoFactory = new DAOFactory();
                daoFactory.abrirConexao();
                VacinaDAO dao = daoFactory.criarVacinaDAO();
                String codigo = textFieldCodigo.getText();
                String descricao = textAreaDescricao.getText();
                String nome = textFieldNome.getText();
                vacinas = dao.buscar(codigo, descricao, nome);
            } catch (SQLException ex) {
                DAOFactory.mostrarSQLException(ex);
            } catch (NumberFormatException n) {
                Alert numeroCodigo = new Alert(Alert.AlertType.INFORMATION);
                numeroCodigo.setTitle(codigo);
                numeroCodigo.setHeaderText("Só é possível digitar numero em Codigo");
                numeroCodigo.show();
            }   finally {
                // daoFactory.fecharConexao();
            }
            // return vacinas;
        }    
        return vacinas;    
    }

    public void pegarVacina(List<Vacina> vacinas) {
        for (Vacina pegarVacina : vacinas) {
            vacina = pegarVacina;
        }
    }

    public List<Pessoa> pesquisarPessoas(Boolean pesquisarTodas) {
        if (pesquisarTodas) {
            try {
                DAOFactory daoFactory = new DAOFactory();
                daoFactory.abrirConexao();
                PessoaDAO dao = daoFactory.criarPessoaDAO();
                pessoas = dao.buscarTodas();
            } catch (SQLException ex) {
                DAOFactory.mostrarSQLException(ex);
            } finally {
                // daoFactory.fecharConexao();
            }
            
        }
            try {
                DAOFactory daoFactory = new DAOFactory();
                daoFactory.abrirConexao();
                PessoaDAO dao = daoFactory.criarPessoaDAO();
                String codigo = textFieldCodigoPessoa.getText();
                String nome = textFieldNomePessoa.getText();
                String cpf = textFieldCpf.getText();
                pessoas = dao.buscar(codigo, nome, cpf, dataInicio, dataFinal);
            } catch (SQLException ex) {
                DAOFactory.mostrarSQLException(ex);
            } finally {
                // daoFactory.fecharConexao();
            }          
        return pessoas;
    }

    @FXML
    void buttonPesquisarClicado(ActionEvent event) {
        /*
         * Arrumar o tableView para apresentar na ordem de codigo
         * 
         */
        Alert vacinasTableview = new Alert(Alert.AlertType.INFORMATION);
        // verificar se os textFields estao totalmente vazios para pesquisar todas as vacinas
        if (textFieldCodigo.getText().isEmpty() && textAreaDescricao.getText().isEmpty() && textFieldNome.getText().isEmpty()) {
            vacinas = pesquisarVacinas(true);
            // System.out.println("ESSE É O TAMANHO DE VACINAS EM PESQUISAR SEM CAMPO PREENCHIDO: "+ vacinas.size());

            tableViewInformacoes.getItems().clear();
            tableViewInformacoes.getSortOrder().add(tableColumnCodigo);
            tableColumnCodigo.setSortType(TableColumn.SortType.ASCENDING);
            tableViewInformacoes.getItems().addAll(vacinas);
            tableViewInformacoes.sort();
            
        } else { // se não estiver vazio:
            vacinas = pesquisarVacinas(false);
            // System.out.println("ESSE É O TAMANHO DE VACINAS EM PESQUISAR COM UM CAMPO PREENCHIDO: "+ vacinas.size());
            if (vacinas.size() == 0) {
                vacinasTableview.setTitle("Tableview vacinas");
                vacinasTableview.setHeaderText("Nenhuma vacina encontrada...");
                vacinasTableview.showAndWait();
            } else {
                tableViewInformacoes.getItems().clear();
                tableViewInformacoes.getSortOrder().add(tableColumnCodigo);
                tableColumnCodigo.setSortType(TableColumn.SortType.ASCENDING);
                tableViewInformacoes.getItems().addAll(vacinas);
                tableViewInformacoes.sort();
            }
        }
    }

    @FXML
    void buttonNovaClicado(ActionEvent event) {
        // add nova vacina no banco --> da pasta de testes o add
        /*
         * settar o editar como false so pra garantir V
         * settar os labels como vazios (está mantendo o que havia preenchido antes)
         * 
         */
        controllerTelaSecundaria.setEditar(false);
        controllerTelaSecundaria.setTextFieldVisible(false);
        controllerTelaSecundaria.limpar();
        //controllerTelaSecundaria.setConexaoDaoFactory(daoFactory);
        stageTelaSecundaria.showAndWait();
        controllerTelaSecundaria.setTextFieldVisible(true);
        buttonPesquisarClicado(event);
    }

    @FXML
    void buttonEditarClicado(ActionEvent event) {
        // editar vacina do bdd --> da pasta de testes o update
        /*
        * Explicação:
        * pega a vacina que voce clicou para editar e abre a tela secundaria pra fazer a edição
        * lembrando de como usar alert : https://aprendendo-javafx.blogspot.com/2015/03/as-janelas-de-dialogos-do-javafx.html
        */

        Alert qtdVacinaInfo = new Alert(Alert.AlertType.INFORMATION);
        controllerTelaSecundaria.setEditar(true);

        vacinas = tableViewInformacoes.getSelectionModel().getSelectedItems();
        // System.out.println("ESSE É O TAMANHO DE VACINAS EM EDITAR: " + vacinas.size());        
        
        if (vacinas.size() == 1) {
            pegarVacina(vacinas);
            // System.out.println("ESSA É A VACINA DEPOIS DE TER SIDO PEGA" + vacina);
            // codigo = String.valueOf(vacina.getCodigo()); // salvando na variavel globar pq acho que estava usando em algum lugar. Arrumar depois se n estiver
            // nome = vacina.getNome();
            // descricao = vacina.getDescricao();
            controllerTelaSecundaria.setVacina(vacina);
            stageTelaSecundaria.showAndWait();
            buttonPesquisarClicado(event);
        } else {
            qtdVacinaInfo.setTitle("Editar vacina");
            qtdVacinaInfo.setHeaderText("Voce nao selecionou nenhuma vacina");
            qtdVacinaInfo.showAndWait();
        }
    }

    @FXML
    void buttonRemoverClicado(ActionEvent event) {
        Alert qtdVacinaInfo = new Alert(Alert.AlertType.INFORMATION);
        // https://acervolima.com/javafx-alerta-com-exemplos/
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        vacinas = tableViewInformacoes.getSelectionModel().getSelectedItems();
        
        if (vacinas.size() == 0) {
            qtdVacinaInfo.setTitle("Remover vacina");
            qtdVacinaInfo.setHeaderText("Você não selecionou uma vacina para remover");
            qtdVacinaInfo.showAndWait();
        } else if (vacinas.size() == 1) {
            confirmacao.setContentText("Quer mesmo remover?");
            Optional<ButtonType> buttonType = confirmacao.showAndWait(); 
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                pegarVacina(vacinas);
                /*
                * Etapa de remover a vacina do banco
                */
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    daoFactory.abrirConexao();
                    VacinaDAO dao = daoFactory.criarVacinaDAO();
                    dao.remover(vacina);
                    textFieldCodigo.setText("");
                    textAreaDescricao.setText("");
                    textFieldNome.setText("");
                    buttonPesquisarClicado(event);
                } catch (SQLException ex) {
                    DAOFactory.mostrarSQLException(ex);
                } finally {
                    daoFactory.fecharConexao();
                }
            }
        } else {
            // vai que de alguma forma ele ta com mais de uma vacina :)
            qtdVacinaInfo.setTitle("Remover vacina");
            qtdVacinaInfo.setHeaderText("Selecione apenas uma vacina para remover");
            qtdVacinaInfo.showAndWait();
        }
    }

    @FXML
    void buttonPesquisarPessoaClicado(ActionEvent event) {
        // try {
        //     DAOFactory daoFactory = new DAOFactory();
        //     daoFactory.abrirConexao();
        //     PessoaDAO dao = daoFactory.criarPessoaDAO();
        //     pessoas = dao.buscarTodas();
            
            // verificar se os textFields estao totalmente vazios para pesquisar todas as vacinas
            if (textFieldCodigoPessoa.getText().isEmpty() && textFieldNomePessoa.getText().isEmpty() && textFieldCpf.getText().isEmpty()
                && datePickerInicio.getValue() == null && datePickerFinal.getValue() == null) {
                // System.out.println("Campos estao vazios");
                pesquisarPessoas(true);
                tableViewInformacoesPessoa.getItems().clear();
                tableViewInformacoesPessoa.getItems().addAll(pessoas);
            } else { // se não estiver vazio:
                // System.out.println("UM DOS CAMPOS N ESTA VAZIO");
                pesquisarPessoas(false);    
                tableViewInformacoesPessoa.getItems().clear();
                tableViewInformacoesPessoa.getItems().addAll(pessoas);
            }
        // } catch (SQLException ex) {
        //     DAOFactory.mostrarSQLException(ex);
        // } finally {
        //     daoFactory.fecharConexao();
        // }
    }

    @FXML
    void buttonCriarAplicacaoClicado(ActionEvent event) {
        // pegar do tableview -> https://stackoverflow.com/questions/17388866/getting-selected-item-from-a-javafx-tableview
        List<Vacina> vacinas = tableViewInformacoes.getSelectionModel().getSelectedItems();
        List<Pessoa> pessoas = tableViewInformacoesPessoa.getSelectionModel().getSelectedItems();

        Alert criarAplicacaoAlert = new Alert(Alert.AlertType.INFORMATION);
        if (vacinas.size() == 1 && pessoas.size() == 1) {
            try {
                // pegar a vacina e a pessoa e salvar em um unico array
                try {
                    for (Vacina pegarVacina : vacinas) {
                        vacinaTable = pegarVacina;
                    }
                    for (Pessoa pegarPessoa : pessoas) {
                        pessoaTable = pegarPessoa;
                    }
                    // System.out.println("VACINA DEPOIS DO FOR" + vacinaTable);
                    // System.out.println("PESSOA DEPOIS DO FOR" + pessoaTable);
                } catch (NullPointerException e) {
                    System.err.println("ERRO DE NULLPOINTEREXCEPTION");
                }

                DAOFactory daoFactory = new DAOFactory();
                daoFactory.abrirConexao();
                AplicacaoDAO dao = daoFactory.criarAplicacaoDAO();
                dao.gravar(vacinaTable, pessoaTable);
                criarAplicacaoAlert.setHeaderText("Aplicação criada com sucesso!");
                criarAplicacaoAlert.show();
                } catch (SQLException ex) {
                    DAOFactory.mostrarSQLException(ex);
                    criarAplicacaoAlert.setHeaderText("Erro ao criar a aplicação");
                    criarAplicacaoAlert.show();
                } finally {
                    daoFactory.fecharConexao();
                } 
        } else {
            criarAplicacaoAlert.setTitle("Criar aplicação");
            criarAplicacaoAlert.setHeaderText("Selecione uma vacina e uma pessoa...");
            criarAplicacaoAlert.show();
        }
    }

    @FXML
    void obterDataInicio(ActionEvent event) {
        dataInicio = datePickerInicio.getValue();
        // System.out.println(dataInicio);
    }

    @FXML
    void obterDataFim(ActionEvent event) {
        dataFinal = datePickerFinal.getValue();
        // System.out.println(dataFinal);
    }

    // funções que serão usadas

    // recebe do App a conexão --> acabei nem usando
    public void setConexaoDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

}