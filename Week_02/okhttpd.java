package demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class okhttpd {

    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:8801")
                .header("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String body = response.body().string();
        System.out.println(body);
    }
}
