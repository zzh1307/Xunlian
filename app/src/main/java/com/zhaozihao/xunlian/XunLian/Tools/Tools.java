package com.zhaozihao.xunlian.XunLian.Tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Tools {
    Socket socket;
    Context context;
    String filePath;
    static final String algorithmStr = "AES/ECB/PKCS5Padding";

    private static final Object TAG = "AES";

    static private KeyGenerator keyGen;

    static private Cipher cipher;

    static boolean isInited = false;
    List<Person> personList = new ArrayList<Person>();;
    String  sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

    public  Tools (Context context){
        this.context = context;
	}
    /*
     *
     *      *1.检测帐号是否注册过
            {"mark":1, "account":"188xxxxxxxx"}
            *2.帐号注册
             //   旧的
            { "mark":2, "account":"xxxxxxxxx", "secret":xxxxxxxx}
           //   新的
            {
                "mark": "2",
                "account": "xxxxxxxxx",
                "secret": "xxxxxxxx",
                "flag": "1.账号注册,2.密码改密保,3.密保改密码",
                "question": "1?",
                "answer": "1"
            }
            3.请求密保问题
            {"mark":3, "account":"xxxxxxxxxx"}

           *4.用户更新信息
            根据数据库表看，现在有自己姓名
            {
                "mark": 6,
                "account": "12222222",
                "name":"newName",
                "update": [
                            {
                                "type": 1,
                                "isUpdateOrInsert": "updata:1,insert:2",
                                "contact": "222"
                            },

                            {
                                "type": 1,
                                "isUpdateOrInsert": "updata:1,insert:2",
                                "contact": "222"
                            }
                           ]
             }
            type是标记哪种联系方式，比如邮箱，电话等等，contact是具体内容
            type1是个人手机号
            type2是工作电话
            type4是家庭手机号
            type8是个人邮箱
            type16是工作邮箱
            type32是家庭邮箱
            type64是qq
            type128是weibo

            return c

            *5.进入应用
            本地没有数据，发送全部数据
            {"mark":7, "account":xxxxxxxxx}

            本地有数据，下拉刷新操作等
            {"mark":8, "account":xxxxxxxxx}

            6.添加联系人

            发送请求获取联系人的详细信息

            {"mark":9, "account":xxxxxxxxx, "friendaccount":xxxxxxxx }

            发动添加请求

            {"mark":15}, "account":xxxxxxxx, "friendaccount":xxxxxxx}

            7.生成二维码
            {"mark":11, "account":xxxxxxxx, "qcode":xxxxxxx}

            8.登录
            {"mark":12, "account":xxxxxxxx, "secret":xxxxxxxxx}

            9删除联系人
            {"mark":13, "account":xxxxxxxx, "friendaccount":xxxxxxxxx}
     */

	public String Key2Json( String mask,String key,String value) {  
        String jsonresult = "";
        JSONObject object = new JSONObject();
        try {  
        	object.put("mark", mask);
        	object.put(key, value);
            jsonresult = object.toString();//生成返回字符串
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
        return jsonresult;  
    }
	public String Key2Json(String mark,String key1,String value1,String key2,String value2) {
        String jsonresult = "";
        JSONObject object = new JSONObject();
        try {  
        	object.put("mark", mark);
        	object.put(key1, value1);
        	object.put(key2, value2);
            jsonresult = object.toString();
            Log.e("------",jsonresult);
        } catch (JSONException e) {
            e.printStackTrace();
            return "11";
        }  
        return jsonresult;  
    }
    public String Key2Json(String mark,String[] K,String[] V) {
        String jsonresult = "";
        JSONObject object = new JSONObject();
        try {
            object.put("mark", mark);
            for (int i = 0;i<K.length;i++){
                object.put(K[i], V[i]);
            }
            jsonresult = object.toString();
            Log.e("------",jsonresult);
        } catch (JSONException e) {
            e.printStackTrace();
            return "11";
        }
        return jsonresult;
    }


    public String Key2Json( String marddk,String value1,String value2,String value3,String[] update) {
        String jsonresult = "";
        JSONObject object = new JSONObject();
        try {
            object.put("mark", marddk);
            object.put("account", value1);
            JSONObject info = new JSONObject();
            info.put("name", value2);
            info.put("head", value3);
            object.put("info",info);
            JSONArray array = new JSONArray();

            Log.e("aaaaaa", update.length + "");

            for (int i = 0;i<update.length;i++){
                Log.e("aaaaaa", update[i] );
                String[] info1 =  update[i].split("#");
                JSONObject jsoninfo = new JSONObject();
                jsoninfo.put("type",info1[0]);
                jsoninfo.put("content", decryption(info1[1]));
                Log.e("aaaaaa", jsoninfo.toString() + "----------" + i);
                array.put(i,jsoninfo);
            }
            object.put("update",array);
            jsonresult = object.toString();
            Log.e("------",jsonresult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonresult;
    }
	
	public String key2Json(String name[],String value[]) {  
        String jsonresult = "";
        JSONObject object = new JSONObject();
        try {
        	object.put(name[0], value[0]);
        	object.put(name[1], 14);

        	JSONObject personInfo = new JSONObject();
            personInfo.put(name[2], value[2]);
            personInfo.put(name[3], value[3]);

            JSONObject emailInfo = new JSONObject();
            emailInfo.put(name[4], value[4]);
            emailInfo.put(name[5], value[5]);
            emailInfo.put(name[6], value[6]);
            object.put("emailInfo", emailInfo);

            JSONObject phoneInfo = new JSONObject();
            phoneInfo.put(name[7], value[7]);
            phoneInfo.put(name[8], value[8]);
            phoneInfo.put(name[9], value[9]);

            object.put("phoneInfo",phoneInfo);

            object.put(name[10],value[10]);


            object.put(name[11],value[11]);
            object.put("personInfo",personInfo);
            jsonresult = object.toString();
            Log.e("------",jsonresult);
        } catch (JSONException e) {
            e.printStackTrace();  
        }  
        return jsonresult;  
    }




    public  String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().substring(8, 24);
    }

    public String sendString2ServersSocket(String str) {

	    String result = "";
        try {
            // 生成Socket,并且指定IP和端口号
             socket = new Socket();
             socket.connect(new InetSocketAddress("121.42.210.40", 10001), 3000);
            //socket.connect(new InetSocketAddress("192.168.43.165", 10000), 3000);

            // 但是对于实时交互性高的程序,建议其改为true,即关闭Nagle算法，
            // 客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
             socket.setTcpNoDelay(true);
            //获取到输入流
             InputStream isRead = socket.getInputStream();
            //将输入流转换成BufferedReader,读取数据
             BufferedReader br = new BufferedReader(new InputStreamReader(isRead));
             //获取输出流,将输出流转换成 BufferedWriter
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
             socket.getOutputStream()));
            //将str里面的回车换成空格
             writer.write(str.replace("\n", " ") + "\n");
            //刷新输出流,将数据发送到服务器
             writer.flush();
            //建立读取缓冲区,大小40000,缓冲区大小决定了可以加的好友数
            //设置为最多可加100为好友
             char[] buffer = new char[40000];
            //建立标志位.默认为0
            int count = 0;
            //进行数据读取
            if((count = br.read(buffer))>0){
                //char[] buffer1 = new char[1024];
                //将读取到的数据进行转换,转换为String,然后进行拼接
                result += getInfoBuff(buffer, count)+ "\n";
                Log.e("sendStr2Socket", result.length()+"");

            }
            Log.e("sendStr2Socket", result);

        }catch (UnknownHostException e) {
            e.printStackTrace();
            //发生未知错误,返回null
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            //发生I/O流错误,返回null
            return result;
        }
        //请求成功,返回Result
        return result;
    }

/*
 * 
 * 将字节数组转换成图片
 */

public Bitmap BytesToBitmap(byte[] b) {
    if (b.length != 0) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
   } else {
        return null;
    }
}

    private String getInfoBuff(char[] buffer, int count)
    {
        char[] temp = new char[count];
        for(int i=0; i<count; i++)
        {
            temp[i] = buffer[i];
        }
        return new String(temp);
    }


