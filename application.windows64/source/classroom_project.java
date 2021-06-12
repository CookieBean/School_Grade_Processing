import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class classroom_project extends PApplet {

int cur_ind, status, cur_sem_ind, cur_static_ind, cur_input_ind;
PFont font;
String[] sem_list;
String cur_sem, btn_name;
Table semester_info, semester, idTable;;
Button change_mode, change_mode_, input_mode, plus, cancel;
ArrayList<ArrayList<Button>> select_plus;
Tab[] tabs, tabs_graph, tabs_input;
Table[] grade_table;
dot_graph dgraph;
Boolean toggle, fflag;

int ms1;
int ms2;
int dt;

public void setup() {
  //println(Boolean.valueOf("false"));
  background(255);
  font = createFont("D2CodingBold-Ver1.3.2-20180524.ttfs", 32);
  //println(PFont.list());
  
  rectMode(CORNERS);
  fill(127);
  textFont(font);
  toggle = false;
  
  semester_info = loadTable("semester_info.csv", "header");
  semester = new Table();
  
  sem_list = new String[6];
  sem_list[0] = "1-1";
  sem_list[1] = "1-2";
  sem_list[2] = "2-1";
  sem_list[3] = "2-2";
  sem_list[4] = "3-1";
  sem_list[5] = "3-2";
  
  tabs = new Tab[semester_info.getRowCount()];
  int tmp = 0;
  for(TableRow row:semester_info.rows()) {
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
    int col = color(255, 255, 255);
    tabs[tmp] = new Tab(name, 0, 250, (int)((float)tmp * height / tabs.length), (int)((float)(tmp+1) * height / tabs.length), col);
    if(task_scores == "") {
      tabs[tmp].sub = new Subject(name, exam_1, exam_2, exam_per, quiz_cnt, quiz_unit, quiz_score, task_unit, semester);
    } else {
      for(int i=0;i<sep.length;i++) {
        tasks[i] = Integer.parseInt(sep[i]);
      }
      tabs[tmp].sub = new Subject(name, exam_1, exam_2, exam_per, quiz_cnt, quiz_unit, quiz_score, task_unit, semester, tasks);
    }
    tmp++;
  }
  
  semester.addColumn("subject");
  semester.addColumn("exam_1");
  semester.addColumn("exam_2");
  semester.addColumn("quiz_score");
  semester.addColumn("task_score");
  semester.addColumn("grade");
  for(int i=0;i<tabs.length;i++) {
    tabs[i].sub.saveRow(false);
  }
  
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
  
  for(int i=1;i<=12;i++) {
    ArrayList<Button> t = new ArrayList<Button>();
    int cnt = 0, j = 0;
    for(TableRow tr:idTable.findRows(str(i), "id")) cnt++;
    //println(cnt);
    for(TableRow tr:idTable.findRows(str(i), "id")) {
      t.add(new Button(10 + j*98, (int)((float)(i*4 - 3) * height / 48), 100 + j*98, (int)((float)(i*4 - 1) * height / 48), 10, tr.getString("subject")));
      j++;
    }
    //for(Button b:t) print(b.name + " ");
    //println();
    select_plus.add(t);
  }
  
  tabs_input = new Tab[5];
  for(int i=0;i<5;i++) {
    tabs_input[i] = new Tab(sem_list[i] + " 학기", 0, 200, (int)((float)i * height / 5), (int)((float)(i+1) * height / 5), color(255, 255, 255));
    tabs_input[i].sub = new Subject(sem_list[i] + " 학기", grade_table[i], 250, 1600);
  }
  
  for(int i=0;i<13;i++) {
    tabs_graph[i].dgraph = new dot_graph(300, 1525, 120, 700, 0, 10, 0, 4.3f, i);
  }
  
  for(int i=0;i<tabs_input.length;i++) {
    tabs_input[i].sub.show_();
  }
  tabs[cur_ind].col = color(127, 255, 127);
  tabs_graph[cur_static_ind].col = color(127, 255, 127);
  tabs_input[cur_input_ind].col = color(127, 255, 127);
  status = 0;
}

public void draw() {
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
      for(int i=0;i<6;i++) {
        int tmpx = x-350 + 120*i, tmpX = x-250 + 120*i;
        if(tmpx <= mouseX && mouseX <= tmpX && y <= mouseY && mouseY <= y+50) {
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
      
      if(toggle) fill(0, 100);
      else fill(0);
      text("현재 학기 모드", 890, 60);
      if(!toggle) fill(0, 100);
      else fill(0);
      text("누적 학기 모드", 1120, 60);
      
      fill(255);
      strokeWeight(2);
      rect(970, 35, 1040, 70);
      strokeWeight(1);
      noStroke();
      fill(255, 127, 127);
      if(!toggle) rect(975, 40, 1005, 65);
      else rect(1005, 40, 1035, 65);
      stroke(0);
      
      tabs[cur_ind].sub.show();
      for(int i=0;i<tabs.length;i++) {
        tabs[i].show();
        tabs[i].chkMouse();
      }
      break;
    case 2:
      textAlign(LEFT);
      text("현재 통계 : " + tabs_graph[cur_static_ind].name, 300, 60);
      //text("누적 학기 모드", 620, 60);
      for(int i=0;i<tabs_graph.length;i++) {
        tabs_graph[i].show();
        tabs_graph[i].chkMouse();
      }
      //change_mode_.highlighted();
      //change_mode_.show();
      
      if(toggle) fill(0, 100);
      else fill(0);
      text("현재 학기 모드", 890, 60);
      if(!toggle) fill(0, 100);
      else fill(0);
      text("누적 학기 모드", 1120, 60);
      
      fill(255);
      strokeWeight(2);
      rect(970, 35, 1040, 70);
      strokeWeight(1);
      noStroke();
      fill(255, 127, 127);
      if(!toggle) rect(975, 40, 1005, 65);
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
      for(int i=0;i<tabs_input.length;i++) {
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
      for(ArrayList<Button> al:select_plus) {
        for(Button bt:al) {
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

public void mousePressed() {
  int x = width/2, y = height/2;
  switch(status) {
    case 0:
      for(int i=0;i<6;i++) {
        int tmpx = x-350 + 120*i, tmpX = x-250 + 120*i;
        if(tmpx <= mouseX && mouseX <= tmpX && y <= mouseY && mouseY <= y+50) {
          status = 1;
          cur_sem = sem_list[i];
          cur_sem_ind = i;
        }
      }
      break;
    case 1:
      if(975 <= mouseX && mouseX <= 1040 && 35 <= mouseY && mouseY <= 70) {
        toggle = !toggle;
        status = 2;
        for(int i=0;i<tabs_graph.length;i++) tabs_graph[i].dgraph.init();
        break;
      } 
      //change_mode.onClick(new onClickListner() {
      //  public void func() {
      //    status = 2;
      //    for(int i=0;i<tabs_graph.length;i++) tabs_graph[i].dgraph.init();
      //  }
      //});
      if(status != 1) break;
      for(int i=0;i<tabs.length;i++) {
        Boolean chk = tabs[i].click();
        if(chk) {
          tabs[cur_ind].col = color(255, 255, 255);
          cur_ind = i;
          tabs[cur_ind].col = color(127, 255, 127);
        }
      }
      tabs[cur_ind].sub.lock_chk();
      for(int i=0;i<tabs.length;i++) {
        tabs[i].sub.saveRow(true);
      }
      saveTable(semester, "data/current_semester.csv");
      break;
    case 2:
      if(975 <= mouseX && mouseX <= 1040 && 35 <= mouseY && mouseY <= 70) {
        toggle = !toggle;
        status = 1;
        break;
      } 
      for(int i=0;i<tabs_graph.length;i++) {
        Boolean chk = tabs_graph[i].click();
        if(chk) {
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
      });
      break;
    case 3:
      change_mode.onClick(new onClickListner() {
        public void func() {
          status = 2;
          for(int i=0;i<tabs_graph.length;i++) tabs_graph[i].dgraph.init();
        }
      });
      if(status != 3) break;
      plus.onClick(new onClickListner() {
        public void func() {
          status = 4;
          for(ArrayList<Button> al:select_plus) {
            for(Button b:al) {
              if(tabs_input[cur_input_ind].sub.sem_grade_exist(b.name)) {
                b.col = color(255, 127, 127);
                b.disabled = true;
              }
              else {
                b.col = color(255, 255, 255);
                b.disabled = false;
              }
            }
          }
        }
      });
      if(status != 3) break;
      for(int i=0;i<tabs_input.length;i++) {
        Boolean chk = tabs_input[i].click();
        if(chk) {
          tabs_input[cur_input_ind].col = color(255, 255, 255);
          tabs_input[i].col = color(127, 255, 127);
          cur_input_ind = i;
        }
      }
      tabs_input[cur_input_ind].sub.lock_chk();
      for(int i=0;i<tabs_input.length;i++) {
        tabs_input[i].sub.saveGrade();
      }
      break;
    case 4:
      cancel.onClick(new onClickListner() {
        public void func() {
          status = 3;
        }
      });
      for(ArrayList<Button> al:select_plus) {
        for(Button bt:al) {
          btn_name = bt.name;
          fflag = bt.disabled;
          bt.onClick(new onClickListner() {
            public void func() {
              tabs_input[cur_input_ind].sub.sem_grade_add(btn_name);
            }
          });
          if(bt.Clicked()) {
            if(fflag) {
              bt.disabled = false;
              bt.col = color(255, 255, 255);
              for(int i=0;i<tabs_input[cur_input_ind].sub.sem_grade.size();i++) {
                Score s = tabs_input[cur_input_ind].sub.sem_grade.get(i);
                if(s.name == btn_name) {
                  tabs_input[cur_input_ind].sub.sem_grade_delete(i);
                }
              }
            } else {
              bt.disabled = true;
              bt.col = color(255, 127, 127);
            }
          }
        }
      }
    default:
      break;
  }
}

public void keyPressed() {
  switch(status) {
    case 0:
      break;
    case 1:
      break;
    case 2:
      break;
    case 3:
      if(key == 'd' || key == 'ㅇ' || key == 'D') {
        for(int i=0;i<tabs_input[cur_input_ind].sub.sem_grade.size();i++) {
          Score s = tabs_input[cur_input_ind].sub.sem_grade.get(i);
          if(s.mouseOver()) {
            tabs_input[cur_input_ind].sub.sem_grade_delete(i);
          }
        }
        for(int i=0;i<tabs_input.length;i++) {
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
class Button {
  int sx, sy, ex, ey, text_sz;
  String name;
  int col;
  Boolean highlighted = false, disabled = false;
  Button(int sx_, int sy_, int ex_, int ey_, int text_sz_, String name_) {
    sx = sx_;
    sy = sy_;
    ex = ex_;
    ey = ey_;
    text_sz = text_sz_;
    name = name_;
    col = color(255, 255, 255);
  }
  Button(int sx_, int sy_, int ex_, int ey_, int text_sz_, String name_, int col_) {
    sx = sx_;
    sy = sy_;
    ex = ex_;
    ey = ey_;
    text_sz = text_sz_;
    name = name_;
    col = col_;
  }
  public void show() {
    if(highlighted) {
      strokeWeight(2);
      fill(200);
    } else {
      fill(col);
    }
    rectMode(CORNERS);
    rect(sx, sy, ex, ey);
    if(col != color(255, 255, 255)) fill(255, 255, 255);
    else fill(0);
    textSize(text_sz);
    if(name.length() > 8) {
      text(name.substring(0, 6) + "\n" + name.substring(6, name.length()), (sx+ex)/2, (sy+ey)/2 - 2);
    } else text(name, (sx+ex)/2, (sy+ey)/2 + 6);
    strokeWeight(1);
  }
  public void onClick(onClickListner listner) {
    if(disabled) return;
    if(sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey) {
      listner.func();
    }
  }
  public Boolean Clicked() {
    return sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey;
  }
  public void highlighted() {
    if(sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey) {
      highlighted = true;
    } else highlighted = false;
  }
}
class Score {
  int x, y, w, h, locked_mouseY = mouseY, locked_mouseX = mouseX;
  int col;
  float cur_score = 0, u, ratio, score;
  Boolean quantization, locked = false, highlighted = false, grade;
  String name;
  float[] cut_list;
  Score(int x_, int y_, int score_, int w_, int h_, Boolean q_, float u_, String name_, int col_, float ratio_) {
    x = x_;
    y = y_;
    h = h_;
    w = w_;
    score = score_;
    quantization = q_;
    name = name_;
    u = (float)round(u_*100)/100;
    col = col_;
    ratio = ratio_;
    grade = false;
  }
  Score(int x_, int y_, float score_, int w_, int h_, String name_, float[] cut_list_) {
    x = x_;
    y = y_;
    h = h_;
    w = w_;
    score = score_;
    quantization = true;
    name = name_;
    col = color(255, 127, 127);
    cut_list = new float[cut_list_.length];
    for(int i=0;i<cut_list_.length;i++) cut_list[i] = cut_list_[i];
    grade = true;
  }
  public void lock_chk() {
    if(x <= mouseX && mouseX <= x+w) {
      locked_mouseY = mouseY;
      locked_mouseX = mouseX;
      if(grade) show_();
      else show();
      //print(cur_score);
    }
  }
  public void highlight_chk() {
    if(x <= mouseX && mouseX <= x+w) {
      strokeWeight(2);
      highlighted = true;
    } else {
      highlighted = false;
      strokeWeight(1);
    }
  }
  public Boolean mouseOver() {
    return x <= mouseX && mouseX <= x+w && y <= mouseY && mouseY <= y+h;
  }
  public void show_() {
    rectMode(CORNERS);
    stroke(0);
    fill(0, 0, 0, 0);
    textSize(15);
    float unit = h / score;

    cur_score = 0;
    Boolean flag = true, flag_ = true, text_filled = false, text_filled_ = false;
    float tmp = 0;
    for(int i=1;i<cut_list.length;i++) {
      if(x <= locked_mouseX && locked_mouseX <= x+w && 
      locked_mouseY <= y+(unit * cut_list[i])) {
        fill(col);
        if(flag) {
          cur_score = score - cut_list[i-1];
          flag = false;
          text_filled = true;
        }
      } else {
        fill(0, 0, 0, 0);
      }
      if(x <= mouseX && mouseX <= x+w && 
      mouseY <= y+(unit * cut_list[i])) {
        if(flag_) {
          tmp = y+(unit * cut_list[i-1]);
          cur_score = score - cut_list[i-1];
          //println(tmp);
          flag_ = false;
          text_filled_ = true;
        }
      }
      
      rect(x, y+(unit * cut_list[i-1]), x+w, y+(unit * cut_list[i]));
      if(text_filled_) {
        fill(0, 100);
        text_filled_ = false;
      } else fill(0);
      if(text_filled) {
        fill(255, 0, 0);
        text_filled = false;
      }
      text(str((float)round((score - cut_list[i-1])*100)/100), x - 20, y+(unit * cut_list[i-1]));
    }
    fill(0);
    text("0", x - 20, y+(unit * score));
    
    if(x <= mouseX && mouseX <= x+w) {
      fill(127, 100);
      if(tmp > 0) rect(x, y+(unit * score), x+w, tmp);
    }
    textSize(12);
    fill(0);
    if(name.length() > 8) text(name.substring(0, 6) + "\n" + name.substring(6, name.length()), x+w/2, y+h+25);
    else text(name, x+w/2, y+h+40);
  }
  public void show() {
    rectMode(CORNERS);
    stroke(0);
    fill(0, 0, 0, 0);
    textSize(15);
    
    if(quantization) {
      int unit = h / (int)score;
      Boolean mx = true;
      if(!(x <= locked_mouseX && locked_mouseX <= x+w)) {
        cur_score = 0;
        mx = false;
      }
      Boolean flag = true, flag_ = true, text_filled = false, text_filled_ = false;
      float i = 0, tmp = 0;
      while(i < score) {
        if(x <= locked_mouseX && locked_mouseX <= x+w && 
        locked_mouseY <= y+(unit * (i+u))) {
          fill(col);
          if(flag) {
            if(!mx) cur_score = score-i;
            flag = false;
            text_filled = true;
          }
        } else {
          fill(0, 0, 0, 0);
        }
        if(x <= mouseX && mouseX <= x+w && 
        mouseY <= y+(unit * (i+u))) {
          if(flag_) {
            tmp = y+(unit * i);
            cur_score = score-i;
            //println(cur_score);
            flag_ = false;
            text_filled_ = true;
          }
        }
        
        rect(x, y+(unit * i), x+w, y+(unit * (i+u)));
        if(text_filled_) {
          fill(0, 100);
          text_filled_ = false;
        } else fill(0);
        if(text_filled) {
          fill(255, 0, 0);
          text_filled = false;
        }
        text(str((float)round(((float)score - i)*100)/100), x - 20, y+(unit * i));
        i += u;
        i = (float)round(i*100)/100;
      }
      fill(0);
      text("0", x - 20, y+(unit * score));
      
      if(x <= mouseX && mouseX <= x+w) {
        fill(127, 100);
        if(tmp > 0) rect(x, y+(unit * score), x+w, tmp);
      }
      
    } else {
      fill(0, 0, 0, 0);
      rect(x, y, x+w, y+h);
      if(x <= locked_mouseX && locked_mouseX <= x+w) {
        cur_score = (float)round(map(constrain(locked_mouseY, y, y+h), y+h, y, 0, score) * 100) / 100;
        if(highlighted) fill(255, 0, 0, 100);
        else fill(255, 0, 0);
        cur_score = (float)floor(cur_score / 0.5f) * 0.5f;
        cur_score = (float)round(cur_score * 100)/100;
        text(str(cur_score), x - 25, constrain(locked_mouseY, y, y+h));
        fill(col);
        rect(x, constrain(locked_mouseY, y, y+h), x+w, y+h);
      } else cur_score = 0;
      if(x <= mouseX && mouseX <= x+w) {
        
        cur_score = (float)round(map(constrain(mouseY, y, y+h), y+h, y, 0, score) * 100) / 100;
        cur_score = (float)floor(cur_score / 0.5f) * 0.5f;
        cur_score = (float)round(cur_score * 100)/100;
        fill(0);
        text(str(cur_score), x - 25, map(cur_score, 100, 0, y, y+h));
        fill(127, 100);
        rect(x, map(cur_score, 100, 0, y, y+h), x+w, y+h);
      }
    }
    textSize(15);
    fill(0);
    if(name.length() > 8) text(name.substring(0, 6) + "\n" + name.substring(6, name.length()), x+w/2, y+h+25);
    else text(name, x+w/2, y+h+40);
  }
}
class Subject {
  String name;
  int quiz_cnt, quiz_score, start_x = 325, end_x = 1425, exam_per, cnt;
  Boolean exam_1, exam_2;
  Table table, grade_table;
  int[] tasks;
  Score[] bars;
  ArrayList<Score> sem_grade;
  int[] colors;
  float quiz_unit, task_unit;
  float[] grade_list = {0.0f, 0.3f, 0.6f, 1.0f, 1.3f, 1.6f, 2.0f, 2.3f, 2.6f, 3.0f, 3.3f, 3.6f, 4.3f};
  Total total_score;
  Table timeTable;
  HashMap<String, Integer> timeMap = new HashMap<String, Integer>();
  subject_grade sg = new subject_grade(0, 0, "", "", "P");
  Subject(String name_, Boolean exam_1_, Boolean exam_2_, int exam_per_, int quiz_cnt_, float quiz_unit_, int quiz_score_, float task_unit_, Table table_, int... tasks_) {
    name = name_;
    quiz_cnt = quiz_cnt_;
    exam_1 = exam_1_;
    exam_2 = exam_2_;
    exam_per = exam_per_;
    quiz_unit = quiz_unit_;
    task_unit = task_unit_;
    table = table_;
    tasks = new int[tasks_.length];
    for(int i=0;i<tasks_.length;i++) {
      tasks[i] = tasks_[i];
    }
    quiz_score = quiz_score_;
    cnt = quiz_cnt + tasks_.length + PApplet.parseInt(exam_1) + PApplet.parseInt(exam_2);
    colors = new int[cnt];
    bars = new Score[cnt];
    int i = 0;
    while(i<quiz_cnt) {
      colors[i] = color((int)map(i, 0, cnt-1, 255, 127), 127, (int)map(i, 0, cnt-1, 127, 255));
      bars[i] = new Score(start_x + (end_x - start_x)/cnt * i, 100, quiz_score, 50, 600, true, quiz_unit, "퀴즈 " + str(i+1), colors[i], 1); i++;
    }
    if(exam_1) {
      colors[i] = color((int)map(i, 0, cnt-1, 255, 127), 127, (int)map(i, 0, cnt-1, 127, 255));
      bars[i] = new Score(start_x + (end_x - start_x)/cnt * i, 100, 100, 50, 600, false, 0, "시험 1회고사", colors[i], (float)exam_per/100); i++;
      //println(name, "asdf");
    }
    if(exam_2) {
      colors[i] = color((int)map(i, 0, cnt-1, 255, 127), 127, (int)map(i, 0, cnt-1, 127, 255));
      bars[i] = new Score(start_x + (end_x - start_x)/cnt * i, 100, 100, 50, 600, false, 0, "시험 2회고사", colors[i], (float)exam_per/100); i++;
      //println(name, "asdf");
    }
    while(i<cnt) {
      colors[i] = color((int)map(i, 0, cnt-1, 255, 127), 127, (int)map(i, 0, cnt-1, 127, 255));
      bars[i] = new Score(start_x + (end_x - start_x)/cnt * i, 100, tasks[i - quiz_cnt - PApplet.parseInt(exam_1) - PApplet.parseInt(exam_2)], 50, 600, true, task_unit, "수행평가 " + str(i + 1 - quiz_cnt - PApplet.parseInt(exam_1) - PApplet.parseInt(exam_2)), colors[i], 1); i++;
    }
    total_score = new Total(1395, 100, 100, 130, 600, false, 0, name + " 학점", colors);
  }
  Subject(String name_, Table grade_table_, int start_x_, int end_x_) {
    name = name_;
    grade_table = grade_table_;
    start_x = start_x_;
    end_x = end_x_;
    sem_grade = new ArrayList<Score>();
    for(int i=0;i<grade_table.getRowCount();i++) {
      TableRow tr = grade_table.getRow(i);
      sem_grade.add(new Score(start_x + (end_x - start_x)/grade_table.getRowCount() * i, 100, 4.3f, 50, 600, tr.getString("subject"), grade_list));
      Score tmp = sem_grade.get(i);
      tmp.locked_mouseX = tmp.x;
      sg.grade = tr.getString("grade");
      //println(sg.grade, tr.getString("subject"));
      tmp.locked_mouseY = (int)map(sg.getgrade() - 0.01f, 0, 4.3f, tmp.y+tmp.h, tmp.y);
      //println(i);
    }
    timeTable = loadTable("subject_id.csv", "header");
    
    for(TableRow row:timeTable.rows()) {
      timeMap.put(row.getString("subject"), Integer.parseInt(row.getString("time")));
    }
  }
  public void sem_grade_add(String subject_name) {
    sem_grade.add(new Score(0, 100, 4.3f, 50, 600, subject_name, grade_list));
    int i = 0;
    for(Score s:sem_grade) {
      if(s.x <= s.locked_mouseX && s.locked_mouseX <= s.x + s.w) {
        s.locked_mouseX = start_x + (end_x - start_x)/sem_grade.size() * i;
      }
      s.x = start_x + (end_x - start_x)/sem_grade.size() * i;
      i++;
    }
  }
  public void sem_grade_delete(int ind) {
    sem_grade.remove(ind);
    int i = 0;
    for(Score s:sem_grade) {
      if(s.x <= s.locked_mouseX && s.locked_mouseX <= s.x + s.w) {
        s.locked_mouseX = start_x + (end_x - start_x)/sem_grade.size() * i;
      }
      s.x = start_x + (end_x - start_x)/sem_grade.size() * i;
      i++;
    }
  }
  public Boolean sem_grade_exist(String name) {
    Boolean flag = false;
    for(Score s:sem_grade) {
      if(name.equals(s.name)) flag = true;
    }
    return flag;
  }
  public void show() {
    float tot = 0;
    for(int i=0;i<cnt;i++) {
      bars[i].highlight_chk();
      bars[i].show();
      total_score.scores[i] = bars[i].cur_score * bars[i].ratio;
      tot += bars[i].cur_score * bars[i].ratio;
      //print(str(bars[i].cur_score) + ", ");
    }
    total_score.cur_score = tot;
    total_score.show();
    //println("");
  }
  public void show_() {
    for(Score s:sem_grade) {
      s.highlight_chk();
      s.show_();
    }
  }
  public void lock_chk() {
    if(bars == null) {
      for(Score s:sem_grade) {
        s.lock_chk();
      }
    } else {
      for(Score b:bars) {
        b.lock_chk();
      }
    }
  }
  public void saveRow(Boolean flag) {
    TableRow tmp;
    if(!flag) {
      tmp = table.addRow();
      tmp.setString("subject", name);
    } else {
      tmp = table.findRow(name, "subject");
    }
    if(exam_1) tmp.setFloat("exam_1", bars[quiz_cnt - 1 + PApplet.parseInt(exam_1)].cur_score);
    if(exam_2) tmp.setFloat("exam_2", bars[quiz_cnt - 1 + PApplet.parseInt(exam_1) + PApplet.parseInt(exam_2)].cur_score);
    if(quiz_cnt > 0) {
      String[] q = new String[quiz_cnt];
      for(int i=0;i<quiz_cnt;i++) {
        q[i] = str(bars[i].cur_score);
      }
      tmp.setString("quiz_score", String.join(",", q));
    } else tmp.setString("quiz_score", "");
    if(tasks.length > 0) {
      String[] q = new String[tasks.length];
      for(int i=0;i<tasks.length;i++) {
        q[i] = str(bars[quiz_cnt + i + PApplet.parseInt(exam_1) + PApplet.parseInt(exam_2)].cur_score);
      }
      tmp.setString("task_score", String.join(",", q));
    } else tmp.setString("task_score", "");
    tmp.setFloat("grade", total_score.cur_score);
  }
  public void saveGrade() {
    Table store = new Table();
    store.addColumn("subject");
    store.addColumn("time");
    store.addColumn("grade");
    store.addColumn("PF");
    for(Score s:sem_grade) {
      TableRow tmp = store.addRow();
      tmp.setString("subject", s.name);
      tmp.setInt("time", timeMap.get(s.name));
      tmp.setString("grade", sg.getgrade((float)round(s.cur_score * 100)/100));
    }
    saveTable(store,  "data/"+ name.substring(0, 3) + ".csv");
  }
}
class Tab {
  int start_x, end_x, start_y, end_y;
  int col;
  Boolean mouseOver = false;
  String name;
  Subject sub;
  dot_graph dgraph;
  Tab(String name_, int start_x_, int end_x_, int start_y_, int end_y_, int col_) {
    name = name_;
    start_x = start_x_;
    end_x = end_x_;
    start_y = start_y_;
    end_y = end_y_;
    col = col_;
  }
  public void chkMouse() {
    if(start_x <= mouseX && mouseX <= end_x &&
    start_y <= mouseY && mouseY <= end_y) {
      mouseOver = true;
    } else mouseOver = false;
  }
  public void show() {
    if(mouseOver) {
      fill(200);
      strokeWeight(2);
    } else {
      fill(col);
      strokeWeight(1);
    }
    stroke(0);
    rect(start_x, start_y, end_x, end_y);
    textAlign(CENTER);
    textSize(20);
    fill(0);
    text(name, (start_x + end_x)/2, (start_y + end_y)/2 + 7);
    strokeWeight(1);
  }
  public Boolean click() {
    return (start_x <= mouseX && mouseX <= end_x &&
    start_y <= mouseY && mouseY <= end_y);
  }
}
class Total {
  int x, y, score, w, h, locked_mouseY = mouseY, locked_mouseX = mouseX;
  float cur_score = 0, u;
  Boolean quantization;
  String name;
  float[] scores;
  int[] colors;
  Total(int x_, int y_, int score_, int w_, int h_, Boolean q_, float u_, String name_, int[] colors_) {
    x = x_;
    y = y_;
    h = h_;
    w = w_;
    score = score_;
    quantization = q_;
    name = name_;
    u = u_;
    scores = new float[colors_.length];
    colors = new int[colors_.length];
    for(int i=0;i<colors_.length;i++) {
      colors[i] = colors_[i];
    }
  }
  public void show() {
    noStroke();
    float sum_score = 0;
    for(int i=0;i<scores.length;i++) {
      fill(colors[i]);
      rect(x, map(sum_score, 100, 0, y, y+h), x+w, map(sum_score + scores[i], 100, 0, y, y+h));
      sum_score += scores[i];
    }
    stroke(0);
    strokeWeight(2);
    fill(0, 0, 0, 0);
    rect(x, y, x+w, map(89.5f, 100, 0, y, y+h)); 
    strokeWeight(1);
    for(int i=0;i<12;i++) {
      fill(0, 0, 0, 0);
      strokeWeight(2);
      if(i < 11) rect(x, map(89.5f - (float)(i*5), 100, 0, y, y+h), x+w, map(89.5f - (float)((i+1)*5), 100, 0, y, y+h));
      else rect(x, map(89.5f - (float)(i*5), 100, 0, y, y+h), x+w, y+h);
      fill(0);
      strokeWeight(1);
      if(cur_score >= 89.5f - (float)(i*5)) fill(255, 0, 0);
      else fill(0);
      text(grade(i), x - 20, map(89.5f - (float)(i*5), 100, 0, y, y+h));
    }
    text(grade(12), x - 20, (map(89.5f - 55, 100, 0, y, y+h) + (float)(y+h))/2);
    textSize(15);
    fill(0);
    strokeWeight(2);
    text(name, x+w/2, y+h+40);
    textSize(20);
    text(grade(cur_score) + " / " + str((float)round(cur_score*100)/100), x+w/2, y - 50);
    strokeWeight(1);
  }
  public String grade(int i) {
    String ret = "";
    if(i < 12) {
      switch(i/3) {
        case 0:
          ret += 'A'; break;
        case 1:
          ret += 'B'; break;
        case 2:
          ret += 'C'; break;
        case 3:
          ret += 'D'; break;
        default: break;
      }
      switch(i%3) {
        case 0:
          ret += '+'; break;
        case 1:
          ret += '0'; break;
        case 2:
          ret += '-'; break;
        default: break;
      }
    } else ret = "F";
    return ret;
  }
  public String grade(float i) {
    int j = 0;
    if(i >= 89.5f) return grade(0);
    for(j=1;j<=12;j++) {
      if(89.5f - (float)((j-1)*5) > i && i >= 89.5f - (float)(j*5)) {
        break;
      }
    }
    return grade(j);
  }
}
class dot_graph {
  int start_x, end_x, start_y, end_y, w, h, cnt, dot_size, mode;
  float[] value, yline_list = {0.0f, 0.7f, 1.0f, 1.3f, 1.7f, 2.0f, 2.3f, 2.7f, 3.0f, 3.3f, 3.7f, 4.0f, 4.3f}, time;
  Boolean[] flag;
  String[] sem_list;
  float min, max;
  Table idTable;
  ArrayList<ArrayList<subject_grade>> sg = new ArrayList<ArrayList<subject_grade>>();
  HashMap<String, Integer> idMap = new HashMap<String, Integer>();
  dot_graph(int start_x_, int end_x_, int start_y_, int end_y_, int cnt_, int dot_size_, float min_, float max_, int mode_) {
    start_x = start_x_;
    end_x = end_x_;
    start_y = start_y_;
    end_y = end_y_;
    cnt = cnt_;
    min = min_;
    max = max_;
    dot_size = dot_size_;
    mode = mode_;
    idTable = loadTable("subject_id.csv", "header");
    
    for(TableRow row:idTable.rows()) {
      idMap.put(row.getString("subject"), Integer.parseInt(row.getString("id")));
    }
    init();
  }
  public void init() {
    Table[] grade_table = new Table[5];
    time = new float[5];
    flag = new Boolean[5];
    
    sem_list = new String[6];
    sem_list[0] = "1-1";
    sem_list[1] = "1-2";
    sem_list[2] = "2-1";
    sem_list[3] = "2-2";
    sem_list[4] = "3-1";
    sem_list[5] = "3-2";
    
    grade_table[0] = loadTable("1-1.csv", "header");
    grade_table[1] = loadTable("1-2.csv", "header");
    grade_table[2] = loadTable("2-1.csv", "header");
    grade_table[3] = loadTable("2-2.csv", "header");
    grade_table[4] = loadTable("3-1.csv", "header");
    
    sg = new ArrayList<ArrayList<subject_grade>>();
    
    for(int i=0;i<5;i++) {
      ArrayList<subject_grade> tmp = new ArrayList<subject_grade>();
      for(TableRow row:grade_table[i].rows()) {
        String name = row.getString("subject");
        String grade = row.getString("grade");
        String PF = row.getString("PF");
        //println(name);
        int id = idMap.get(name);
        int time = Integer.parseInt(row.getString("time"));
        tmp.add(new subject_grade(id, time, grade, name, PF));
        //println(id, time, grade, name, PF);
      }
      sg.add(tmp);
    }
  }
  public void time_init() {
    for(int i=0;i<5;i++) time[i] = 0;  
  }
  public void show() {
    strokeWeight(1);
    stroke(127, 100);
    fill(127);
    for(float yline:yline_list) {
      int ytmp = (int)map(yline, min, max, end_y, start_y);
      text(str(yline), start_x - 20, ytmp);
      line(start_x, ytmp, end_x, ytmp);
    }
    ellipseMode(CENTER);
    fill(0);
    float pre_x = -1, pre_y = 0;
    stroke(0, 0, 255);
    for(int i=0;i<5;i++) {
      float avg = 0, total_time = 0;
      ArrayList<subject_grade> select_list = new ArrayList<subject_grade>();
      for(subject_grade s : sg.get(i)) {
        if(mode == 0) {
          avg += s.getgrade()*s.time;
          total_time += s.time;
          select_list.add(s);
        } else {
          if(s.id == mode) {
            avg += s.getgrade()*s.time;
            total_time += s.time;
            select_list.add(s);
          }
        }
      }
      avg /= total_time;
      avg = (float)(round(avg*100))/100;
      float cur_x = map(i, 0, 4, start_x+20, end_x - 20);
      float cur_y = map(avg, min, max, end_y, start_y);
      float coe = 1;
      if(pre_x >= 0 && total_time > 0) {
        line(pre_x, pre_y, map(i, 0, 4, start_x+20, end_x - 20), map(avg, min, max, end_y, start_y));
      }
      if(dist(mouseX, mouseY, cur_x, cur_y) <= dot_size/2) {
        if(!flag[i]) {
          flag[i] = true;
          time[i] = 0;
        }
        if(time[i] < PI/4) time[i] += PI/100;
        coe = exp(-time[i]/4) * sin(time[i]*2) + 1;
        fill(0);
        text(str(avg), cur_x, cur_y - 20);
        fill(255);
        stroke(0);
        strokeWeight(3);
        int st_x = 0;
        if(cur_x + 200 > width) st_x = (int)cur_x - 400;
        else st_x = (int)cur_x - 200;
        rect(st_x, cur_y + 20, st_x + 400, cur_y + 40 + 30*select_list.size());
        strokeWeight(1);
        fill(0);
        textAlign(LEFT);
        text("해당 과목 : ", st_x + 20, cur_y + 50);
        int j = 0;
        for(subject_grade s:select_list) {
          textAlign(LEFT);
          text(s.name, st_x + 150, cur_y + 50 + 30*j);
          textAlign(RIGHT);
          text(s.grade, st_x + 380, cur_y + 50 + 30*j);
          j++;
        }
        textAlign(CENTER);
        stroke(0, 0, 255);
      } else {
        flag[i] = false;
        coe = exp(-time[i]/4) * sin(time[i]*2) + 1;
        time[i] += 0.1f;
      }
      if(total_time == 0) {
        pre_x = -1;
      } else {
        fill(0, 0, 255);
        ellipse(cur_x, cur_y, dot_size*coe, dot_size*coe);
        pre_x = cur_x;
        pre_y = cur_y;
      }
      fill(0);
      text(sem_list[i], cur_x, end_y + 40);
    }
  }
}
class onClickListner {
  public void func() {};
}
class subject_grade {
  int id, time;
  String grade, name;
  Boolean PF;
  HashMap<String, Float> grade2int = new HashMap<String, Float>();
  HashMap<Float, String> int2grade = new HashMap<Float, String>();
  subject_grade(int id_, int time_, String grade_, String name_, String PF_) {
    id = id_;
    time = time_;
    grade = grade_;
    name = name_;
    if(PF_ == "P") PF = true;
    else if(PF_ == "F") PF = false;
    
    grade2int.put("A+", 4.3f);
    grade2int.put("A0", 4.0f);
    grade2int.put("A-", 3.7f);
    grade2int.put("B+", 3.3f);
    grade2int.put("B0", 3.0f);
    grade2int.put("B-", 2.7f);
    grade2int.put("C+", 2.3f);
    grade2int.put("C0", 2.0f);
    grade2int.put("C-", 1.7f);
    grade2int.put("D+", 1.3f);
    grade2int.put("D0", 1.0f);
    grade2int.put("D-", 0.7f);
    grade2int.put("F", 0.0f);
    grade2int.put("", 0.0f);
    
    int2grade.put(4.3f, "A+");
    int2grade.put(4.0f, "A0");
    int2grade.put(3.7f, "A-");
    int2grade.put(3.3f, "B+");
    int2grade.put(3.0f, "B0");
    int2grade.put(2.7f, "B-");
    int2grade.put(2.3f, "C+");
    int2grade.put(2.0f, "C0");
    int2grade.put(1.7f, "C-");
    int2grade.put(1.3f, "D+");
    int2grade.put(1.0f, "D0");
    int2grade.put(0.7f, "D-");
    int2grade.put(0.0f, "F");
    int2grade.put(0.0f, "");
    
  }
  public float getgrade() {
    return grade2int.get(grade);
  }
  public String getgrade(float g) {
    return int2grade.get(g);
  }
}
  public void settings() {  size(1580, 770); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "classroom_project" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
