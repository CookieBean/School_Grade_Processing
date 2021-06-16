int cur_ind, status, cur_sem_ind, cur_static_ind, cur_input_ind;
PFont font;
String[] sem_list;
String cur_sem, btn_name;
Table semester_info, semester, idTable;
;
Button change_mode, change_mode_, input_mode, plus, cancel;
ArrayList<ArrayList<Button>> select_plus;
Tab[] tabs, tabs_graph, tabs_input;
Table[] grade_table;
dot_graph dgraph;
Boolean toggle, fflag;

int ms1;
int ms2;
int dt;

void setup() {
  //println(Boolean.valueOf("false"));
  background(255);
  font = createFont("D2CodingBold-Ver1.3.2-20180524.ttf", 32);
  //println(PFont.list());
  size(1580, 770);
  rectMode(CORNERS);
  fill(127);
  textFont(font);
  toggle = false;
  
  sem_list = new String[6];
  sem_list[0] = "1-1";
  sem_list[1] = "1-2";
  sem_list[2] = "2-1";
  sem_list[3] = "2-2";
  sem_list[4] = "3-1";
  sem_list[5] = "3-2";

  change_mode = new Button(890, 30, 1090, 75, 20, "누적 학기 모드");
  change_mode_ = new Button(890, 30, 1090, 75, 20, "현재 학기 모드");
  input_mode = new Button(580, 30, 780, 75, 20, "학점 입력하기");
  plus = new Button(500, 30, 725, 75, 20, "과목 추가 +");
  cancel = new Button(1350, (int)((float)43 * height / 48), 1550, (int)((float)47 * height / 48), 25, "돌아가기", color(127, 127, 255));

  tabs_graph = new Tab[13];
  tabs_graph[0] = new Tab("학기별 평점", 0, 200, (int)((float)0 * height / 13), (int)((float)(0+1) * height / 13), color(255, 255, 255));
  tabs_graph[1] = new Tab("국어과", 0, 200, (int)((float)1 * height / 13), (int)((float)(1+1) * height / 13), color(255, 255, 255));
  tabs_graph[2] = new Tab("사회과", 0, 200, (int)((float)2 * height / 13), (int)((float)(2+1) * height / 13), color(255, 255, 255));
  tabs_graph[3] = new Tab("외국어과", 0, 200, (int)((float)3 * height / 13), (int)((float)(3+1) * height / 13), color(255, 255, 255));
  tabs_graph[4] = new Tab("예술체육과", 0, 200, (int)((float)4 * height / 13), (int)((float)(4+1) * height / 13), color(255, 255, 255));
  tabs_graph[5] = new Tab("수학과", 0, 200, (int)((float)5 * height / 13), (int)((float)(5+1) * height / 13), color(255, 255, 255));
  tabs_graph[6] = new Tab("물리학과", 0, 200, (int)((float)6 * height / 13), (int)((float)(6+1) * height / 13), color(255, 255, 255));
  tabs_graph[7] = new Tab("화학과", 0, 200, (int)((float)7 * height / 13), (int)((float)(7+1) * height / 13), color(255, 255, 255));
  tabs_graph[8] = new Tab("생명과학과", 0, 200, (int)((float)8 * height / 13), (int)((float)(8+1) * height / 13), color(255, 255, 255));
  tabs_graph[9] = new Tab("지구과학과", 0, 200, (int)((float)9 * height / 13), (int)((float)(9+1) * height / 13), color(255, 255, 255));
  tabs_graph[10] = new Tab("정보과학과", 0, 200, (int)((float)10 * height / 13), (int)((float)(10+1) * height / 13), color(255, 255, 255));
  tabs_graph[11] = new Tab("공학과", 0, 200, (int)((float)11 * height / 13), (int)((float)(11+1) * height / 13), color(255, 255, 255));
  tabs_graph[12] = new Tab("프로젝트 기반연구", 0, 200, (int)((float)12 * height / 13), (int)((float)(12+1) * height / 13), color(255, 255, 255));

  Table[] grade_table = new Table[5];
  grade_table[0] = loadTable("1-1.csv", "header");
  grade_table[1] = loadTable("1-2.csv", "header");
  grade_table[2] = loadTable("2-1.csv", "header");
  grade_table[3] = loadTable("2-2.csv", "header");
  grade_table[4] = loadTable("3-1.csv", "header");
  idTable = loadTable("subject_id.csv", "header");

  select_plus = new ArrayList<ArrayList<Button>>();

  for (int i=1; i<=12; i++) {
    ArrayList<Button> t = new ArrayList<Button>();
    int cnt = 0, j = 0;
    for (TableRow tr : idTable.findRows(str(i), "id")) cnt++;
    //println(cnt);
    for (TableRow tr : idTable.findRows(str(i), "id")) {
      t.add(new Button(10 + j*98, (int)((float)(i*4 - 3) * height / 48), 100 + j*98, (int)((float)(i*4 - 1) * height / 48), 10, tr.getString("subject")));
      j++;
    }
    //for(Button b:t) print(b.name + " ");
    //println();
    select_plus.add(t);
  }

  tabs_input = new Tab[5];
  for (int i=0; i<5; i++) {
    tabs_input[i] = new Tab(sem_list[i] + " 학기", 0, 200, (int)((float)i * height / 5), (int)((float)(i+1) * height / 5), color(255, 255, 255));
    tabs_input[i].sub = new Subject(sem_list[i] + " 학기", grade_table[i], 250, 1600);
  }

  for (int i=0; i<13; i++) {
    tabs_graph[i].dgraph = new dot_graph(300, 1525, 120, 700, 0, 10, 0, 4.3, i);
  }

  for (int i=0; i<tabs_input.length; i++) {
    tabs_input[i].sub.show_();
  }
  
  tabs_graph[cur_static_ind].col = color(127, 255, 127);
  tabs_input[cur_input_ind].col = color(127, 255, 127);
  status = 0;
}

