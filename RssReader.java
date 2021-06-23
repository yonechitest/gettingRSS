import rssreader.Converter;
import rssreader.Output;
import rssreader.Parse;

public class RssReader {
    //コンパイルコマンド:javac -encoding エンコーディング名 RssReader.java
    //エンコーディング名にはプログラムを実行している環境で使用しているエンコーディングをを指定してください。
    public static void main(String[] args) {
        String path        = args[0];//取り込みRSSフィードまたはファイルのパス
        String convType    = args[1];//変換処理の種類
        //第３引数がある時は入力値を設定、設定されていない時は空文字を設定する。
        String outFaleName = "";
        if(args.length == 3) outFaleName = args[2];

        Output op = new Output(outFaleName);
        Parse par = new Parse(path, convType);

        //第１引数のURLスキームが"http://"か"https://"の時
        if ( path.startsWith( "http://" ) || path.startsWith( "https://" ) ) {
            StringBuilder convertedXMLText = par.parseXML();
            op.output(convertedXMLText);
        }else {
            StringBuilder convertedFileText = par.parseTextFile();
            op.output(convertedFileText);
        }
    }
}