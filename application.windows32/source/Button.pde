class Button {
  int sx, sy, ex, ey, text_sz;
  String name;
  color col;
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
  Button(int sx_, int sy_, int ex_, int ey_, int text_sz_, String name_, color col_) {
    sx = sx_;
    sy = sy_;
    ex = ex_;
    ey = ey_;
    text_sz = text_sz_;
    name = name_;
    col = col_;
  }
  void show() {
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
  void onClick(onClickListner listner) {
    if(disabled) return;
    if(sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey) {
      listner.func();
    }
  }
  Boolean Clicked() {
    return sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey;
  }
  void highlighted() {
    if(sx <= mouseX && mouseX <= ex && sy <= mouseY && mouseY <= ey) {
      highlighted = true;
    } else highlighted = false;
  }
}
