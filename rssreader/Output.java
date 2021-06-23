package rssreader;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Output {

    //出力時のファイル名
    private String fileName;

    //コンストラクタ
    public Output(String fileName){
        this.fileName = fileName;
        if(this.fileName != ""){
             String lastStr = this.fileName.substring(this.fileName.length() - 4);
            if(!lastStr.equals(".txt")){
                System.out.println("ファイルの拡張子は.txtである必要があります。");
                System.exit(0);
            }
        }
    }

    //ファイル出力処理
    public void output(StringBuilder outData) {

        //第3引数（ファイル名）が設定されるとき
        if(this.fileName != ""){
            try{
                //ファイル生成時にファイル名に付与するタイムスタンプを作成
                Date dateString = new Date();
                SimpleDateFormat formatType = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileTimestamp  = formatType.format(dateString); 

                //出力ファイル名を生成
                String faleName = this.fileName.replaceAll(".txt", "");
                String createFileName = faleName + "_" + fileTimestamp + ".txt";
                //ファイル書き込み処理
                FileWriter fw = new FileWriter("file/out/"+createFileName);
                fw.write(outData.toString());
                fw.close();
                System.out.println(createFileName + "を出力しました。");

            }catch(FileNotFoundException e){
                System.out.println("指定されたファイルが見つかりません。");
                System.exit(0);
            }catch(IOException e){
                System.out.println(e);
                System.exit(0);
            }
        //第3引数（ファイル名）が設定されてない時標準出力する
        }else{
            System.out.println(outData.toString());
        }
    }

}