void init() {
  semester_info = loadTable(cur_sem + "_info.csv", "header");
  
  tabs = new Tab[semester_info.getRowCount()];
  int tmp = 0;
  for (TableRow row : semester_info.rows()) {
    String name = row.getString("subject");
    Boolean exam_1 = Boolean.valueOf(row.getString("exam_1"));
    Boolean exam_2 = Boolean.valueOf(row.getString("exam_2"));
    int exam_per = Integer.parseInt(row.getString("exam_per"));
    int quiz_cnt = Integer.parseInt(row.getString("quiz_cnt"));
    float quiz_unit = Float.parseFloat(row.getString("quiz_unit"));
    int quiz_score = Integer.parseInt(row.getString("quiz_score"));
    float task_unit = Float.parseFloat(row.getString("task_unit"));
    String task_scores = row.getString("task_scores");
    String[] sep = task_scores.split("/");
    int[] tasks = new int[sep.length];
    color col = color(255, 255, 255);
    tabs[tmp] = new Tab(name, 0, 250, (int)((float)tmp * height / tabs.length), (int)((float)(tmp+1) * height / tabs.length), col);
    if (task_scores == "") {
      tabs[tmp].sub = new Subject(name, exam_1, exam_2, exam_per, quiz_cnt, quiz_unit, quiz_score, task_unit, semester, cur_sem);
    } else {
      for (int i=0; i<sep.length; i++) {
        tasks[i] = Integer.parseInt(sep[i]);
      }
      tabs[tmp].sub = new Subject(name, exam_1, exam_2, exam_per, quiz_cnt, quiz_unit, quiz_score, task_unit, semester, cur_sem, tasks);
    }
    tmp++;
  }
  tabs[cur_ind].col = color(127, 255, 127);
}

