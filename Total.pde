class Total {
  int x, y, score, w, h, locked_mouseY = mouseY, locked_mouseX = mouseX;
  float cur_score = 0, u;
  Boolean quantization;
  String name;
  float[] scores;
  color[] colors;
  Total(int x_, int y_, int score_, int w_, int h_, Boolean q_, float u_, String name_, color[] colors_) {
    x = x_;
    y = y_;
    h = h_;
    w = w_;
    score = score_;
    quantization = q_;
    name = name_;
    u = u_;
    scores = new float[colors_.length];
    colors = new color[colors_.length];
    for(int i=0;i<colors_.length;i++) {
      colors[i] = colors_[i];
    }
  }
  void show() {
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
    rect(x, y, x+w, map(89.5, 100, 0, y, y+h)); 
    strokeWeight(1);
    for(int i=0;i<12;i++) {
      fill(0, 0, 0, 0);
      strokeWeight(2);
      if(i < 11) rect(x, map(89.5 - (float)(i*5), 100, 0, y, y+h), x+w, map(89.5 - (float)((i+1)*5), 100, 0, y, y+h));
      else rect(x, map(89.5 - (float)(i*5), 100, 0, y, y+h), x+w, y+h);
      fill(0);
      strokeWeight(1);
      if(cur_score >= 89.5 - (float)(i*5)) fill(255, 0, 0);
      else fill(0);
      text(grade(i), x - 20, map(89.5 - (float)(i*5), 100, 0, y, y+h));
    }
    text(grade(12), x - 20, (map(89.5 - 55, 100, 0, y, y+h) + (float)(y+h))/2);
    textSize(15);
    fill(0);
    strokeWeight(2);
    text(name, x+w/2, y+h+40);
    textSize(20);
    text(grade(cur_score) + " / " + str((float)round(cur_score*100)/100), x+w/2, y - 50);
    strokeWeight(1);
  }
  String grade(int i) {
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
  String grade(float i) {
    int j = 0;
    if(i >= 89.5) return grade(0);
    for(j=1;j<=12;j++) {
      if(89.5 - (float)((j-1)*5) > i && i >= 89.5 - (float)(j*5)) {
        break;
      }
    }
    return grade(j);
  }
}
