package com.rdm.common.base.unit;

import com.rdm.base.Extra;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;


/**
 *
 * 测试基本的运行环境。
 * Created by Rao on 2015/1/11.
 */
public class ExtraTester extends ApplicationTest {

    private static byte[] DATA = new byte[]{34,45,24,65,35,65,35,3};

    public void test(){
        Extra extra = new Extra();

        extra.putDouble("double",456456546546.5);
        extra.putInt("integer",10);
        extra.putLong("long",324243242223L);
        extra.putString("string","等级外网机");
        extra.putChar("char",(char)3);
        extra.putByteArray("bytes",DATA);
        extra.putByte("byte",(byte)0x8);
        extra.putBoolean("boolean",true);
        extra.putFloat("float",0.234f);
        extra.putShort("short",(short)888);

        assertThat(extra);

        byte[] data = extra.toByteArray();

        assertThat(Extra.parase(data));
    }

    private void assertThat(Extra extra){
        Assert.assertEquals(extra.get("integer"), 10);
        Assert.assertEquals(extra.get("long"), 324243242223L);
        Assert.assertEquals(extra.get("string"), "等级外网机");
        Assert.assertEquals(extra.get("char"), (char) 3);
        Assert.assertEquals(extra.getByteArray("bytes").length, DATA.length);
        Assert.assertEquals(extra.get("byte"), (byte) 0x8);
        Assert.assertEquals(extra.get("boolean"), true);
        Assert.assertEquals(extra.get("float"), 0.234f);
        Assert.assertEquals(extra.get("double"), 456456546546.5);
        Assert.assertEquals(extra.get("short"), (short) 888);
    }

    public void testDemo(){

        String normal = "党委书记噢金佛山基地ddd-werjosjdc.;s";

        byte[] data = toByteArrays(normal);
        String txet2 = toString(data);
        Assert.assertTrue(normal.equals(txet2));

      /*  KeyValue kv = new KeyValue();
        kv.setKey("xsdsfsdf" + System.currentTimeMillis());
        kv.setByteArrayValue(new byte[]{1, 2, 3});
        KeyValue load1 =  KeyValue.loadFromDB(getSession(), kv.getKey(), kv);
        KeyValue.saveToDB(getSession(),kv);
        Assert.assertTrue(load1 == kv);

        KeyValue load2 =  KeyValue.loadFromDB(getSession(), kv.getKey(), kv);

        Assert.assertTrue(load2 != kv);
        Assert.assertTrue(load1.getKey().equals(kv.getKey()));
*/




    }


    protected String toString(byte[] data){
        if(data == null) {
            return null;
        }

        if(data.length == 0){
            return "";
        }
        try {
           /* ByteArrayInputStream bin = new ByteArrayInputStream(data);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            //压缩处理
            GZIPOutputStream zout = new GZIPOutputStream(bout);
            IOUtils.copy(bin, zout);
            bin.close();
            zout.flush();
            bout.flush();
            new String(bout.toByteArray(),"utf8");*/
            return new String(data,"utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将文本压缩序列化。
     * @param text
     * @return
     */
    protected byte[] toByteArrays(String text){
        if(text == null) {
            return null;
        }

        if(text.length() == 0){
            return new byte[0];
        }
        try {
           /* ByteArrayInputStream bin = new ByteArrayInputStream(text.getBytes("utf8"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            //压缩处理
            GZIPInputStream zis = new GZIPInputStream(bin);
            IOUtils.copy(zis,bout);
            zis.close();
            bin.close();
            bout.flush();
            return bout.toByteArray();*/
            return text.getBytes("utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
