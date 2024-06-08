package GUI.decryptionPage.rsa;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class attackRSAController {
    @FXML
    private TextField input;
    @FXML
    private TextArea output;
    @FXML
    private Button doAttack;
    @FXML
    private Label tips;

    private Integer n=null;

    private Thread pythonProgram= new Thread(() -> {
        int p=0,q=0;
        Platform.runLater(()->{
            doAttack.setDisable(true);
        });

        try {
            String pythonScript = "/PythonProgram/ShorAlgorithms/shorForFactoringProblem.py";

            // 创建ProcessBuilder对象，并传入Python脚本命令
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);

            // 启动Python脚本
            Process process = processBuilder.start();

            // 获取Python脚本的输出流（用于向脚本输入数据）
            OutputStream outputStream = process.getOutputStream();

            // 向Python脚本发送数据
            String inputData = n.toString()+'\n';
            outputStream.write(inputData.getBytes());
            outputStream.flush(); // 清空缓冲区

            // 获取Python脚本的输入流（用于读取脚本返回的数据）
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            process.waitFor();

            // 读取Python脚本的返回数据
            String line;
            StringBuilder outputData = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                outputData.append(line).append("\n");
            }

            Platform.runLater(()->{
                output.setText(outputData.toString());
            });

            // 关闭流
            outputStream.close();
            reader.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            doAttack.setDisable(false);
            output.setText(p+"\n"+q);
        });
    });

    @FXML
    private void doAttack(){
        try{
            n=Integer.parseInt(input.getText());
        }catch(NumberFormatException e){
            tips.setText("Please enter a number");
            return;
        }

        if(n<=1){
            tips.setText("Please enter a valid number");
            return;
        }

        tips.setText("");

        pythonProgram.start();
    }
}
