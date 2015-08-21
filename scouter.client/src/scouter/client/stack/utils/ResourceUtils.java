/*
 *  Copyright 2015 LG CNS.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package scouter.client.stack.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import scouter.client.stack.base.PreferenceManager;

public class ResourceUtils {
	public static InputStream getDefaultXMLConfig(){
		return ResourceUtils.class.getClassLoader().getResourceAsStream("/scouter/client/stack/doc/config_default.xml");
	}
    
    public static int [] getScreenSize(){
    	int [] size = new int[2];
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	size[0] = gd.getDisplayMode().getWidth();
    	size[1] = gd.getDisplayMode().getHeight();
    	return size;
    }
    
	static public String openFileSaveDialog(String [] names, String [] extensions, String path, String defaultName){
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setOverwrite(true);
		dialog.setFilterNames(names);
		dialog.setFilterExtensions(extensions);
		dialog.setFilterPath(path);
		if(defaultName != null){
			dialog.setFileName(defaultName);
		}
		return dialog.open();		
	}
	
    static public void saveFile(String fileName, String contents){
    	if(contents == null){
    		return;
    	}
    	
    	FileOutputStream out = null;
    	try {
    		out = new FileOutputStream(new File(fileName), false);
    		contents = contents.trim();
    		out.write(contents.getBytes());
    		out.flush();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		if(out != null){
    			try { out.close(); }catch(Exception ex){}
    		}
    	}
    }
  
	static public String selectFileDialog(Composite parent, String title, String [] names, String [] extensions){
        PreferenceManager prefManager = PreferenceManager.get();
		FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
		dialog.setText(title);
		dialog.setFilterNames(names);
		dialog.setFilterExtensions(extensions);
		dialog.setFilterPath(prefManager.getPreference(title, "."));
		String fileName = dialog.open();
		if(fileName != null){
			File file = new File(fileName);
            prefManager.setPreference(title, file.getParentFile().getPath());			
		}
		return fileName;		
	}
	
    static public boolean isZipFile(String fileName){
    	if(fileName == null){
    		return false;
    	}
    	File file = new File(fileName);
    	if(!file.exists() || !file.isFile()){
    		return false;
    	}
    	InputStream in = null;
    	try {
    		in = new FileInputStream(file);
    		byte [] data = new byte[2];
    		in.read(data);
    		if(data[0] == 0x50 && data[1] ==0x4b){
    			return true;
    		}
    	}catch(Exception ex){
    		return false;
    	}finally {
    		if(in!= null){
    			try {in.close(); }catch(Exception e){}
    		}
    	}
    	return false;
    }    
 }