/*
 * 
 * 将图片转换成字节数组
 */

    public byte[] BitmapToBytes(Bitmap bm) {
        byte[] sendBytes=null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            sendBytes= baos.toByteArray().toString() .getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendBytes;
}


    public  ProgressDialog creatDialog(Context context,String title,String message){

        ProgressDialog pd1 = null;

        pd1 = new ProgressDialog(context);
        // 设置对话框的标题
        pd1.setTitle(title);
        // 设置对话框显示的内容
        pd1.setMessage(message);
        // 设置对话框能用“取消”按钮关闭
        pd1.setCancelable(false);
        // 设置对话框的进度条风格
        pd1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置对话框的进度条是否显示进度
        pd1.setIndeterminate(false);

        return pd1;

    }





    /** 保存方法 */
    public String saveBitmap(Bitmap bm,String str) {
        File f = new File(createDirOnSDCard()+"/", str + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            return createDirOnSDCard()+"/"+ str + ".jpg";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File createDirOnSDCard()
    {
           File dirFile = new File(sdCardRoot + File.separator + "xunlian" +File.separator);
           dirFile.mkdirs();
           return dirFile;
        }



    public String[] parseJSONMark12(String strResult) throws NumberFormatException, JSONException {

        JSONObject jsonObj = new JSONObject(strResult);
        JSONObject result = jsonObj.getJSONObject("result");
        String IsSuccess =result.getString("IsSuccess");
        String ResultINFO =result.getString("ResultINFO");
        String[] StrArry = new String[]{IsSuccess,ResultINFO};
        return StrArry;
    }
    public String[] parseJSONMark9(String strResult) throws NumberFormatException, JSONException {

        JSONObject jsonObj = new JSONObject(strResult);
        JSONObject result = jsonObj.getJSONObject("result");
        String[] StrArry = new String[10];

        JSONObject ResultINFO = result.getJSONObject("ResultINFO");
        StrArry[0] = ResultINFO.getString("name");
        StrArry[1] =ResultINFO.getString("personNumber");
        StrArry[2] =ResultINFO.getString("workNumber");
        StrArry[3] =ResultINFO.getString("homeNumber");
        StrArry[4] =ResultINFO.getString("personEmail");
        StrArry[5] =ResultINFO.getString("workEmail");
        StrArry[6] =ResultINFO.getString("homeEmail");
        StrArry[7] =ResultINFO.getString("qqNumber");
        StrArry[8] =ResultINFO.getString("weiboNumber");
        StrArry[9] =result.getString("requestPhoneNum");
        return StrArry;
    }

    public List<Person> parseJSONMark7(String strResult) throws NumberFormatException, JSONException {

        List<Person>  personlist = new ArrayList<Person>();
        Person person;
        JSONObject jsonObj = new JSONObject(strResult);
        String[] StrArry = new String[10];
        JSONArray result = jsonObj.getJSONArray("result");
        for(int i = 0;i<result.length();i++){
            JSONObject json = (JSONObject) result.get(i);
            StrArry[0] =json.getString("name");
            StrArry[1] =json.getString("personNumber");
            StrArry[2] =json.getString("workPhoneNumber");
            StrArry[3] =json.getString("homePhoneNumber");
            StrArry[4] =json.getString("personEmail");
            StrArry[5] =json.getString("workEmail");
            StrArry[6] =json.getString("homeEmail");
            StrArry[7] =json.getString("qqNumber");
            StrArry[8] =json.getString("weiboNumber");
            StrArry[9] =json.getString("account");
            person = new Person(StrArry);
            personlist.add(person);
        }


        return personlist;
    }







    private   void init() {
        try {
            /**为指定算法生成一个 KeyGenerator 对象。
             *此类提供（对称）密钥生成器的功能。
             *密钥生成器是使用此类的某个 getInstance 类方法构造的。
             *KeyGenerator 对象可重复使用，也就是说，在生成密钥后，
             *可以重复使用同一 KeyGenerator 对象来生成进一步的密钥。
             *生成密钥的方式有两种：与算法无关的方式，以及特定于算法的方式。
             *两者之间的惟一不同是对象的初始化：
             *与算法无关的初始化
             *所有密钥生成器都具有密钥长度 和随机源 的概念。
             *此 KeyGenerator 类中有一个 init 方法，它可采用这两个通用概念的参数。
             *还有一个只带 keysize 参数的 init 方法，
             *它使用具有最高优先级的提供程序的 SecureRandom 实现作为随机源
             *（如果安装的提供程序都不提供 SecureRandom 实现，则使用系统提供的随机源）。
             *此 KeyGenerator 类还提供一个只带随机源参数的 inti 方法。
             *因为调用上述与算法无关的 init 方法时未指定其他参数，
             *所以由提供程序决定如何处理将与每个密钥相关的特定于算法的参数（如果有）。
             *特定于算法的初始化
             *在已经存在特定于算法的参数集的情况下，
             *有两个具有 AlgorithmParameterSpec 参数的 init 方法。
             *其中一个方法还有一个 SecureRandom 参数，
             *而另一个方法将已安装的高优先级提供程序的 SecureRandom 实现用作随机源
             *（或者作为系统提供的随机源，如果安装的提供程序都不提供 SecureRandom 实现）。
             *如果客户端没有显式地初始化 KeyGenerator（通过调用 init 方法），
             *每个提供程序必须提供（和记录）默认初始化。
             */
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 初始化此密钥生成器，使其具有确定的密钥长度。
        keyGen.init(128); //128位的AES加密
        try {
            // 生成一个实现指定转换的 Cipher 对象。
            cipher = Cipher.getInstance(algorithmStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        //标识已经初始化过了的字段
        isInited = true;
    }

    private  byte[] genKey() {
        if (!isInited) {
            init();
        }
        //首先 生成一个密钥(SecretKey),
        //然后,通过这个秘钥,返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
        return keyGen.generateKey().getEncoded();
    }

    private  byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;
        if (!isInited) {
            init();
        }
        /**
         *类 SecretKeySpec
         *可以使用此类来根据一个字节数组构造一个 SecretKey，
         *而无须通过一个（基于 provider 的）SecretKeyFactory。
         *此类仅对能表示为一个字节数组并且没有任何与之相关联的钥参数的原始密钥有用
         *构造方法根据给定的字节数组构造一个密钥。
         *此构造方法不检查给定的字节数组是否指定了一个算法的密钥。
         */
        Key key = new SecretKeySpec(keyBytes, "AES");
        try {
            // 用密钥初始化此 cipher。
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            //按单部分操作加密或解密数据，或者结束一个多部分操作。(不知道神马意思)
            encryptedText = cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    private  byte[] encrypt(String content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);//   ʼ
            byte[] result = cipher.doFinal(byteContent);
            return result; //
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  byte[] decrypt(byte[] content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ
            byte[] result = cipher.doFinal(content);
            return result; //
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  byte[] getKey(String password) {
        byte[] rByte = null;
        if (password!=null) {
            rByte = password.getBytes();
        }else{
            rByte = new byte[24];
        }
        return rByte;
    }

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public  String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public  byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    //注意: 这里的password(秘钥必须是16位的)
    private  final String keyBytes = "xunlianxunlianxl";

    /**
     *加密
     */
    public  String encode(String content){
        //加密之后的字节数组,转成16进制的字符串形式输出
        return parseByte2HexStr(encrypt(content, keyBytes));
    }

    /**
     *解密
     */
    public  String decode(String content){
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
        return new String(b);
    }

    //调用加密
    public  String encryption(String str){
        String pStr = encode(str);
        Log.i("encryption加密：", str + "\n" + pStr);
        return pStr;
    }
    //调用解密
    public  String decryption(String str){
        String pStr = decode(str);
        Log.i("decryption解密：", str + "\n" + pStr);
        return pStr;
    }
}


