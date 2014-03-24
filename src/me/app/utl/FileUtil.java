package me.app.utl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.app.mdl.Row;

public class FileUtil {
	private static ArrayList<Row> rows = new ArrayList<Row>();
	public static ArrayList<Row> readFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] datas = line.split(",");
				
				if (datas.length == 4 && Character.isDigit(datas[2].charAt(0))) {
					Row tmpRow = new Row();
					tmpRow.setUid(datas[0]);
					tmpRow.setBid(datas[1]);
					tmpRow.setType(Integer.valueOf(datas[2]));
					tmpRow.setDate(datas[3]);
					
					rows.add(tmpRow);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rows;
	}
}
