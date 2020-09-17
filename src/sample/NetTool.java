package sample;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NetTool {
    //    private static Logger logger = Logger.getLogger(NetTool.class);
    public static String REQUEST_GET = "get";
    public static String REQUEST_POST = "post";

    /**
     * @param url
     * @param timeout_ms
     * @param retryTimes
     * @return
     */
    public static Document getDocByFakeBrowser(String url, int timeout_ms, int retryTimes) {
        return getDocByFakeBrowser(url, timeout_ms, retryTimes, null, null);
    }

    public static Document getDocByFakeBrowser(String url, int timeout_ms, int retryTimes, Map<String, String> params) {
        return getDocByFakeBrowser(url, timeout_ms, retryTimes, params, null);
    }

    public static Document getDocByFakeBrowser(String url, int timeout_ms, int retryTimes, Map<String, String> params,
                                               Map<String, String> cookies) {
        Document doc = null;
        retryTimes = retryTimes > 0 ? retryTimes : 1;
        timeout_ms = timeout_ms > 0 ? timeout_ms : 2000;
        for (int i = 0; i < retryTimes; i++) {
            try {
                if (Tool.validStr(url)) {
                    doc = Jsoup.connect(url).timeout(timeout_ms)
                            .userAgent(
                                    "Mozilla/5.0?(Windows?NT?6.1;?WOW64)?AppleWebKit/537.31?(KHTML,?like?Gecko)?Chrome/26.0.1410.64?Safari/537.31")
                            .data(params).cookies(cookies).get();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return doc;
    }

    public static Document postDocByFakeBrowser(String url, int timeout_ms, int retryTimes, Map<String, String> params,
                                                Map<String, String> cookies) {
        Document doc = null;
        retryTimes = retryTimes > 0 ? retryTimes : 1;
        timeout_ms = timeout_ms > 0 ? timeout_ms : 2000;
        for (int i = 0; i < retryTimes; i++) {
            try {
                if (Tool.validStr(url)) {
                    doc = Jsoup.connect(url).timeout(timeout_ms)
                            .userAgent(
                                    "Mozilla/5.0?(Windows?NT?6.1;?WOW64)?AppleWebKit/537.31?(KHTML,?like?Gecko)?Chrome/26.0.1410.64?Safari/537.31")
                            .data(params).cookies(cookies).post();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return doc;
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static int downLoadFromUrl(String urlStr, String fileName, String savePath, int timeout) throws IOException {
        // 文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        // 首先判断文件是否已经存在
        File file = new File(saveDir + File.separator + fileName);
        if (file.exists()) {
            System.out.println("文件已存在....");
            return -1;
        }
        URL url = new URL(urlStr);
        System.out.println("开始下载....");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeout);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组
        byte[] getData = Tool.readInputStream(inputStream);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("下载完成!.." + "[" + fileName + "]");
        return 1;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\r\n";
            }
        } catch (Exception e) {
            System.out.println("GET请求出现异常" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, Map<String, String> param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + mapToArgsString(param);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.setConnectTimeout(10000);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\r\n";
            }
        } catch (Exception e) {
            System.out.println("GET请求出现异常" + e);
            e.printStackTrace();
            return result;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;

    }


    /**
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\r\n";
            }
        } catch (Exception e) {
            System.out.println("POST 请求出现异常" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, Map param) {
        return sendPost(url, mapToArgsString(param));
    }

    public static String mapToArgsString(Map<String, String> conditions) {
        StringBuffer result = new StringBuffer();
        Set<String> keySet = conditions.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = conditions.get(key);
            result.append(key + "=" + value);
            if (it.hasNext()) {
                result.append("&");
            }
        }
        return result.toString();
    }

}
