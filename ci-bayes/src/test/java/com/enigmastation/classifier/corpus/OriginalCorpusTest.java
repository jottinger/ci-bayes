package com.enigmastation.classifier.corpus;

import com.enigmastation.classifier.ClassifierProbability;
import com.enigmastation.classifier.FisherClassifier;
import com.enigmastation.classifier.impl.FisherClassifierImpl;
import com.enigmastation.classifier.testing.MemoryMonitor;
import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.objectify.CategoryDAOImpl;
import com.enigmastation.dao.objectify.FeatureDAOImpl;
import com.enigmastation.extractors.impl.SimpleWordLister;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.ice.tar.TarArchive;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Date;

public class OriginalCorpusTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @AfterTest
    public void tearDown() {
        helper.tearDown();
    }

    @BeforeTest(alwaysRun = true)
    public void setup() throws Throwable {
        try {
            FisherClassifierImpl classifier = new FisherClassifierImpl();
            classifier.setWordLister(new SimpleWordLister());
            FeatureDAO fdao = new FeatureDAOImpl();
            CategoryDAO cdao = new CategoryDAOImpl();
            classifier.setFeatureDAO(fdao);
            classifier.setCategoryDAO(cdao);
            classifier.init();

            this.classifier = classifier;
            helper.setUp();

        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    FisherClassifier classifier;

    interface Manager {
        void handleFile(Command c, String type, File corpus) throws IOException;
    }

    interface Command {
        String getType();

        void execute(String s, String type);
    }

    interface Selector {
        boolean accept(int i);
    }

    @Test(groups = {"fulltest"})
    public void runTest() throws IOException {
        MemoryMonitor monitor = new MemoryMonitor();
        monitor.start();
        //monitor.setMonitoring(true);
        savedOutput = new PrintWriter(sw);
        long start = System.currentTimeMillis();
        buildClassifier();
        long end = System.currentTimeMillis();
        System.gc();
        savedOutput.printf("Training Runtime: %dms\n", (end - start));
        monitor.setMonitoring(true);
        start = System.currentTimeMillis();
        testClassifier();
        end = System.currentTimeMillis();
        monitor.setMonitoring(false);
        System.gc();
        savedOutput.printf("Testing Runtime: %dms\n", (end - start));
        showOutput();
        monitor.shutdown();
        // no need to empty the filesystem with maven, since we no longer pollute the same tree.
        //emptyFilesystem();

        // if this test takes longer than twenty seconds, something's wrong. We're running slow.
        assert (end - start) < 20000L;
    }

    private static void showOutput() {
        System.err.println(sw.getBuffer().toString());
    }

    @SuppressWarnings("unused")
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
                }, new Selector() {
                    public boolean accept(int i) {
                        return true;
                    }
                }, false, true
        );
    }

    static PrintWriter savedOutput;
    static StringWriter sw = new StringWriter();

    private void testClassifier() throws IOException {

        final int[] hits = new int[1];
        final int[] misses = new int[2];
        processFiles(new Manager() {
                    public void handleFile(Command c, String type, File corpus) throws IOException {
                        FileReader fr = new FileReader(corpus);
                        BufferedReader br = new BufferedReader(fr, 16384);
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
                    boolean normalized = false;

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
                            if (normalized == false) {
                                normalized = true;
                                ClassifierProbability[] probs = classifier.getProbabilities(s);
                                savedOutput.println("probabilities: ");
                                for (ClassifierProbability prob : probs) {
                                    savedOutput.println(prob.toString());
                                }
                            }
                            hits[0]++;
                        }
                    }
                }, new Selector() {
                    public boolean accept(int i) {
                        return (i % 10) > 7;
                    }
                }, false
        );
        savedOutput.println("Hits: " + hits[0]);
        savedOutput.println("Misses: " + (misses[0] + misses[1]));
        savedOutput.println("Wrong classifications: " + misses[0]);
        savedOutput.println("Unclear classifications: " + misses[1]);
        savedOutput.println("Totals: " + (hits[0] + misses[0] + misses[1]) + " items");
        savedOutput.println("Hit rate: " + ((1.0 * hits[0]) / (hits[0] + misses[0] + misses[1])) + "%");
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
                }, new Selector() {
                    public boolean accept(int i) {
                        return (i % 10) <= 7;
                    }
                }, true, true
        );
    }

    void processFiles(final Manager m, Command c, final Selector s, boolean expand) throws IOException {
        processFiles(m, c, s, expand, false);
    }

    void processFiles(final Manager m, final Command c, final Selector s, final boolean expand, boolean multithread) throws IOException {

        File file = new File(System.getProperty("user.dir") + "/target/test-classes/training");
        final File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.indexOf("bz2") != -1) {
                    if (name.indexOf("data") == -1) {
                        return true;
                    }
                }
                return false;
            }
        });
        ThreadGroup g = new ThreadGroup("CORPUS GROUP");
        if (expand) {
            for (final File f : files) {
                Thread e1 = new Thread(g, "Expando " + f.getName()) {
                    public void run() {
                        try {
                            final File x = new File(f.toString() + ".data");
                            x.mkdir();

                            FileInputStream fis = new FileInputStream(f);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            //noinspection ResultOfMethodCallIgnored
                            bis.read(new byte[2]);
                            CBZip2InputStream bzip2 = new CBZip2InputStream(bis);
                            TarArchive archive = new TarArchive(bzip2);
                            archive.extractContents(x);
                            System.out.printf("%s finished expanding.\n", f.getName());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                };
                e1.start();
            }
            while (g.activeCount() > 0) {
                try {
                    Thread.sleep(2000);
                    System.out.printf("%s waiting for expansion to finish: %d threads\n", new Date(), g.activeCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        for (final File f : files) {
            Thread t = new Thread(g, f.getName()) {
                public void run() {
                    try {
                        System.out.println(f);
                        final File x = new File(f.toString() + ".data");

                        System.out.printf("%s...%s!\n", f.getName(), c.getType());
                        String type = f.toString().indexOf("ham") == -1 ? "spam" : "ham";
                        int t = 0;
                        for (File dir : x.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.indexOf("data") == -1;
                            }
                        })) {
                            for (File corpus : dir.listFiles()) {
                                if (s.accept(t)) {
                                    m.handleFile(c, type, corpus);
                                }
                                if (++t % 100 == 0) {
                                    System.out.printf("%s: %d mark\n", c.getType(), t);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            if (multithread) {
                t.start();
            } else {
                t.run();
            }
        }
        if (multithread) {
            while (g.activeCount() > 0) {
                try {
                    Thread.sleep(2000);
                    System.out.printf("%s: Waiting for %d threads to finish...\n", new Date().toString(), g.activeCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
