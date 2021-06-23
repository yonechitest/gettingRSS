package rssreader;
import java.util.Arrays;

public class Converter {

    //第2引数 変換処理の種類
    private String[] convProcess;

    //getter/setter用クラス変数
    private String[] outputArticle;
    private String convertedLine;

    //変換時の設定値
    final int SPLIT_TITLE_LENGTH_10 = 10;//タイトルをカットする文字数
    final int SPLIT_CONTENT_LENGTH_30 = 30;//本文をカットする文字数
    final String BEFORE_CONVERT_CONTENT = "ユーザベース";//変換対象の文字列
    final String AFTER_CONVERT_CONTENT = "UZABASE";//変換後の文字列

    //コンストラクタ
    public Converter(String commandArg2){
        this.convProcess = commandArg2.split(",");
        if(!Arrays.asList(this.convProcess).contains("cut") && !Arrays.asList(this.convProcess).contains("cha")){
            System.out.println("第２引数はカンマで区切り、cutかchaのどちらかが含まれている必要があります。");
            System.exit(0);
        }
    }

    //getter/setter メソッド
    public String[] getOutputArticle(){
        return outputArticle;
    }
    public void setOutputArticle(String[] outputArticle){
        this.outputArticle = outputArticle;
    }
    public String getConvertedLine(){
        return convertedLine;
    }
    public void setConvertedLine(String convertedLine){
        this.convertedLine = convertedLine;
    }

    //解析対象がRSSフィードの時
    public void convertXML(String title , String content) {
        String[] outputArticle = new String[2];
        outputArticle[0] = title;
        outputArticle[1] = content;

        //文字列カット処理の時
        if(Arrays.asList(this.convProcess).contains("cut")){
            outputArticle[0] = outputArticle[0].substring(0, SPLIT_TITLE_LENGTH_10);//10文字にカット
            outputArticle[1] = outputArticle[1].substring(0, SPLIT_CONTENT_LENGTH_30);//30文字にカット
        }
        //置換処理の時
        if(Arrays.asList(this.convProcess).contains("cha")){
            outputArticle[1] = outputArticle[1].replace(BEFORE_CONVERT_CONTENT, AFTER_CONVERT_CONTENT);//文字列"ユーザベース"から"UZABASE"に変換


        }
        setOutputArticle(outputArticle);
    }

    //解析対象がテキストファイルの時
    public void convertFile(String fetchedLine, String lineType) {
        String convertingString = fetchedLine;
        
        //文字列カット処理の時
        if(Arrays.asList(this.convProcess).contains("cut")){
            if(lineType  == "title" )  convertingString = convertingString.substring(0, SPLIT_TITLE_LENGTH_10);//10文字にカット
            if(lineType  == "body"  )  convertingString = convertingString.substring(0, SPLIT_CONTENT_LENGTH_30);//30文字にカット
        }
        //置換処理の時
        if(Arrays.asList(this.convProcess).contains("cha")){
            //文字列"ユーザベース"から"UZABASE"に変換
            if(lineType  == "body" )   convertingString = convertingString.replace(BEFORE_CONVERT_CONTENT, AFTER_CONVERT_CONTENT);//文字列"ユーザベース"から"UZABASE"に変換
        }
        setConvertedLine(convertingString);
    }
}