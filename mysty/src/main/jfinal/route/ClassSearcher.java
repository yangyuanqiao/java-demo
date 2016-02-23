package main.jfinal.route;

import java.io.File;

import java.net.URL;

import java.util.ArrayList;

import java.util.List;
/**
 * �����
 * @author loyin
 *  2012-9-4 ����11:42:47
 */
public class ClassSearcher {
	
	private static List<File> classFiles = new ArrayList<File>();
	/**
	 * �ݹ�����ļ�
	 * @param baseDirName
	 *            ���ҵ��ļ���·��
	 * @param targetFileName
	 *            ��Ҫ���ҵ��ļ���
	 */
	public static List<File> findFiles(String baseDirName,
			String targetFileName) {
		/**
		 * �㷨������ ��ĳ������������ҵ��ļ��г������������ļ��е��������ļ��м��ļ���
		 * ��Ϊ�ļ��������ƥ�䣬ƥ��ɹ��������������Ϊ���ļ��У�������С� ���в��գ��ظ�d��������������Ϊ�գ�������������ؽ����
		 */
		String tempName = null;
		// �ж�Ŀ¼�Ƿ����
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.out.println("�ļ�����ʧ�ܣ�" + baseDirName + "����һ��Ŀ¼��");
		} else {
			File[] filelist = baseDir.listFiles();
			if(filelist!=null)
			for(File f:filelist){
				if(f.isDirectory()==true){
					findFiles(f.getPath(),targetFileName);
				}else{
					tempName = f.getName();
					if (ClassSearcher.wildcardMatch(targetFileName, tempName)) {
						classFiles.add(f.getAbsoluteFile());
					}
				}
			}
		}
		return classFiles;
	}
	/**
	 * ͨ���������
	 * @param clazz ����
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> findClasses(Class clazz) {
		List<Class> classList = new ArrayList<Class>();
		URL classPathUrl = ClassSearcher.class.getResource("/");
		List<File> classFileList = findFiles(classPathUrl.getFile(), "*.class");
//		String lib = new File(classPathUrl.getFile()).getParent() + "/lib/";
		for (File classFile : classFileList) {
			String className = className(classFile, "/classes");
			try {
				Class<?> classInFile = Class.forName(className);
				if (classInFile.getSuperclass() == clazz) {
					classList.add(classInFile);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classList;
	}
	@SuppressWarnings("rawtypes")
	public static List<Class> findClasses() {
		List<Class> classList = new ArrayList<Class>();
		URL classPathUrl = ClassSearcher.class.getResource("/");
		List<File> classFileList = findFiles(classPathUrl.getFile(), "*.class");
//		String lib = new File(classPathUrl.getFile()).getParent() + "/lib/";
		for (File classFile : classFileList) {
			String className = className(classFile, "/classes");
			try {
				Class<?> classInFile = Class.forName(className);
				classList.add(classInFile);
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		
		return classList;
	}
	private static String className(File classFile, String pre) {
		String objStr = classFile.toString().replaceAll("\\\\", "/");
		String className;
		className = objStr.substring(objStr.indexOf(pre) + pre.length(),
		objStr.indexOf(".class"));
		if (className.startsWith("/")) {
			className = className.substring(className.indexOf("/") + 1);
		}
		return className.replaceAll("/", ".");
	}
	/**
	 * ͨ���ƥ��
	 * @param pattern
	 *            ͨ���ģʽ
	 * @param str
	 *            ��ƥ����ַ���
	 * @return ƥ��ɹ��򷵻�true�����򷵻�false
	 */
	private static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				// ͨ����Ǻ�*��ʾ����ƥ���������ַ�
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1),
					str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				// ͨ����ʺ�?��ʾƥ������һ���ַ�
				strIndex++;
				if (strIndex > strLength) {
					// ��ʾstr���Ѿ�û���ַ�ƥ��?�ˡ�
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}
}