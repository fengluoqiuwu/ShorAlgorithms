package GUI.mainPage;

import GUI.Enum.DecryptionPage;
import GUI.Enum.EncryptionPage;
import GUI.decryptionPage.decryptionPageController;
import GUI.encryptionPage.encryptionPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static GUI.GUIUtil.StageUtil.changeViews;
import static GUI.GUIUtil.StageUtil.resetLocation;
import static main.Main.getPrimaryStage;

public class mainPageController {
    // 页面信息
    Stage stage;

    // 加密页面按钮
    @FXML
    private Button RSAButton;
    @FXML
    private Button DSAButton;
    @FXML
    private Button ECDSAButton;

    // 破译页面按钮
    @FXML
    private Button AttackRSA;
    @FXML
    private Button AttackElGamal;
    @FXML
    private Button AttackECC;

    //页面存在
    private boolean isEncryptionPageExist=false;
    private boolean isDecryptionPageExist=false;

    //页面信息
    private EncryptionPage encryptionPage=null;
    private DecryptionPage decryptionPage=null;
    private Stage encryptionStage=null;
    private Stage decryptionStage=null;
    private encryptionPageController encryptionController=null;
    private decryptionPageController decryptionController=null;

    //页面构造函数
    @FXML
    public void initialize() {
        stage = getPrimaryStage();
    }

    //打开页面执行函数
    @FXML
    private void doRSAButton(){
        openEncryptionPage(EncryptionPage.RSA);
    }
    @FXML
    private void doDSAButton(){
        openEncryptionPage(EncryptionPage.DSA);
    }
    @FXML
    private void doECDSAButton(){
        openEncryptionPage(EncryptionPage.ECDSA);
    }
    @FXML
    private void doRSAAttack(){
        openDecryptionPage(DecryptionPage.RSA);
    }
    @FXML
    private void doElgamalAttack(){
        openDecryptionPage(DecryptionPage.ElGamal);
    }
    @FXML
    private void doECCAttack(){
        openDecryptionPage(DecryptionPage.ECC);
    }

    //打开页面调用的函数
    private void openEncryptionPage(EncryptionPage encryption){
        if(isEncryptionPageExist){
            if(encryption!=encryptionPage){
                encryptionPage=encryption;
                encryptionController.setPage(encryption);
                encryptionController.flush();
            }
            resetLocation(encryptionStage);
        }
        else{
            isEncryptionPageExist=true;
            encryptionPage=encryption;
            encryptionStage=new Stage();

            encryptionController=changeViews(encryptionStage,"/GUI/encryptionPage/encryptionPage.fxml");
            encryptionController.setPage(encryption);
            encryptionController.flush();

            encryptionStage.setOnHiding(e->{
                isEncryptionPageExist=false;
                encryptionStage.close();
                encryptionStage=null;
            });
            encryptionStage.setResizable(false);
            encryptionStage.show();
            encryptionStage.setTitle("Encryption");
            resetLocation(encryptionStage);
        }
    }
    private void openDecryptionPage(DecryptionPage decryption){
        if(isDecryptionPageExist){
            if(decryption!=decryptionPage){
                decryptionPage=decryption;
                decryptionController.setPage(decryption);
                decryptionController.flush();
            }
            resetLocation(decryptionStage);
        }
        else{
            isDecryptionPageExist=true;
            decryptionPage=decryption;
            decryptionStage=new Stage();

            decryptionController=changeViews(decryptionStage,"/GUI/decryptionPage/decryptionPage.fxml");
            decryptionController.setPage(decryption);
            decryptionController.flush();

            decryptionStage.setOnHiding(e->{
                isDecryptionPageExist=false;
                decryptionStage.close();
                decryptionStage=null;
            });
            decryptionStage.setResizable(false);
            decryptionStage.show();
            decryptionStage.setTitle("Decryption");
            resetLocation(decryptionStage);
        }
    }
}
