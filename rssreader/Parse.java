package rssreader;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.lang.StringBuilder;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {

    private String path;//第1引数 取り込みRSSフィードまたはファイルのパス
    private String convType;//第2引数 変換処理の種類

    //コンストラクタ
    public Parse(String path, String convType){
        this.path = path;
        this.convType = convType;
    }

    //RSSを解析するとき
    public StringBuilder parseXML() {
        StringBuilder outParsedXML = new StringBuilder();
        Converter conv = new Converter(this.convType);

        try {
            final URL url = new URL(this.path);
            //HTTP通信開始
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            //HTTPステータスコード取得
            int responseCode = huc.getResponseCode();
            if (responseCode != 200) throw new IOException();
        }catch (IOException e) {
            System.out.println("指定したURLは無効なURLかアクセス権限がありません");
            System.exit(0);
        }
        
        try {
            // HTTP通信先のRSSフィードをDOM化する
            DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder         builder = factory.newDocumentBuilder();
            Document                document = builder.parse(this.path);
            Element                 root = document.getDocumentElement();

            //トップレベルのタグがrssかどうか判別
            if (root.getNodeName().toLowerCase() != "rss") throw new SAXException();

            // タイトル、本文の情報を配列に格納する
            NodeList                item_list = root.getElementsByTagName("item");


            // タイトル,本文の文字列を取り出す
            for (int i = 0; i <item_list.getLength(); i++) {
                Element  element = (Element)item_list.item(i);
                NodeList item_title = element.getElementsByTagName("title");
                NodeList item_content  = element.getElementsByTagName("description");
                String titleString = item_title.item(0).getFirstChild().getNodeValue();
                String contentString = item_content.item(0).getFirstChild().getNodeValue();

                contentString = contentString.replaceAll("\n", "");
                
                //変換処理
                conv.convertXML(titleString, contentString);

                outParsedXML.append("タイトル:" + conv.getOutputArticle()[0] + "\n");
                outParsedXML.append("本文:" + conv.getOutputArticle()[1] + "\n\n");
            }

        }catch (SAXException e) {
            System.out.println("対象URLはRSSフィードではありません。");
            System.exit(0);
        }catch (ParserConfigurationException e) {
            System.out.println("Parser Configuration Exception");
            System.exit(0);
        }catch (IOException e) {
            System.out.println("IO Exception");
            System.exit(0);
        }
        return outParsedXML;
    }

    //ファイルを解析するとき
    public StringBuilder parseTextFile() {
        StringBuilder sbOut = null;

        try{
            File file = new File("file/in/"+this.path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            Converter conv = new Converter(this.convType);

            String line;
            String lineType;

            //テキスト内を１行ずつ解析
            while ((line = br.readLine()) != null) {

                //取得した行の最初の文字が"title:"の時
                if(line.indexOf("title:") == 0 ){
                    line = line.replaceAll("title: ", "");
                    lineType = "title";
                //取得した行の最初の文字が"body:"の時
                }else if(line.indexOf("body:") == 0 ){
                    line = line.replaceAll("body: ", "");
                    lineType = "body";
                //どちらでもない時は出力対象にしない。
                }else{
                    continue;
                }

                //変換処置
                conv.convertFile(line, lineType);

                sb.append(conv.getConvertedLine() + "\n");
                if(lineType  == "body" ) sb.append("\n");
            }
            br.close();
            fr.close();
            
            sbOut = sb;

        }catch(FileNotFoundException e){
            System.out.println("指定された入力ファイル、URLのパスが無効です");
            System.exit(0);
        }catch(IOException e){
            System.out.println(e);
            System.exit(0);
        }
        return sbOut;
    }
}