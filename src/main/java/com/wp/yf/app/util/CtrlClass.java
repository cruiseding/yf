package com.wp.yf.app.util;

import java.io.File;

public class CtrlClass {
	
	static String path = CtrlClass.class.getClassLoader().getResource("/").getFile();
	static File file1 = new File(path.substring(1, path.length() - 1));
	
	protected static File file = new File(new File(file1.getParent()).getParent());
	// protected static File file=new File(file2.getParent());

}
