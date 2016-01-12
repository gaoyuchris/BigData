
ScreenPosition interPos(ScreenPosition p1, ScreenPosition p2, float t) {
  float mt = 1-t;
  float px = mt*p1.x + t*p2.x;
  float py = mt*p1.y + t*p2.y;
  return new ScreenPosition(px, py);
}


class Cluster {
  int id;
  ArrayList<Location> bound;
  Location center;
  double radius;
  //Appearance
  float[] hsba; //0-255



  Cluster(Location c, int _id) {
    center = c;
    id = _id;
    radius = 0;  //init radius
  }
  void setRadius(double r) {
    radius = r;
  }
  void setColor(float h, float s, float b, float alpha) {
    float[] c = {
      h, s, b, alpha
    };
    hsba = c;
  }


  boolean isIn(float x, float y) {
    Location p = map.getLocation(x, y);
    return ( geo.getDistance(p, center) <= radius );
  }


  void draw_lines() {
    noFill();
    beginShape();
    for ( int j=0; j<bound.size (); j++) {
      ScreenPosition v = map.getScreenPosition(bound.get(j));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
  }

  void draw0(float smooth) {
    int bd_size = bound.size();
    beginShape();
    ScreenPosition v1, v2, v3, m12, m23, v2l, v2r;
    for (int j=0; j<bd_size; j++) {
      v1 = map.getScreenPosition(bound.get(j));
      v2 = map.getScreenPosition(bound.get((j+1)%bd_size));
      v3 = map.getScreenPosition(bound.get((j+2)%bd_size));
      m12 = interPos(v1, v2, 0.5);
      m23 = interPos(v2, v3, 0.5);
      v2l = interPos(v2, m12, smooth);
      v2r = interPos(v2, m23, smooth);
      if ( j==0 ) {
        vertex(m12.x, m12.y);
      }
      bezierVertex(v2l.x, v2l.y, v2r.x, v2r.y, m23.x, m23.y);
    }
    endShape();
  }

  void draw_rounded(float smooth) {
    fill(hsba[0], hsba[1], hsba[2], hsba[3]);
    noStroke();
    draw0(smooth);
  }

  void draw_highlight(float smooth) {
    fill(hsba[0], hsba[1]/2, hsba[2], hsba[3]);
    stroke(0, 255, 255);
    draw0(smooth);
  }
}

