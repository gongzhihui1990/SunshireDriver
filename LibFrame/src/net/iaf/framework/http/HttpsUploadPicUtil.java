package net.iaf.framework.http;

import android.text.TextUtils;

import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @Description: 上传图片
 * @author: qianjx
 * @version: 1.0
 * @see
 */
public class HttpsUploadPicUtil extends BaseHttp {

    String multipart_form_data = "multipart/form-data";
    String twoHyphens = "--";
    String boundary = "7cd4a6d158c"; // 数据分隔符
    String lineEnd = "\r\n"; // The value is "\r\n" in Windows.

    /**
     * 构造DefaultHttpClient的方法
     */
    @Override
    protected DefaultHttpClient buildClient(int connectionPoolTimeout,
                                            int connectionTimeout, int socketTimeout) {
        return HttpClientHelper.buildHttpClient(connectionPoolTimeout,
                connectionTimeout, socketTimeout);
    }

    /**
     * 上传一张图片的方法
     *
     * @param uri
     * @param hmParams
     * @param imgkey
     * @param imgpath
     * @return
     * @throws NetworkException
     */
    private HttpUriRequest buildHttpUriRequest(String uri,
                                               HashMap<String, String> hmParams, String imgkey, String imgpath)
            throws NetworkException {

        HttpPost post = new HttpPost(uri);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 20);
        byte[] data = null;
        try {
            paramToUpload(bos, hmParams);
            post.setHeader("Content-Type", multipart_form_data + "; boundary="
                    + boundary);
            imageContentToUpload(bos, imgkey, imgpath);
            data = bos.toByteArray();
            bos.close();
            ByteArrayEntity formEntity = new ByteArrayEntity(data);
            post.setEntity(formEntity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
        return post;
    }

    /**
     * 上传两张图片的方法
     *
     * @param uri
     * @param hmParams
     * @param imgkey1
     * @param imgpath1
     * @param imgkey2
     * @param imgpath2
     * @return
     */
    private HttpUriRequest buildHttpUriRequest(String uri,
                                               HashMap<String, String> hmParams, String imgkey1, String imgpath1,
                                               String imgkey2, String imgpath2) {

        HttpPost post = new HttpPost(uri);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 20);
        byte[] data = null;
        try {
            paramToUpload(bos, hmParams);
            post.setHeader("Content-Type", multipart_form_data + "; boundary="
                    + boundary);
            imageContentToUpload(bos, imgkey1, imgpath1, imgkey2, imgpath2);
            data = bos.toByteArray();
            bos.close();
            ByteArrayEntity formEntity = new ByteArrayEntity(data);
            post.setEntity(formEntity);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return post;
    }

    /**
     * Upload weibo contents into output stream .
     */
    private void paramToUpload(OutputStream baos, HashMap<String, String> params)
            throws IOException {
        Iterator<Entry<String, String>> iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            StringBuilder temp = new StringBuilder(10);
            temp.setLength(0);
            temp.append(twoHyphens + boundary).append("\r\n");
            temp.append("content-disposition: form-data; name=\"")
                    .append(entry.getKey()).append("\"\r\n\r\n");
            temp.append(entry.getValue()).append("\r\n");
            // Loger.e("Key:" + entry.getKey());
            // Loger.e("Value:" + entry.getValue());
            byte[] res = temp.toString().getBytes();
            baos.write(res);
        }
    }

    /**
     * 图片文件转换byte[]类型的方法
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private byte[] readFileImage(String filename) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));
            int len = bufferedInputStream.available();
            byte[] bytes = new byte[len];
            int r = bufferedInputStream.read(bytes);
            if (len != r) {
                bytes = null;
                throw new IOException("读取文件不正确");
            }
            return bytes;
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
    }

    /**
     * 上传一张图片 Upload image into output stream .
     */
    private void imageContentToUpload(OutputStream out, String imgkey,
                                      String imgpath) throws IOException {

        if (TextUtils.isEmpty(imgpath)) {
            return;
        }

        StringBuilder temp = new StringBuilder();
        temp.append(twoHyphens + boundary + lineEnd);
        temp.append("Content-Disposition: form-data; name=\"" + imgkey
                + "\"; filename=\"temp.jpg\"\r\n");
        byte[] content = readFileImage(imgpath);
        String imgtype = getImageType(imgpath);
        if (imgtype != null
                && (imgtype.equalsIgnoreCase("image/gif")
                || imgtype.equalsIgnoreCase("image/png") || imgtype
                .equalsIgnoreCase("image/jpeg"))) {
            temp.append("Content-Type: " + imgtype + lineEnd);
            temp.append(lineEnd);
            byte[] res = temp.toString().getBytes();
            out.write(res);
            out.write(content);
            out.write(lineEnd.getBytes());
            out.write((lineEnd + twoHyphens + boundary + twoHyphens).getBytes());
        } else {
            throw new IOException(
                    "Unsupported image type, Only Suport JPG ,GIF,PNG!");
        }
    }

