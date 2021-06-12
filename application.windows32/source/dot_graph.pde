class dot_graph {
  int start_x, end_x, start_y, end_y, w, h, cnt, dot_size, mode;
  float[] value, yline_list = {0.0, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3}, time;
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
  void init() {
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
  void time_init() {
    for(int i=0;i<5;i++) time[i] = 0;  
  }
  void show() {
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
        time[i] += 0.1;
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
