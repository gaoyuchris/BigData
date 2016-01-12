import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.fhpotsdam.unfolding.*; 
import de.fhpotsdam.unfolding.geo.*; 
import de.fhpotsdam.unfolding.utils.*; 
import de.fhpotsdam.unfolding.providers.Google.*; 
import de.fhpotsdam.unfolding.providers.OpenStreetMap.*; 
import de.fhpotsdam.unfolding.data.*; 
import de.fhpotsdam.unfolding.marker.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class map extends PApplet {




// Map provider


//import de.fhpotsdam.unfolding.providers.Microsoft.*;






UnfoldingMap map;
GeoUtils geo;

Table table;
Table grid;
ArrayList<Cluster> clstList;
int clstSelected;

String clst_bound_file = "new_bound.csv";
String grid_file = "grid2_9.csv"; 

ArrayList<Heatmap> htmapList;
boolean flag_htmap;

ImageMarker marker;
boolean flag_marker;

int sHeight = 600;
int sWidth = 800;

//map coord shift
//openStreetMap
float mlat_shift = 0;  //map = file + shift
float mlon_shift = 0.002f;

public void setup() {
  size(sWidth, sHeight, P2D);
  //map = new UnfoldingMap(this, new GoogleMapProvider());  //should enable global proxy
  map = new UnfoldingMap(this, new OpenStreetMapProvider());
  //map = new UnfoldingMap(this, new RoadProvider()); //MS
  map.zoomAndPanTo(11, new Location(39.92f, 116.38f));
  MapUtils.createDefaultEventDispatcher(this, map);
  geo = new GeoUtils();
  
  //map marker
  marker = new ImageMarker(new Location(0,0), loadImage("ui/marker.png"));
  map.addMarkers(marker);
  flag_marker = false;
  
  

  //table Init
  table = loadTable(clst_bound_file, "header");

  //clstList Init
  clstList = new ArrayList<Cluster>();
  for ( int i=0; i<table.getRowCount (); ) {
    int clst_id = PApplet.parseInt(table.getRow(i).getString("cluster"));
    ArrayList<Location> boundList = new ArrayList<Location>();
    int c_id = clst_id;
    float c_lat = 0;
    float c_lon = 0;
    int count = 0;
    while ( c_id == clst_id ) {
      float lat = PApplet.parseFloat(table.getRow(i).getString("lat"));
      float lon = PApplet.parseFloat(table.getRow(i).getString("lng"));
      Location p = new Location(lat, lon);
      c_lat += lat;
      c_lon += lon;
      count += 1;
      boundList.add(p);
      i += 1;
      if ( i >= table.getRowCount() ) {
        c_id = -1;
      } else {
        c_id = PApplet.parseInt(table.getRow(i).getString("cluster"));
      }
    }
    c_lat /= count;
    c_lon /= count;
    Location center = new Location(c_lat, c_lon);
    double r = geo.getDistance(center, boundList.get(0));
    for (int j=1; j<boundList.size (); j++) {
      double dist = geo.getDistance(center, boundList.get(j));
      if ( dist < r ) {
        r = dist;
      }
    }
    Cluster clst = new Cluster(center, clst_id);
    clst.setRadius(r);
    clst.bound = boundList;
    clstList.add(clst);
  }
  //clstList Init

    //grid Init
  htmapList = new ArrayList<Heatmap>();
  grid = loadTable(grid_file, "header");
  for (int i=0; i<grid.getRowCount (); ) {
    Heatmap htmap = new Heatmap();
    htmap.cluster_id = PApplet.parseInt(grid.getRow(i).getString("cluster"));
    htmap.gxSize = 2 + round(sqrt(PApplet.parseFloat(grid.getRow(i).getString("grid_cnt"))));
    htmap.gySize = htmap.gxSize;
    htmap.gweight = new float[htmap.gxSize][htmap.gySize];
    htmap.glat_span = PApplet.parseFloat(grid.getRow(i).getString("lat_span"));
    htmap.glon_span = PApplet.parseFloat(grid.getRow(i).getString("lng_span"));
    htmap.gmax = 0;
    htmap.gmaxp = new Location(0, 0);
    int c_id = htmap.cluster_id;
    while ( c_id == htmap.cluster_id ) {
      int x = PApplet.parseInt(grid.getRow(i).getString("grid_x"));
      int y = PApplet.parseInt(grid.getRow(i).getString("grid_y"));
      float w = PApplet.parseFloat(grid.getRow(i).getString("weight"));
      htmap.gweight[x+1][y+1] = w;
      if ( w > htmap.gmax ) {
        htmap.gmax = w;
        htmap.gmaxp.setLat( PApplet.parseFloat(grid.getRow(i).getString("lat")) + htmap.glat_span/2 );
        htmap.gmaxp.setLon( PApplet.parseFloat(grid.getRow(i).getString("lng")) + htmap.glon_span/2 );
      }
      if ( x==0 && y==0 ) {
        htmap.glat_start = PApplet.parseFloat(grid.getRow(i).getString("lat")) - htmap.glat_span/2;
        htmap.glon_start = PApplet.parseFloat(grid.getRow(i).getString("lng")) - htmap.glon_span/2;
      }
      if ( x==htmap.gxSize-3 && y==htmap.gySize-3 ) {
        htmap.glat_end = PApplet.parseFloat(grid.getRow(i).getString("lat")) + 3 * htmap.glat_span/2;
        htmap.glon_end = PApplet.parseFloat(grid.getRow(i).getString("lng")) + 3 * htmap.glon_span/2;
      }
      i += 1;
      if ( i >= grid.getRowCount() ) {
        c_id = -1;
      } else {
        c_id = PApplet.parseInt(grid.getRow(i).getString("cluster"));
      }
    }
    for (int j=0; j<htmap.gxSize; j++) {
      for (int k=0; k<htmap.gySize; k++) {
        if ( j==0 || j==htmap.gxSize-1 || k==0 || k==htmap.gySize-1 ) {
          htmap.gweight[j][k] = 0;
        }
      }
    }
    htmapList.add(htmap);
  }
  flag_htmap = false;
  //grid Init

  clstSelected = -1; // no cluster selected


  //map coord shift
  //cluster
  for (int i=0; i<clstList.size (); i++) {
    Cluster c = clstList.get(i);
    c.center.setLat( c.center.getLat() + mlat_shift );
    c.center.setLon( c.center.getLon() + mlon_shift );
    ArrayList<Location> bd = c.bound;
    for (int j=0; j<bd.size (); j++) {
      bd.get(j).setLat(bd.get(j).getLat() + mlat_shift );
      bd.get(j).setLon(bd.get(j).getLon() + mlon_shift );
    }
  }
  //heatmap
  for (int i=0; i<htmapList.size (); i++) {
    Heatmap h = htmapList.get(i);
    h.glat_start += mlat_shift;
    h.glat_end += mlat_shift;
    h.glon_start += mlon_shift;
    h.glon_end += mlon_shift;
    h.gmaxp.setLat(h.gmaxp.getLat() + mlat_shift);
    h.gmaxp.setLon(h.gmaxp.getLon() + mlon_shift);
  }
  //map coord shift


    //display
  colorMode(HSB, 256);
  for ( int i=0; i<clstList.size (); i++ ) {
    int clst_id = clstList.get(i).id;
    float h = map(clst_id, 0, 27, 0, 255), 
    s = 255, 
    b = 255, 
    alpha = 100;
    clstList.get(i).setColor(h, s, b, alpha);
  }
}


public void mouseClicked() {
  for (int i=1; i<clstList.size (); i++) {
    if ( clstList.get(i).isIn(mouseX, mouseY) ) {
      if ( clstSelected == clstList.get(i).id ) {
        clstSelected = -1;
      } else {
        clstSelected = clstList.get(i).id;
      }
    }
  }
}


public void drawCluster_polygon() {
  for (int i=1; i<clstList.size (); i++) {
    clstList.get(i).draw_lines();
  }
}



public void drawCluster(float smooth) {
  for (int i=1; i<clstList.size (); i++) {
    if ( clstList.get(i).isIn(mouseX, mouseY) || clstList.get(i).id == clstSelected) {
      clstList.get(i).draw_highlight(smooth);
    } else {
      clstList.get(i).draw_rounded(smooth);
    }
  }
}

public void drawHeatmap() {
  if ( clstSelected > 0 && flag_htmap ) {
    for (int i=0; i<htmapList.size (); i++) {
      if ( htmapList.get(i).cluster_id == clstSelected ) {
        htmapList.get(i).show();
        //htmapList.get(i).show_data();
        if ( flag_marker ){
          marker.setLocation(htmapList.get(i).gmaxp);
          
        }else {
          marker.setLocation(new Location(0, 0));
        }
      }
    }
  }
}


public void keyTyped() {
  if (key == 'h') {
    flag_htmap = !flag_htmap;
    flag_marker = false;
    marker.setLocation(new Location(0, 0));
  }
  if (key == 'm') {
    flag_marker = !flag_marker;
  }
}







public void draw() {
  map.draw();
  //stroke(0);
  //drawCluster_polygon();
  drawCluster(0.1f);

  //heatmap
  drawHeatmap();




  //lat lon
  //  fill(0);
  //  Location mouse = map.getLocation(mouseX, mouseY);
  //  text(mouse.getLat() + " " + mouse.getLon(), mouseX, mouseY);
  //  println(mouse.getLat() + " " + mouse.getLon());
  //  





  //info
  fill(0);
  Location mouse = map.getLocation(mouseX, mouseY);
  double mdist = geo.getDistance(mouse, clstList.get(0).center);
  int nearest = 0;
  for (int i=1; i<clstList.size (); i++) {
    double dist = geo.getDistance(mouse, clstList.get(i).center);
    if ( dist < clstList.get(i).radius && dist < mdist ) {
      mdist = dist;
      nearest = i;
    }
  }
  if ( nearest != 0 ) {
    text("cluster: " + clstList.get(nearest).id, mouseX, mouseY);
  }
}


public ScreenPosition interPos(ScreenPosition p1, ScreenPosition p2, float t) {
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
  public void setRadius(double r) {
    radius = r;
  }
  public void setColor(float h, float s, float b, float alpha) {
    float[] c = {
      h, s, b, alpha
    };
    hsba = c;
  }


  public boolean isIn(float x, float y) {
    Location p = map.getLocation(x, y);
    return ( geo.getDistance(p, center) <= radius );
  }


  public void draw_lines() {
    noFill();
    beginShape();
    for ( int j=0; j<bound.size (); j++) {
      ScreenPosition v = map.getScreenPosition(bound.get(j));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
  }

  public void draw0(float smooth) {
    int bd_size = bound.size();
    beginShape();
    ScreenPosition v1, v2, v3, m12, m23, v2l, v2r;
    for (int j=0; j<bd_size; j++) {
      v1 = map.getScreenPosition(bound.get(j));
      v2 = map.getScreenPosition(bound.get((j+1)%bd_size));
      v3 = map.getScreenPosition(bound.get((j+2)%bd_size));
      m12 = interPos(v1, v2, 0.5f);
      m23 = interPos(v2, v3, 0.5f);
      v2l = interPos(v2, m12, smooth);
      v2r = interPos(v2, m23, smooth);
      if ( j==0 ) {
        vertex(m12.x, m12.y);
      }
      bezierVertex(v2l.x, v2l.y, v2r.x, v2r.y, m23.x, m23.y);
    }
    endShape();
  }

  public void draw_rounded(float smooth) {
    fill(hsba[0], hsba[1], hsba[2], hsba[3]);
    noStroke();
    draw0(smooth);
  }

  public void draw_highlight(float smooth) {
    fill(hsba[0], hsba[1]/2, hsba[2], hsba[3]);
    stroke(0, 255, 255);
    draw0(smooth);
  }
}


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

  public void show_data() {
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


  public void show() {
    Location p0 = new Location(glat_start, glon_start);
    ScreenPosition v0 = map.getScreenPosition(p0);
    Location pn = new Location(glat_end, glon_end);
    ScreenPosition vn = map.getScreenPosition(pn);
    int x0 = PApplet.parseInt(v0.x + 2), 
    y0 = PApplet.parseInt(v0.y - 2), 
    xn = PApplet.parseInt(vn.x - 2), 
    yn = PApplet.parseInt(vn.y + 2);
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

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "map" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
