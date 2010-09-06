/*
 * @(#) $Header$
 *
 * Copyright (C) 2009  Micael Gallego Carrillo - LADyR
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

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
