package com.biz.grade.exec;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public class PrintStream_01 {
	public static void main(String[] args) {

		String fileName = "src/com/biz/grade/exec/data/test.1txt";
		FileWriter filewriter = null;
		PrintWriter pSeam = null;
		try {

			// PrintSteam() 으로 파일을 기록하기 위해 Open
			// 기존 파일이 삭제되고 새로 생성
			/*
			 * PintWriter는 보통 단독으로 사용하지 않고 FIleWriter로 파일을 open한 후 PrintWriter에 연결하여 사용한다.
			 * FileWriter로 파일을 open할 때 두번째 매개변수로 true 값을 주입하면 
			 * 		파일을 append mode로 open 한다.
			 * 		append mode로 open이 되면 기존에 저장된 내용을 삭제하지 않고
			 * 		계속해서 문자열을 추가하는 상태로 변경된다.
			 */

			filewriter = new FileWriter(fileName, true);
			pSeam = new PrintWriter(filewriter);

			Date date = new Date();
			pSeam.println("대한민국만세 :" + date.toString());

			// PrintWriter는 값을 저장하면 일단 임시 buffer에 보관이 된다.
			// flush() method를 호출하여 buffer에 담긴 데이터를
			// 파일로 보낸후 close() 해줘야 한다.
			pSeam.flush();
			pSeam.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
