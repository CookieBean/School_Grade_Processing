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
    
    grade2int.put("A+", 4.3);
    grade2int.put("A0", 4.0);
    grade2int.put("A-", 3.7);
    grade2int.put("B+", 3.3);
    grade2int.put("B0", 3.0);
    grade2int.put("B-", 2.7);
    grade2int.put("C+", 2.3);
    grade2int.put("C0", 2.0);
    grade2int.put("C-", 1.7);
    grade2int.put("D+", 1.3);
    grade2int.put("D0", 1.0);
    grade2int.put("D-", 0.7);
    grade2int.put("F", 0.0);
    grade2int.put("", 0.0);
    
    int2grade.put(4.3, "A+");
    int2grade.put(4.0, "A0");
    int2grade.put(3.7, "A-");
    int2grade.put(3.3, "B+");
    int2grade.put(3.0, "B0");
    int2grade.put(2.7, "B-");
    int2grade.put(2.3, "C+");
    int2grade.put(2.0, "C0");
    int2grade.put(1.7, "C-");
    int2grade.put(1.3, "D+");
    int2grade.put(1.0, "D0");
    int2grade.put(0.7, "D-");
    int2grade.put(0.0, "F");
    int2grade.put(0.0, "");
    
  }
  float getgrade() {
    return grade2int.get(grade);
  }
  String getgrade(float g) {
    return int2grade.get(g);
  }
}
