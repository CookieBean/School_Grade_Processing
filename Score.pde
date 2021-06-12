class Score {
  int x, y, w, h, locked_mouseY = mouseY, locked_mouseX = mouseX;
  color col;
  float cur_score = 0, u, ratio, score;
  Boolean quantization, locked = false, highlighted = false, grade;
  String name;
  float[] cut_list;
  Score(int x_, int y_, int score_, int w_, int h_, Boolean q_, float u_, String name_, color col_, float ratio_) {
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
  void lock_chk() {
    if(x <= mouseX && mouseX <= x+w) {
      locked_mouseY = mouseY;
      locked_mouseX = mouseX;
      if(grade) show_();
      else show();
      //print(cur_score);
    }
  }
  void highlight_chk() {
    if(x <= mouseX && mouseX <= x+w) {
      strokeWeight(2);
      highlighted = true;
    } else {
      highlighted = false;
      strokeWeight(1);
    }
  }
  Boolean mouseOver() {
    return x <= mouseX && mouseX <= x+w && y <= mouseY && mouseY <= y+h;
  }
  void show_() {
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
  void show() {
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
        cur_score = (float)floor(cur_score / 0.5) * 0.5;
        cur_score = (float)round(cur_score * 100)/100;
        text(str(cur_score), x - 25, constrain(locked_mouseY, y, y+h));
        fill(col);
        rect(x, constrain(locked_mouseY, y, y+h), x+w, y+h);
      } else cur_score = 0;
      if(x <= mouseX && mouseX <= x+w) {
        
        cur_score = (float)round(map(constrain(mouseY, y, y+h), y+h, y, 0, score) * 100) / 100;
        cur_score = (float)floor(cur_score / 0.5) * 0.5;
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
