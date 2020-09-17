package sample;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: lane
 * @Date: 2019-02-19 16:00
 * @Description:
 * @Version 1.0.0
 */
public class PicCatcher {
    private static final String BASE_URL = "https://www.520mojing.com/";
    private static final String START_URL = "index.html";
    private static String REGION_ID_REG_EXP_FIRST = "^\\d{2,12}(?=\\.html)";//后发零宽断言,抓取形如"*1101.html"里面的1101

    private static final int MAX_LEVEL = 1;
    private static String[] ELE_LIST = new String[MAX_LEVEL + 1];

    public static void main(String[] args) {
        PicCatcher dataCatcher = new PicCatcher();
        dataCatcher.start();
    }

    public void start() {
        ELE_LIST[0] = "tr.provincetr td a";
        ELE_LIST[1] = "tr.citytr td a";
//        ELE_LIST[2] = "tr.counrytr td a";
//        ELE_LIST[3] = "tr.towntr td a";
//        ELE_LIST[4] = "tr.villagetr td";
        JSONObject jsonObject = new JSONObject();
        get(START_URL, 0, jsonObject);
    }


    private Document getHtmlContent(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(BASE_URL + url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private String filter(int level, Element element, String key) {
        if (level == 0) {

        }
        return null;
    }
    private static final String KEY_ID = "id";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_NAME = "name";
    private static final String KEY_CHILDREN = "children";

    private void get(String url, int level, JSONObject regionJsonObj) {
        Document document = getHtmlContent("thread-39144-1-1.html");
        List<Element> element = document.getElementsByClass("zoom");
        element.removeIf((e) -> !e.hasAttr("aid"));
        /* https://img.520mojing.com/data/attachment/forum/202009/15/111004vl06qqqjlvlcmm2n.jpg */
        for (int i = 0; i < element.size(); i++) {
//            NetTool.downLoadFromUrl()
            System.out.println(element.get(i));
        }
        String parentId = findStrByRegEx(url, "");

    }


    public static String findStrByRegEx(String content, String regEx) {
        if (content == null || content.equals("")) {
            return null;
        }
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

}
