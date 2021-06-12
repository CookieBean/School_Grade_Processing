class Tab {
  int start_x, end_x, start_y, end_y;
  color col;
  Boolean mouseOver = false;
  String name;
  Subject sub;
  dot_graph dgraph;
  Tab(String name_, int start_x_, int end_x_, int start_y_, int end_y_, color col_) {
    name = name_;
    start_x = start_x_;
    end_x = end_x_;
    start_y = start_y_;
    end_y = end_y_;
    col = col_;
  }
  void chkMouse() {
    if(start_x <= mouseX && mouseX <= end_x &&
    start_y <= mouseY && mouseY <= end_y) {
      mouseOver = true;
    } else mouseOver = false;
  }
  void show() {
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
  Boolean click() {
    return (start_x <= mouseX && mouseX <= end_x &&
    start_y <= mouseY && mouseY <= end_y);
  }
}
