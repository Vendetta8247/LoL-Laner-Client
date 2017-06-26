package ua.com.vendetta8247.lollaner;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

// MIT License
//
// Copyright(c) 2017 Pär Amsen
//
// Permission is hereby granted,free of charge,to any person obtaining a copy
// of this software and associated documentation files(the"Software"),to deal
// in the Software without restriction,including without limitation the rights
// to use,copy,modify,merge,publish,distribute,sublicense,and/or sell
// copies of the Software,and to permit persons to whom the Software is
// furnished to do so,subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED"AS IS",WITHOUT WARRANTY OF ANY KIND,EXPRESS OR
// IMPLIED,INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,DAMAGES OR OTHER
// LIABILITY,WHETHER IN AN ACTION OF CONTRACT,TORT OR OTHERWISE,ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

/**
 * Concurrently download from predefined list of urls on a predefined amount of worker threads.
 * Workers execute requests until the Stack is depleted in a synchronized manor, since Stack
 * is synchronized in Java.
 *
 * Does not care about IO errors and/or pause/resume for now
 *
 * @author Pär Amsen 05/2017
 */
public class ConcurrentRequestSource {
    public static final int WORKERS = 20;

    private AtomicBoolean done;
    private File imagesDir;
    private Stack<String> urls;
    private String version;

    public ConcurrentRequestSource(String version, File imagesDir, List<String> urls) {
        this.imagesDir = imagesDir;
        this.urls = new Stack<>();
        this.urls.addAll(urls);
        this.version = version;
        done = new AtomicBoolean(false);
    }

    /**
     * @param callback is a callback that will be called with each work result when available
     */
    public void download(Callback callback) {
        for(Thread worker : newWorkers(callback)) {
            worker.start();
        }
    }

    private List<Thread> newWorkers(Callback callback) {
        ArrayList<Thread> workers = new ArrayList<>();

        for (int i = 0; i < WORKERS; i++) {
            workers.add(new Thread(newWorker(callback), "ConcurrentRequestSourceWorker-" + i));
        }

        return workers;
    }

    private Runnable newWorker(final Callback callback) {
        return new Runnable() {
            @Override
            public void run() {
                while (!urls.empty()) {
                    callback.onEach(ConcurrentRequestSource.this.doDownload(urls.pop()));
                }

                if(done.compareAndSet(false, true)) {
                    callback.onDone(imagesDir);
                }

                //   System.out.println(String.format("Thread %s done", Thread.currentThread().getName()));
            }
        };
    }

    /**
     * Download and persist file on disk
     *
     * @return File pointing to downloaded file on disk
     */
    private File doDownload(String path) {
        File f = new File(imagesDir + "/" + path);
        try {
            String newPath = "http://ddragon.leagueoflegends.com/cdn/" + version + "/img/profileicon/" + path;
            URL url = new URL(newPath);
            Thread.sleep(10); //mock request time
            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(15000);
            ucon.setConnectTimeout(50000);

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);



            if (f.exists()) {
                f.delete();
            }
            //System.out.println(f.createNewFile());
            //System.out.println("File created");



            FileOutputStream outStream = new FileOutputStream(f);
            byte[] buff = new byte[5 * 1024];

            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (InterruptedException e) {
            //ignore
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.printf("Downloaded %s on thread: %s%n", path, Thread.currentThread().getName());
        return f; // file pointing to downloaded image on disk
    }

    interface Callback {
        void onEach(File image);
        void onDone(File imagesDir);
    }
}