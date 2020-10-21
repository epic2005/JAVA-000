package Week_01;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class HelloClassloader extends ClassLoader {
    public static void main(String[] args) {
        try{
            Class<?> myclass = new HelloClassloader().findClass("Hello");
            Object obj = myclass.newInstance();
            Method method = myclass.getMethod("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        InputStream mystream = HelloClassloader.class.getClassLoader().getResourceAsStream("Week_01/Hello.xlass");
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[mystream.available()];
            mystream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mystream != null) {
                try {
                    mystream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
