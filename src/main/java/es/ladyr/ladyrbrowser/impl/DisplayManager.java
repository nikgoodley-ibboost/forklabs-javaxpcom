package es.ladyr.ladyrbrowser.impl;

import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.widgets.Display;

public class DisplayManager {



        private static DisplayManager sc = null;

        private Display display;
        private CountDownLatch initDisplay;
        private boolean ownDisplay = true;

        public synchronized static DisplayManager getInstance() {
                if (sc == null) {
                        sc = new DisplayManager();
                }

                return sc;
        }

        private DisplayManager() {

                this.initDisplay = new CountDownLatch(1);

                Thread t = new Thread("SWT-Thread") {
                        @Override
                        public void run() {

                                try {

                                DisplayManager.this.display = new Display();
                                DisplayManager.this.initDisplay.countDown();

                                while (true) {
                                        try {
                                                if (!DisplayManager.this.display.readAndDispatch())
                                                        DisplayManager.this.display.sleep();
                                        } catch (Exception e) {
                                                break;
                                        }
                                }

                                } catch(Exception e){
                                        DisplayManager.this.display = Display.getDefault();
                                        DisplayManager.this.ownDisplay = false;
                                        DisplayManager.this.initDisplay.countDown();
                                }

                        }
                };

                t.setDaemon(true);
                t.start();

                try {
                        this.initDisplay.await();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        public Display getDisplay() {
                return this.display;
        }

}