    /**
     * 同时上传两张图片的流处理
     */
    private void imageContentToUpload(OutputStream out, String imgkey1,
                                      String imgpath1, String imgkey2, String imgpath2)
            throws IOException {
        if (TextUtils.isEmpty(imgpath1)) {
            return;
        }

        StringBuilder temp = new StringBuilder();
        temp.append(twoHyphens + boundary + lineEnd);
        temp.append("Content-Disposition: form-data; name=\"" + imgkey1
                + "\"; filename=\"temp1.jpg\"" + lineEnd);

        byte[] content = readFileImage(imgpath1);
        String imgtype = getImageType(imgpath1);
        if (imgtype != null
                && (imgtype.equalsIgnoreCase("image/gif")
                || imgtype.equalsIgnoreCase("image/png") || imgtype
                .equalsIgnoreCase("image/jpeg"))) {
            temp.append("Content-Type: " + imgtype + lineEnd);
            temp.append(lineEnd);
            byte[] res = temp.toString().getBytes();
            out.write(res);
            out.write(content);
            out.write(lineEnd.getBytes());
        } else {
            throw new IOException(
                    "Unsupported image type, Only Suport JPG ,GIF,PNG!");
        }

        if (!TextUtils.isEmpty(imgpath2)) {
            StringBuilder temp2 = new StringBuilder();
            temp2.append(twoHyphens + boundary + lineEnd);
            temp2.append("Content-Disposition: form-data; name=\"" + imgkey2
                    + "\"; filename=\"temp2.jpg\"" + lineEnd);
            byte[] content2 = readFileImage(imgpath2);
            String imgtype2 = getImageType(imgpath2);
            if (imgtype2 != null
                    && (imgtype2.equalsIgnoreCase("image/gif")
                    || imgtype2.equalsIgnoreCase("image/png") || imgtype2
                    .equalsIgnoreCase("image/jpeg"))) {
                temp2.append("Content-Type: " + imgtype2 + lineEnd);
                temp2.append(lineEnd);
                byte[] res = temp2.toString().getBytes();
                out.write(res);
                out.write(content2);
                out.write(lineEnd.getBytes());
            } else {
                throw new IOException(
                        "Unsupported image type, Only Suport JPG ,GIF,PNG!");
            }
        }
        out.write((lineEnd + twoHyphens + boundary + twoHyphens).getBytes());// 数据结束标志
    }

    /**
     * 获取图片类型
     *
     * @param imgPath
     * @return
     */
    private String getImageType(String imgPath) {
        String type = "";
        String strTemp = imgPath;
        strTemp = strTemp.toLowerCase();
        if (strTemp.endsWith(".png")) {
            type = "image/png";
        } else if (strTemp.endsWith(".jpg") || strTemp.endsWith(".jpeg")) {
            type = "image/jpeg";
        } else if (strTemp.endsWith(".gif")) {
            type = "image/gif";
        }
        return type;
    }

    /**
     * 执行Request，HttpPost中参数默认编码方式为BaseHttp中的ENCODING
     */
    public HttpResult executeRequest(String url,
                                     HashMap<String, String> hmParams, String imgkey1, String imgpath1,
                                     String imgkey2, String imgpath2) throws TimeoutException,
            NetworkException {
        return sendRequest(buildHttpUriRequest(url, hmParams, imgkey1,
                imgpath1, imgkey2, imgpath2));
    }

    /**
     * 请求连接，返回HttpResult
     *
     * @param url
     * @param hmParams
     * @param imgkey
     * @param imgpath
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(String url,
                                     HashMap<String, String> hmParams, String imgkey, String imgpath)
            throws TimeoutException, NetworkException {
        return sendRequest(buildHttpUriRequest(url, hmParams, imgkey, imgpath));
    }

    /**
     * 请求连接，返回HttpResult
     *
     * @param post
     * @return
     * @throws TimeoutException
     * @throws NetworkException
     */
    public HttpResult executeRequest(HttpPost post) throws TimeoutException,
            NetworkException {
        return sendRequest(post);
    }
}
