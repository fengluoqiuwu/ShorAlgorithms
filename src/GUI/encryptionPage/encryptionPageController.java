package GUI.encryptionPage;

import GUI.Enum.EncryptionPage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.encryption.DSA.ElGamalService;
import project.encryption.EncryptionService;
import project.encryption.RSA.RSAService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

import static GUI.GUIUtil.StageUtil.resetLocation;

public class encryptionPageController {
    @FXML
    private Button generateKeyButton;
    @FXML
    private Button pathChoosingButton;
    @FXML
    private ChoiceBox<Integer> keyChoiceBox;
    @FXML
    private TextArea publicKeyArea;
    @FXML
    private TextArea privateKeyArea;
    @FXML
    private Button decryptionButton;
    @FXML
    private Button encryptionButton;
    @FXML
    private TextArea textArea;

    private EncryptionPage encryptionPage=EncryptionPage.RSA;
    private EncryptionService service=null;

    Stage pathChoosingStage;
    boolean isPathChoosingPageExist=false;
    File file=null;
    String text="";

    @FXML
    private void initialize(){
        pathChoosingStage=new Stage();
        pathChoosingStage.setOnHiding(e->{
            isPathChoosingPageExist=false;
            pathChoosingStage.close();
        });

        flush();
    }
    public void setPage(EncryptionPage encryptionPage){
        this.encryptionPage = encryptionPage;
    }
    public void flush(){
        resetChoiceBox();
        try{
            switch(encryptionPage){
                case RSA:
                    service=new RSAService();
                    break;
                case DSA:
                    service=new ElGamalService();
                    break;
                case ECDSA:
                    service=null;
            }
            generateKey();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    private void resetChoiceBox(){
        keyChoiceBox.getItems().clear();
        switch (encryptionPage){
            case RSA-> keyChoiceBox.getItems().addAll(1024,2048,3072,4096);
            case DSA-> keyChoiceBox.getItems().addAll(1024,2048);
            case ECDSA-> keyChoiceBox.getItems().addAll(2);
        }
        keyChoiceBox.getSelectionModel().selectFirst();
    }
    @FXML
    private void generateKey() throws NoSuchAlgorithmException {
        service.setKeySize(keyChoiceBox.getValue());

        service.generateKeyPair();
        publicKeyArea.setText(service.getPublicKey());
        privateKeyArea.setText(service.getPrivateKey());
    }
    @FXML
    private void pathChoosing(){
        file=null;

        if(!isPathChoosingPageExist){
            isPathChoosingPageExist=true;

            Platform.runLater(() -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("打开文件");

                fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                file = fileChooser.showOpenDialog(pathChoosingStage);

                if(file!=null){
                    try{
                        text= Files.readString(file.toPath());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                isPathChoosingPageExist=false;
                textArea.setText(text);
            });
        }
        else{
            resetLocation(pathChoosingStage);
        }
    }
    @FXML
    private void doEncryption(){
        text=textArea.getText();
        try{
            text=service.encrypt(text);
        }catch (Exception e){
            e.printStackTrace();
        }
        textArea.setText(text);

        if(file!=null){
            try{
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(text);
                bw.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void doDecryption(){
        text=textArea.getText();
        try{
            text=service.decrypt(text);
        }catch (Exception e){
            e.printStackTrace();
        }
        textArea.setText(text);

        if(file!=null){
            try{
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(text);
                bw.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
