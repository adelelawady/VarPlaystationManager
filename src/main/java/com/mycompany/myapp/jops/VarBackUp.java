package com.mycompany.myapp.jops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VarBackUp {
    /* @Scheduled(cron = "1 * * * * *")
	public void scheduleTaskUsingCronExpression() {

		URL path = VarBackUp.class.getResource("mongodb-url.txt");
		File inputFile = new File("mongodb-url.txt");
		try {
			Scanner reader = new Scanner(inputFile);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				System.out.println(data);

			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("scanner error");
			e.printStackTrace();
		}
		
		String command="mongodump --forceTableScan -d er -o /Users/mymac/Documents/db_dumb";
		try {
			Process p = Runtime.getRuntime().exec(command);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		long now = System.currentTimeMillis() / 1000;
		System.out.println("schedule tasks using cron jobs - " + now);
	}

*/

}
