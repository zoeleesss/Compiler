package changeType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import changeType.ChangeType;

public class SaveFile {
	public static void saveCpp(){
		FileOutputStream fop = null;
		File file;
		String content = ChangeType.getFile();
		try {
			file = new File("/Users/njy97/Desktop/a.cpp");
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			System.out.println("======File Saved======");
			  
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fop != null) {
					fop.close();
			    }
			}
			catch (IOException e) {
			    e.printStackTrace();
			}
		}
	}
}
