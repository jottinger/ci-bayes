package com.enigmastation;

import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.impl.FisherClassifierImpl;
import com.ice.tar.TarArchive;
import org.apache.tools.bzip2.CBZip2InputStream;

import java.io.*;

public class TestTrainingCorpus {
    FisherClassifier classifier = new FisherClassifierImpl();

    interface Manager {
        void handleFile(Command c, String type, File corpus) throws IOException;
    }

    interface Command {
        String getType();

        void execute(String s, String type);
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        TestTrainingCorpus ttc = new TestTrainingCorpus();
        ttc.buildClassifier();
        long end = System.currentTimeMillis();
        System.out.printf("Training Runtime: %dms\n", (end - start));
        start = System.currentTimeMillis();
        ttc.testClassifier();
        end = System.currentTimeMillis();
        System.out.printf("Testing Runtime: %dms\n", (end - start));
        ttc.emptyFilesystem();

    }

    private void emptyFilesystem() throws IOException {
        processFiles(new Manager() {
            public void handleFile(Command c, String type, File corpus) throws IOException {
                corpus.delete();
            }
        }, new Command() {
            public String getType() {
                return "deleting";
            }

            public void execute(String s, String type) {
            }
        }, false);
    }

    private void testClassifier() throws IOException {
        final int[] hits = new int[1];
        final int[] misses = new int[2];
        processFiles(new Manager() {
            public void handleFile(Command c, String type, File corpus) throws IOException {
                FileReader fr = new FileReader(corpus);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                br.close();
                fr.close();
                c.execute(sb.toString(), type);
            }
        }, new Command() {
            public String getType() {
                return "testing";
            }

            public void execute(String s, String type) {
                String f = classifier.getClassification(s, "unknown");
                if (!f.equals(type)) {
                    System.out.println("should have gotten " + type + ", got " + f);
                    if (f.equals("unknown")) {
                        misses[1]++;
                    } else {
                        misses[0]++;
                    }

                } else {
                    hits[0]++;
                }
            }
        }, false);
        System.out.println("Hits: " + hits[0]);
        System.out.println("Misses: " + (misses[0] + misses[1]));
        System.out.println("Wrong classifications: " + misses[0]);
        System.out.println("Unclear classifications: " + misses[1]);
        System.out.println("Totals: " + (hits[0] + misses[0] + misses[1]) + " items");
        System.out.println("Hit rate: " + ((1.0 * hits[0]) / (hits[0] + misses[0] + misses[1])) + "%");
    }

    private void buildClassifier() throws IOException {
        processFiles(new Manager() {
            public void handleFile(Command c, String type, File corpus) throws IOException {
                FileReader fr = new FileReader(corpus);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                br.close();
                fr.close();
                c.execute(sb.toString(), type);
            }
        }, new Command() {
            public String getType() {
                return "training";
            }

            public void execute(String s, String type) {
                classifier.train(s, type);
            }
        }, true);
    }


    void processFiles(Manager m, Command c, boolean expand) throws IOException {

        File file = new File(System.getProperty("user.dir") + "/training");
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.indexOf("bz2") != -1) {
                    if (name.indexOf("data") == -1) {
                        return true;
                    }
                }
                return false;
            }
        });
        for (File f : files) {
            System.out.println(f);
            File x = new File(f.toString() + ".data");
            if (expand) {
                x.mkdir();

                FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bis = new BufferedInputStream(fis);
                //noinspection ResultOfMethodCallIgnored
                bis.read(new byte[2]);
                CBZip2InputStream bzip2 = new CBZip2InputStream(bis);
                TarArchive archive = new TarArchive(bzip2);
                archive.extractContents(x);
            }
            System.out.printf("...%s!\n", c.getType());
            String type = f.toString().indexOf("ham") == -1 ? "spam" : "ham";
            int t = 0;
            for (File dir : x.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.indexOf("data") == -1;
                }
            })) {
                for (File corpus : dir.listFiles()) {
                    m.handleFile(c, type, corpus);
                    if (++t % 100 == 0) {
                        System.out.println(t);
                    }
                }
            }
            // break;
        }

    }

}
