package poov.vacinadbcjavafx;

import java.io.IOException;
import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // criar e guardar a tela A e o seu controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaPrincipal.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Vacinadbc - Tela 1");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/java.png")));
            // salvar tela A
            // TelaPrincipalController principal = loader.getController();
            // mandando a conexão daoFactory 
            // principal.setConexaoDaoFactory(daoFactory);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo FXML: " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("pt", "BR"));
        launch();
    }

}