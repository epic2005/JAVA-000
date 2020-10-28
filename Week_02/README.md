## Week02 作业（周四）：JAVA中不同GC的总结：
1. 串行 GC: 串行 GC 对年轻代使用 mark-copy(标记-复制) 算法, 对老年代使用 mark-sweep-compact(标记-清除-整理)算法. 两者都是单线程的垃圾收集器,不能进行并行处理，并且都会触发全线暂停(STW),停止所有的应用线程。该选项只适合几百MB堆内存的JVM,而且是单核CPU时比较有用。
2. 并行 GC：并行 GC 年轻代和老年代的垃圾回收都会触发STW事件,暂停所有的应用线程来执行垃圾收集。两者在执行 标记和 复制/整理阶段时都使用多个线程, 因此得名“(Parallel)”。
3. CMS GC：CMS GC对年轻代采用并行 STW方式的 mark-copy (标记-复制)算法, 对老年代主要使用并发 mark-sweep (标记-清除)算法。CMS的设计目标是避免在老年代垃圾收集时出现长时间的卡顿。主要通过两种手段来达成此目标。
4. G1： 最新一代GC，目的替代 CMS，并且将STW停顿的时间和分布变成可预期以及可配置的

## Week02 作业（周六）：OkHttp via access NIO/Netty Client demo code：
```java
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
```
