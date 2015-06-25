
PShape shape;

void setup(){
  size(800,600,P3D);
  shape = loadShape("051-tiny-02.svg");
  shape.disableStyle();
}

void draw(){
  background(0);
  stroke(255);
  fill(255);
  println(shape.getChildCount());
  shape(shape, 50, 50, shape.getWidth(), shape.getHeight());
}

