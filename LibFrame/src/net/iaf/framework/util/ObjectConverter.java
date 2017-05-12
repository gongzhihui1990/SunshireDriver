package net.iaf.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectConverter {
    public static byte[] ObjectToByte(java.lang.Object obj) throws Exception {
        byte[] bytes = new byte[1024];
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return (bytes);
    }

    public static java.lang.Object ByteToObject(byte[] bytes) throws Exception {
        java.lang.Object obj = new java.lang.Object();
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return obj;
    }
}
