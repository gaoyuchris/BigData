
class Heatmap {
  int cluster_id;
  int gxSize;
  int gySize;
  float glat_span;
  float glon_span;
  float glat_start;
  float glon_start;
  float glat_end;
  float glon_end;
  float[][] gweight;
  float gmax;
  Location gmaxp;

  void show_data() {
    Location start = new Location(glat_start, glon_start);
    Location end = new Location(glat_end, glon_end);
    ScreenPosition v0 = map.getScreenPosition(start);
    ScreenPosition vn = map.getScreenPosition(end);
    fill(0);
    ellipse(v0.x, v0.y, 10, 10);
    ellipse(vn.x, vn.y, 10, 10);

    Location mouse = map.getLocation(mouseX, mouseY);
    for (int i=0; i<gxSize; i++) {
      for (int j=0; j<gySize; j++) {
        float lat = glat_start + i*glat_span;
        float lon = glon_start + j*glon_span;
        float dlat = abs(lat - mouse.getLat());
        float dlon = abs(lon - mouse.getLon());
        if ( dlat < glat_span/2 && dlon < glon_span/2 ) {
          Location p = new Location(lat, lon);
          ScreenPosition s = map.getScreenPosition(p);
          fill(0, 255, 255);
          ellipse(s.x, s.y, 10, 10);
          text("weight: " + gweight[i][j], s.x, s.y);
        }
      }
    }
  }


  void show() {
    Location p0 = new Location(glat_start, glon_start);
    ScreenPosition v0 = map.getScreenPosition(p0);
    Location pn = new Location(glat_end, glon_end);
    ScreenPosition vn = map.getScreenPosition(pn);
    int x0 = int(v0.x + 2), 
    y0 = int(v0.y - 2), 
    xn = int(vn.x - 2), 
    yn = int(vn.y + 2);
    for (int x=x0; x<xn; x++) {
      for (int y=yn; y<y0; y++) {
        Location p = map.getLocation(x, y);
        float gx = (p.getLat() - glat_start) / glat_span;
        float gy = (p.getLon() - glon_start) / glon_span;
        int gi = floor(gx);
        int gj = floor(gy);
        if (gi < 0) {
          println("y " + y);
          println("plat " + p.getLat());
          println("gx " + gx);
          println("gi " + gi);
        }
        if (gj < 0) {
          println("x " + x);
          println("plon " + p.getLon());
          println("gy " + gy);
          println("gj " + gj);
        }
        int gin = (gi+1) % gxSize;
        int gjn = (gj+1) % gySize;
        float ti = gx - gi;
        float tj = gy - gj;
        float mti = 1-ti;
        float mtj = 1-tj;
        //bi-linear interpolation
        float w1 = mti*gweight[gi][gj] + ti*gweight[gin][gj];
        float w2 = mti*gweight[gi][gjn] + ti*gweight[gin][gjn];
        float w = mtj*w1 + tj*w2;
        //display
        float h = map(w, 0, gmax, 100, 0);
        float a = 100;
        if ( h >= 95 ) {
          a = 0;
        }
        stroke(h, 255, 255, a);
        point(x, y);
      }
    }
  }
}

