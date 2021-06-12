class Subject {
  String name;
  int quiz_cnt, quiz_score, start_x = 325, end_x = 1425, exam_per, cnt;
  Boolean exam_1, exam_2;
  Table table, grade_table;
  int[] tasks;
  Score[] bars;
  ArrayList<Score> sem_grade;
  color[] colors;
  float quiz_unit, task_unit;
  float[] grade_list = {0.0, 0.3, 0.6, 1.0, 1.3, 1.6, 2.0, 2.3, 2.6, 3.0, 3.3, 3.6, 4.3};
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
    cnt = quiz_cnt + tasks_.length + int(exam_1) + int(exam_2);
    colors = new color[cnt];
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
      bars[i] = new Score(start_x + (end_x - start_x)/cnt * i, 100, tasks[i - quiz_cnt - int(exam_1) - int(exam_2)], 50, 600, true, task_unit, "수행평가 " + str(i + 1 - quiz_cnt - int(exam_1) - int(exam_2)), colors[i], 1); i++;
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
      sem_grade.add(new Score(start_x + (end_x - start_x)/grade_table.getRowCount() * i, 100, 4.3, 50, 600, tr.getString("subject"), grade_list));
      Score tmp = sem_grade.get(i);
      tmp.locked_mouseX = tmp.x;
      sg.grade = tr.getString("grade");
      //println(sg.grade, tr.getString("subject"));
      tmp.locked_mouseY = (int)map(sg.getgrade() - 0.01, 0, 4.3, tmp.y+tmp.h, tmp.y);
      //println(i);
    }
    timeTable = loadTable("subject_id.csv", "header");
    
    for(TableRow row:timeTable.rows()) {
      timeMap.put(row.getString("subject"), Integer.parseInt(row.getString("time")));
    }
  }
  void sem_grade_add(String subject_name) {
    sem_grade.add(new Score(0, 100, 4.3, 50, 600, subject_name, grade_list));
    int i = 0;
    for(Score s:sem_grade) {
      if(s.x <= s.locked_mouseX && s.locked_mouseX <= s.x + s.w) {
        s.locked_mouseX = start_x + (end_x - start_x)/sem_grade.size() * i;
      }
      s.x = start_x + (end_x - start_x)/sem_grade.size() * i;
      i++;
    }
  }
  void sem_grade_delete(int ind) {
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
  Boolean sem_grade_exist(String name) {
    Boolean flag = false;
    for(Score s:sem_grade) {
      if(name.equals(s.name)) flag = true;
    }
    return flag;
  }
  void show() {
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
  void show_() {
    for(Score s:sem_grade) {
      s.highlight_chk();
      s.show_();
    }
  }
  void lock_chk() {
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
  void saveRow(Boolean flag) {
    TableRow tmp;
    if(!flag) {
      tmp = table.addRow();
      tmp.setString("subject", name);
    } else {
      tmp = table.findRow(name, "subject");
    }
    if(exam_1) tmp.setFloat("exam_1", bars[quiz_cnt - 1 + int(exam_1)].cur_score);
    if(exam_2) tmp.setFloat("exam_2", bars[quiz_cnt - 1 + int(exam_1) + int(exam_2)].cur_score);
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
        q[i] = str(bars[quiz_cnt + i + int(exam_1) + int(exam_2)].cur_score);
      }
      tmp.setString("task_score", String.join(",", q));
    } else tmp.setString("task_score", "");
    tmp.setFloat("grade", total_score.cur_score);
  }
  void saveGrade() {
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
