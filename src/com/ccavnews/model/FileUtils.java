package com.ccavnews.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtils {
	private static String filePath = MyApplication.getAppContext()
			.getFilesDir().getPath();

	public String getFilePath() {
		return filePath;
	}

	public boolean fileIsExists(String date) {
		try {
			File f = new File(filePath + date);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void writeFile(ArrayList<NewsItem> list, String date) {
		if (null == date)
			throw new RuntimeException("FileName is null!");

		try {
			FileOutputStream fos = MyApplication.getAppContext()
					.openFileOutput(date, android.content.Context.MODE_PRIVATE);
			ObjectOutputStream objectOutStream = new ObjectOutputStream(fos);
			objectOutStream.writeInt(list.size()); // Save size first
			for (NewsItem r : list)
				objectOutStream.writeObject(r);
			objectOutStream.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<NewsItem> readFile(String date) {
		if (null == date)
			return null;
		NewsItemList list = new NewsItemList();
		try {
			FileInputStream fis = MyApplication.getAppContext().openFileInput(
					date);
			ObjectInputStream objectInStream = new ObjectInputStream(fis);
			int count = objectInStream.readInt(); // Get the number of regions

			for (int c = 0; c < count; c++)
				list.add((NewsItem) objectInStream.readObject());
			objectInStream.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public boolean removeFile(String date) {
		if (null == date)
			return false;
		File file = new File(filePath + date);
		boolean deleted = file.delete();
		return deleted;
	}

}
