package com.biz.grade.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.grade.config.DBContract;
import com.biz.grade.config.Lines;
import com.biz.grade.domain.StudentVO;

public class StudentServiceImplV1 implements StudentService {
	private List<StudentVO> stList;
	private Scanner scan;
	private String fileName;

	public StudentServiceImplV1() {
		stList = new ArrayList<StudentVO>();
		scan = new Scanner(System.in);
		fileName = "src/com/biz/grade/exec/data/Student.txt";

	}

	@Override
	public void loadStudent() {

		FileReader fileReader = null;
		BufferedReader buffer = null;

		try {
			fileReader = new FileReader(this.fileName);
			buffer = new BufferedReader(fileReader);
			String reader = "";
			while (true) {
				reader = buffer.readLine();
				if (reader == null) {
					break;
				}
			}
			String[] students = reader.split(":");
			StudentVO stVO = new StudentVO();
			stVO.setNum(students[DBContract.STUDENT.ST_NUM]);
			stVO.setName(students[DBContract.STUDENT.ST_NAME]);
			stVO.setEpt(students[DBContract.STUDENT.ST_DEPT]);
			stVO.setGrade(Integer.valueOf(students[DBContract.STUDENT.ST_GRADE]));
			stVO.setTel(students[DBContract.STUDENT.ST_TEL]);
			stList.add(stVO);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("학생정보 파일열기 오류");

		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("학생정보파일읽기오류");
		}

	}

	@Override
	public boolean inputStudent() {
		
		StudentVO stVO = new StudentVO();
		
		System.out.print("학번(END:종료) >>");
		// 변수의 명명규칙
		// Camel case : 두 단어 이상 사용할때 단어 첫글자 대문자
		// snack case " 두 단어 이상 사용할때 단어 사이_(under score)
		
		// 넘버 시작
		String st_num = scan.nextLine();
		if(st_num.equals("END" )) {
			return false;
		}
		int intNum = 0;
		try {
			intNum = Integer.valueOf(st_num);
		} catch (Exception e) {
			System.out.println("학번은 숫자만 가능");
			System.out.println("입력한 문자열 :" +st_num);
			return true;
		}
		if(intNum < 1 || intNum > 99999) {
			System.out.println("학번은1~99999까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}
		// 00001 형식으로 만들기
		st_num = String.format("%05d", intNum);
		
		for(StudentVO sVO : stList) {
			if(sVO.getNum().contentEquals(st_num)) {
				System.out.println("학생정보가 학적부에 없음");
				System.out.println("성적을 입력할수가 없음");
				return true;
			}
		}
		stVO.setNum(st_num);
		
		
		System.out.print("이름(END로종료) >>");
		String st_name = scan.nextLine();
		if(st_name.equals("END")) {
			return false;
		}
		stVO.setName(st_name);
		
		
		
		System.out.print("학과(END로종료) >>");
		String st_ept = scan.nextLine();
		if(st_ept.equals("END")) {
			return false;
		}
		stVO.setEpt(st_ept);
		
		
		
		System.out.print("학년(END:종료) >>");
		String st_grade = scan.nextLine();
		if(st_grade.equals("END" )) {
			return false;
		}
		int intGrade = 0;
		try {
			intGrade = Integer.valueOf(st_grade);
		} catch (Exception e) {
			System.out.println("학년은 숫자만 가능");
			System.out.println("입력한 문자열 :" +st_grade);
			return true;
		}
		if(intGrade < 1 || intGrade > 99999) {
			System.out.println("학번은1~4까지만 가능");
			System.out.println("다시 입력해 주세요");
			return true;
		}
		// 00001 형식으로 만들기
		st_grade = String.format("%05d", intGrade);
		stVO.setGrade(Integer.valueOf(st_grade));
		
		
		System.out.print("전화번호: 010-111-1111 형식(END로종료) >>");
		String st_tel = scan.nextLine();
		if(st_tel.equals("END")) {
			return false;
		}
		stVO.setTel(st_tel);
		
		// List에 추가하기
		stList.add(stVO);
		// 파일에 저장하기
		this.saveStudent();
		
		return true;
	}

	@Override
	public void saveStudent() {
		
		FileWriter filewriter = null;
		PrintWriter pwriter = null;
		
		try {
			filewriter =  new FileWriter(this.fileName);
			pwriter = new PrintWriter(filewriter);
			
			// 내부의 writer buffer 에 값을 기록
			for(StudentVO stVO : stList ) {
				pwriter.printf("%s:", stVO.getNum());
				pwriter.printf("%s:", stVO.getName());
				pwriter.printf("%s:", stVO.getEpt());
				pwriter.printf("%d:", stVO.getGrade());
				pwriter.printf("%s\n", stVO.getTel());
			}
			// writer buffer 에 기록된 값을 파일에 저장 
			pwriter.flush();
			pwriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public void studentList() {
		System.out.println(Lines.dLine);
		System.out.println("학생 명부 리스트");
		System.out.println(Lines.dLine);
		System.out.println("학번\t|이름\t|학과\t|학년\t|전화번호");
		System.out.println(Lines.sLine);
		for(StudentVO sVO : stList) {
			System.out.printf("%s\t|", sVO.getNum());
			System.out.printf("%s\t|", sVO.getName());
			System.out.printf("%s\t|", sVO.getEpt());
			System.out.printf("%s\t|", sVO.getGrade());
			System.out.printf("%s\t|", sVO.getTel());
		}
		System.out.println(Lines.dLine);
		

		
	}

}
