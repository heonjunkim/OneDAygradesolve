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
import com.biz.grade.domain.ScoreVO;
import com.biz.grade.domain.StudentVO;



	public class ScoreServiceImplV12 implements ScoreService {

		private List<StudentVO> studentList;
		private List<ScoreVO> scoreList;
		private Scanner scan;
		private String fileName;

		StudentService stService;



		public ScoreServiceImplV12() {
			scoreList = new ArrayList<ScoreVO>();
			scan = new Scanner(System.in);
			fileName = "src/com/biz/grade/exec/data/score.txt";

			stService = new StudentServiceImplV1();
			stService.loadStudent();
			// StudentService로부터 studentList를 추출하여
			// 사용할 준비를 하자
			studentList = stService.getStudentList();
		}

		// studentList를 외부에서 가져다 사용할수 있도록 통로를 설정
		public List<StudentVO> getStudentList() {
			return stList;
		}

		@Override
		public void loadScore() {
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
					String[] scores = reader.split(":");
					ScoreVO scoreVO = new ScoreVO();
					scoreVO.setNum(scores[DBContract.SCORE.SC_NUM]);
					scoreVO.setKor(Integer.valueOf(scores[DBContract.SCORE.SC_KOR]));
					scoreVO.setEng(Integer.valueOf(scores[DBContract.SCORE.SC_ENG]));
					scoreVO.setMath(Integer.valueOf(scores[DBContract.SCORE.SC_MATH]));
					scoreVO.setMusic(Integer.valueOf(scores[DBContract.SCORE.SC_MUSIC]));
					scoreList.add(scoreVO);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("학생정보 파일 열기 오류!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("학생정보 파일 읽기 오류!");
			}
		}
		// return type을 int(Privitive) 가 아닌 integer(Wrapper Class)로 설정

		// sc_score(매개변수)로 전달받은 값을 검사하는 코드
		// END 문자열을 받았으면 -1을 return하고 숫자로 바꿀 수 없는 문자열, 점수범위를 벗어나는 값이면 null을 return
		// 정사적이면 문자열을 정수로 바꾸어 return
		private Integer scoreCheck(String sc_score) {

			// 만약 END를 입력했으면 -1을 return해라
			if (sc_score.equals("END")) {
				return -1;
			}

			/*
			 * int intScore = null; : 오류가 발생하는 코드 Primitive int 형 변수는 null값으로 clear, 초기화를 할
			 * 수 없다.
			 * 
			 * Integer intScore = null; : 정상적인 코드 Wrapper Class Integer형 변수는 null값으로 clear,
			 * 초기화 할 수 있다.
			 */
			Integer intScore = null;
			try {
				intScore = Integer.valueOf(sc_score);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("점수는 숫자반 가능합니다.");
				System.out.println("입력한 문자열 : " + sc_score);
				return null;
			}
			if (intScore < 0 || intScore > 100) {
				System.out.println("학번은 0 ~ 100 까지만 가능합니다.");
				System.out.println("다시 입력해주세요 ");
				return null;
			}

			return intScore;
		}

		@Override
		public boolean inputScore() {

			ScoreVO scoreVO = new ScoreVO();

			System.out.print("학번 (종료 : END) >> ");
			String st_num = scan.nextLine();

			if (st_num.equals("END")) {
				return false;
			}

			int intNum = 0;
			try {
				intNum = Integer.valueOf(st_num);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("학번은 숫자반 가능합니다.");
				System.out.println("입력한 문자열 : " + st_num);
				return true;
			}
			if (intNum < 1 || intNum > 99999) {
				System.out.println("학번은 1 ~ 99999 까지만 가능합니다.");
				System.out.println("다시 입력해주세요 ");
				return true;
			}
			// 00001 형식으로 만들기
			st_num = String.format("%05d", intNum);

			for (ScoreVO sVO : scoreList) {
				if (sVO.getNum().equals(st_num)) {
					System.out.println(st_num + "학생의 성적이 이미등록되있습니다.");
				}
			}

			// 학생정보에서 학번이 등록되어 있는지 확인
			scoreVO.setNum(st_num);

			// 과목명을 문자열 배열로 선언하고, 과목명 문자열 배열 개수만큼 점수를 담을 intScores 배열을 선언한다.
			String[] strSubjects = { "국어", "영어", "수학", "음악" };
			Integer[] intScores = new Integer[strSubjects.length];

			for (int i = 0; i < strSubjects.length; i++) {
				System.out.printf("%s 점수 (END : 종료) >> ", strSubjects[i]);
				String sc_score = scan.nextLine();

				// intScore -1, null, 숫자값이 담겨지게 된다.
				Integer intScore = this.scoreCheck(sc_score);

				if (intScore == null) { // 입력값이 오류!
					// 만약 입력한 점수가 오류(문자열, 범위)가 발생했다면
					// for() 반복문의 i값을 -1하여 감소시켜주고 다시 for()문을 시작하도록 한다.
					// 국어점수에서 이러한 일이 발생한다면 계속해서 국어점수를 입력받는 화면이 반복해서 나타날 것이다.
					i--;
					continue;
				} else if (intScore < 0) {
					return false;
				}
				// 모든 것이 정상이면 점수배열에 값을 저장하자
				intScores[i] = intScore;
			}

			scoreVO.setKor(intScores[0]);
			scoreVO.setEng(intScores[1]);
			scoreVO.setMath(intScores[2]);
			scoreVO.setMusic(intScores[3]);
			scoreList.add(scoreVO);
			this.saveScoreVO(scoreVO); // 1명의 데이터를 추가 저장하기
			return true;
		}

		@Override
		public void saveScore() {
		}

		@Override
		public void scoreList() {
			System.out.println(Lines.dLine);
			System.out.println("성적 일람표");
			System.out.println(Lines.dLine);
			System.out.println("학과\t|이름\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|");
			System.out.println(Lines.sLine);
			for (ScoreVO sVO : scoreList) {
				System.out.printf("%s\t|", sVO.getNum());
				System.out.printf("%s\t|", "이름");
				System.out.printf("%d\t|", sVO.getKor());
				System.out.printf("%d\t|", sVO.getEng());
				System.out.printf("%d\t|", sVO.getMath());
				System.out.printf("%d\t|", sVO.getMusic());
				System.out.printf("%d\t|", sVO.getSum());
				System.out.printf("%5.2f\t|\n", sVO.getAvg());
			}
			System.out.println(Lines.dLine);
		}
		
			

		@Override
		public void saveScoreVO(ScoreVO scoreVO) {
			FileWriter fileWriter = null;
			PrintWriter pWriter = null;
			try {
				fileWriter = new FileWriter(this.fileName, true);
				pWriter = new PrintWriter(fileWriter);
				pWriter.printf("%s:", scoreVO.getNum());
				pWriter.printf("%d:", scoreVO.getKor());
				pWriter.printf("%d:", scoreVO.getEng());
				pWriter.printf("%d:", scoreVO.getMath());
				pWriter.printf("%d:", scoreVO.getMusic());

				pWriter.flush();
				pWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