void draw() {
  ms1 = millis();
  background(255);
  rectMode(CORNERS);
  int x = width/2, y = height/2;
  textAlign(CENTER);
  stroke(0);
  switch(status) {
  case 0:
    textSize(30);
    fill(0);
    text("현재 학기를 선택하세요", x, y-50);
    textSize(20);
    for (int i=0; i<6; i++) {
      int tmpx = x-350 + 120*i, tmpX = x-250 + 120*i;
      if (tmpx <= mouseX && mouseX <= tmpX && y <= mouseY && mouseY <= y+50) {
        fill(200);
      } else fill(0, 0);
      rect(tmpx, y+50, tmpX, y);
      fill(0);
      text(sem_list[i], x-300 + 120*i, y+35);
    }
    break;
  case 1:
    textAlign(LEFT);
    textSize(20);
    text("현재 학기 : " + cur_sem, 300, 60);
    text("현재 과목 : " + tabs[cur_ind].name, 500, 60);
    textAlign(CENTER);
    //text("누적 학기 모드", 900, 60);
    //change_mode.highlighted();
    //change_mode.show();

    if (toggle) fill(0, 100);
    else fill(0);
    text("현재 학기 모드", 890, 60);
    if (!toggle) fill(0, 100);
    else fill(0);
    text("누적 학기 모드", 1120, 60);

    fill(255);
    strokeWeight(2);
    rect(970, 35, 1040, 70);
    strokeWeight(1);
    noStroke();
    fill(255, 127, 127);
    if (!toggle) rect(975, 40, 1005, 65);
    else rect(1005, 40, 1035, 65);
    stroke(0);

    tabs[cur_ind].sub.show();
    for (int i=0; i<tabs.length; i++) {
      tabs[i].show();
      tabs[i].chkMouse();
    }
    break;
  case 2:
    textAlign(LEFT);
    text("현재 통계 : " + tabs_graph[cur_static_ind].name, 300, 60);
    //text("누적 학기 모드", 620, 60);
    for (int i=0; i<tabs_graph.length; i++) {
      tabs_graph[i].show();
      tabs_graph[i].chkMouse();
    }
    //change_mode_.highlighted();
    //change_mode_.show();

    if (toggle) fill(0, 100);
    else fill(0);
    text("현재 학기 모드", 890, 60);
    if (!toggle) fill(0, 100);
    else fill(0);
    text("누적 학기 모드", 1120, 60);

    fill(255);
    strokeWeight(2);
    rect(970, 35, 1040, 70);
    strokeWeight(1);
    noStroke();
    fill(255, 127, 127);
    if (!toggle) rect(975, 40, 1005, 65);
    else rect(1005, 40, 1035, 65);
    stroke(0);

    input_mode.highlighted();
    input_mode.show();

    tabs_graph[cur_static_ind].dgraph.show();
    break;
  case 3:
    textAlign(LEFT);
    textSize(20);
    text("현재 학기 : " + sem_list[cur_input_ind], 250, 60);
    for (int i=0; i<tabs_input.length; i++) {
      tabs_input[i].show();
      tabs_input[i].chkMouse();
    }

    change_mode.highlighted();
    change_mode.show();

    plus.highlighted();
    plus.show();

    tabs_input[cur_input_ind].sub.show_();
    break;
  case 4:
    for (ArrayList<Button> al : select_plus) {
      for (Button bt : al) {
        bt.show();
        bt.highlighted();
      }
    }
    cancel.highlighted();
    cancel.show();
    break;
  default:
    break;
  }
  ms2 = millis();
  //println(max(ms2 - ms1, 60/1000));
}

