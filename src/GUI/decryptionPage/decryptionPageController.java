package GUI.decryptionPage;

import GUI.Enum.DecryptionPage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import static GUI.GUIUtil.StageUtil.loadScene;
import static GUI.GUIUtil.StageUtil.newRoot;

public class decryptionPageController {
    @FXML
    private AnchorPane Anchor;

    private DecryptionPage decryptionPage=null;

    public void setPage(DecryptionPage decryptionPage){
        this.decryptionPage = decryptionPage;
    }

    public void flush(){
        switch(decryptionPage){
            case RSA->{
                FXMLLoader loader = loadScene("/GUI/decryptionPage/rsa/attackRSA.fxml");
                Parent root = newRoot(loader);

                Anchor.getChildren().clear();
                Anchor.getChildren().add(root);
            }
            case ElGamal -> {
                FXMLLoader loader = loadScene("/GUI/decryptionPage/elgamal/attackElGamal.fxml");
                Parent root = newRoot(loader);

                Anchor.getChildren().clear();
                Anchor.getChildren().add(root);
            }
            case ECC -> {
                FXMLLoader loader = loadScene("/GUI/decryptionPage/ecc/attackECC.fxml");
                Parent root = newRoot(loader);

                Anchor.getChildren().clear();
                Anchor.getChildren().add(root);
            }
        }
    }
}
