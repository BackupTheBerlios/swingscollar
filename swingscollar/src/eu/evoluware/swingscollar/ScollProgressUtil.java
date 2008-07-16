//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** 
 * MySwing: Advanced Swing Utilites 
 * Copyright (C) 2005  Santhosh Kumar T 
 * <p/> 
 * This library is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version. 
 * <p/> 
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * Lesser General Public License for more details. 
 */ 
public class ScollProgressUtil{ 
    static class MonitorListener implements ChangeListener, ActionListener{ 
        ScollProgressMonitor monitor; 
        Window owner; 
        Timer timer; 
 
        public MonitorListener(Window owner, ScollProgressMonitor monitor){ 
            this.owner = owner; 
            this.monitor = monitor; 
        } 
 
        public void stateChanged(ChangeEvent ce){ 
            ScollProgressMonitor monitor = (ScollProgressMonitor)ce.getSource(); 
            if(monitor.getCurrent()!=monitor.getTotal()){ 
                if(timer==null){ 
                    timer = new Timer(monitor.getMilliSecondsToWait(), this); 
                    timer.setRepeats(false); 
                    timer.start(); 
                } 
            }else{ 
                if(timer!=null && timer.isRunning()) 
                    timer.stop(); 
                monitor.removeChangeListener(this); 
            } 
        } 
 
        public void actionPerformed(ActionEvent e){ 
            monitor.removeChangeListener(this); 
            ScollProgressDialog dlg = owner instanceof Frame 
                    ? new ScollProgressDialog((Frame)owner, monitor) 
                    : new ScollProgressDialog((Dialog)owner, monitor); 
            dlg.pack(); 
            dlg.setLocationRelativeTo(null); 
            dlg.setVisible(true); 
        }

    } 
 
    public static ScollProgressMonitor createScollProgressMonitor(Component owner, int total, boolean indeterminate, int milliSecondsToWait){ 
        ScollProgressMonitor monitor = new ScollProgressMonitor(total, indeterminate, milliSecondsToWait); 
        Window window = owner instanceof Window 
                ? (Window)owner 
                : SwingUtilities.getWindowAncestor(owner); 
        monitor.addChangeListener(new MonitorListener(window, monitor)); 
        return monitor; 
    } 
}