void mousePressed() {
  int x = width/2, y = height/2;
  switch(status) {
  case 0:
    for (int i=0; i<6; i++) {
      int tmpx = x-350 + 120*i, tmpX = x-250 + 120*i;
      if (tmpx <= mouseX && mouseX <= tmpX && y <= mouseY && mouseY <= y+50) {
        status = 1;
        cur_sem = sem_list[i];
        cur_sem_ind = i;
        init();
        
      }
    }
    break;
  case 1:
    if (975 <= mouseX && mouseX <= 1040 && 35 <= mouseY && mouseY <= 70) {
      toggle = !toggle;
      status = 2;
      for (int i=0; i<tabs_graph.length; i++) tabs_graph[i].dgraph.init();
      break;
    } 
    //change_mode.onClick(new onClickListner() {
    //  public void func() {
    //    status = 2;
    //    for(int i=0;i<tabs_graph.length;i++) tabs_graph[i].dgraph.init();
    //  }
    //});
    if (status != 1) break;
    for (int i=0; i<tabs.length; i++) {
      Boolean chk = tabs[i].click();
      if (chk) {
        tabs[cur_ind].col = color(255, 255, 255);
        cur_ind = i;
        tabs[cur_ind].col = color(127, 255, 127);
      }
    }
    tabs[cur_ind].sub.lock_chk();
    semester = new Table();
    semester = new Table();
    semester.addColumn("subject");
    semester.addColumn("exam_1");
    semester.addColumn("exam_2");
    semester.addColumn("quiz_score");
    semester.addColumn("task_score");
    semester.addColumn("grade");

    for (int i=0; i<tabs.length; i++) {
      tabs[i].sub.saveRow(semester);
    }
    saveTable(semester, "data/" + cur_sem + "_score.csv");
    break;
  case 2:
    if (975 <= mouseX && mouseX <= 1040 && 35 <= mouseY && mouseY <= 70) {
      toggle = !toggle;
      status = 1;
      break;
    } 
    for (int i=0; i<tabs_graph.length; i++) {
      Boolean chk = tabs_graph[i].click();
      if (chk) {
        tabs_graph[cur_static_ind].col = color(255, 255, 255);
        cur_static_ind = i;
        tabs_graph[cur_static_ind].col = color(127, 255, 127);
        tabs_graph[i].dgraph.time_init();
      }
    }
    //change_mode_.onClick(new onClickListner() {
    //  public void func() {
    //    status = 1;
    //  }
    //});
    input_mode.onClick(new onClickListner() {
      public void func() {
        status = 3;
      }
    }
    );
    break;
  case 3:
    change_mode.onClick(new onClickListner() {
      public void func() {
        status = 2;
        for (int i=0; i<tabs_graph.length; i++) tabs_graph[i].dgraph.init();
      }
    }
    );
    if (status != 3) break;
    plus.onClick(new onClickListner() {
      public void func() {
        status = 4;
        for (ArrayList<Button> al : select_plus) {
          for (Button b : al) {
            if (tabs_input[cur_input_ind].sub.sem_grade_exist(b.name)) {
              b.col = color(255, 127, 127);
              b.disabled = true;
            } else {
              b.col = color(255, 255, 255);
              b.disabled = false;
            }
          }
        }
      }
    }
    );
    if (status != 3) break;
    for (int i=0; i<tabs_input.length; i++) {
      Boolean chk = tabs_input[i].click();
      if (chk) {
        tabs_input[cur_input_ind].col = color(255, 255, 255);
        tabs_input[i].col = color(127, 255, 127);
        cur_input_ind = i;
      }
    }
    tabs_input[cur_input_ind].sub.lock_chk();
    for (int i=0; i<tabs_input.length; i++) {
      tabs_input[i].sub.saveGrade();
    }
    break;
  case 4:
    cancel.onClick(new onClickListner() {
      public void func() {
        status = 3;
      }
    }
    );
    for (ArrayList<Button> al : select_plus) {
      for (Button bt : al) {
        btn_name = bt.name;
        fflag = bt.disabled;
        bt.onClick(new onClickListner() {
          public void func() {
            tabs_input[cur_input_ind].sub.sem_grade_add(btn_name);
          }
        }
        );
        if (bt.Clicked()) {
          if (fflag) {
            bt.disabled = false;
            bt.col = color(255, 255, 255);
            int for_lim = tabs_input[cur_input_ind].sub.sem_grade.size();
            for (int i=0; i<for_lim; i++) {
              Score s = tabs_input[cur_input_ind].sub.sem_grade.get(i);
              println(s.name, btn_name);
              if (s.name.equals(btn_name)) {
                println(tabs_input[cur_input_ind].sub.sem_grade.get(i).name);
                tabs_input[cur_input_ind].sub.sem_grade_delete(i);
                for_lim--;
              }
            }
          } else {
            bt.disabled = true;
            bt.col = color(255, 127, 127);
          }
        }
      }
    }
    for (int i=0; i<tabs_input.length; i++) {
      tabs_input[i].sub.saveGrade();
    }
  default:
    break;
  }
}

void keyPressed() {
  switch(status) {
  case 0:
    break;
  case 1:
    break;
  case 2:
    break;
  case 3:
    if (key == 'd' || key == 'ㅇ' || key == 'D') {
      for (int i=0; i<tabs_input[cur_input_ind].sub.sem_grade.size(); i++) {
        Score s = tabs_input[cur_input_ind].sub.sem_grade.get(i);
        if (s.mouseOver()) {
          tabs_input[cur_input_ind].sub.sem_grade_delete(i);
        }
      }
      for (int i=0; i<tabs_input.length; i++) {
        tabs_input[i].sub.saveGrade();
      }
    }
    break;
  case 4:
    break;
  default:
    break;
  }
}
