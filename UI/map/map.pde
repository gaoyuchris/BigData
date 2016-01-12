import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
// Map provider
import de.fhpotsdam.unfolding.providers.Google.*;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.*;
//import de.fhpotsdam.unfolding.providers.Microsoft.*;

import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.marker.*;



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
float mlon_shift = 0.002;

void setup() {
  size(sWidth, sHeight, P2D);
  //map = new UnfoldingMap(this, new GoogleMapProvider());  //should enable global proxy
  map = new UnfoldingMap(this, new OpenStreetMapProvider());
  //map = new UnfoldingMap(this, new RoadProvider()); //MS
  map.zoomAndPanTo(11, new Location(39.92, 116.38));
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
    int clst_id = int(table.getRow(i).getString("cluster"));
    ArrayList<Location> boundList = new ArrayList<Location>();
    int c_id = clst_id;
    float c_lat = 0;
    float c_lon = 0;
    int count = 0;
    while ( c_id == clst_id ) {
      float lat = float(table.getRow(i).getString("lat"));
      float lon = float(table.getRow(i).getString("lng"));
      Location p = new Location(lat, lon);
      c_lat += lat;
      c_lon += lon;
      count += 1;
      boundList.add(p);
      i += 1;
      if ( i >= table.getRowCount() ) {
        c_id = -1;
      } else {
        c_id = int(table.getRow(i).getString("cluster"));
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
    htmap.cluster_id = int(grid.getRow(i).getString("cluster"));
    htmap.gxSize = 2 + round(sqrt(float(grid.getRow(i).getString("grid_cnt"))));
    htmap.gySize = htmap.gxSize;
    htmap.gweight = new float[htmap.gxSize][htmap.gySize];
    htmap.glat_span = float(grid.getRow(i).getString("lat_span"));
    htmap.glon_span = float(grid.getRow(i).getString("lng_span"));
    htmap.gmax = 0;
    htmap.gmaxp = new Location(0, 0);
    int c_id = htmap.cluster_id;
    while ( c_id == htmap.cluster_id ) {
      int x = int(grid.getRow(i).getString("grid_x"));
      int y = int(grid.getRow(i).getString("grid_y"));
      float w = float(grid.getRow(i).getString("weight"));
      htmap.gweight[x+1][y+1] = w;
      if ( w > htmap.gmax ) {
        htmap.gmax = w;
        htmap.gmaxp.setLat( float(grid.getRow(i).getString("lat")) + htmap.glat_span/2 );
        htmap.gmaxp.setLon( float(grid.getRow(i).getString("lng")) + htmap.glon_span/2 );
      }
      if ( x==0 && y==0 ) {
        htmap.glat_start = float(grid.getRow(i).getString("lat")) - htmap.glat_span/2;
        htmap.glon_start = float(grid.getRow(i).getString("lng")) - htmap.glon_span/2;
      }
      if ( x==htmap.gxSize-3 && y==htmap.gySize-3 ) {
        htmap.glat_end = float(grid.getRow(i).getString("lat")) + 3 * htmap.glat_span/2;
        htmap.glon_end = float(grid.getRow(i).getString("lng")) + 3 * htmap.glon_span/2;
      }
      i += 1;
      if ( i >= grid.getRowCount() ) {
        c_id = -1;
      } else {
        c_id = int(grid.getRow(i).getString("cluster"));
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


void mouseClicked() {
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


void drawCluster_polygon() {
  for (int i=1; i<clstList.size (); i++) {
    clstList.get(i).draw_lines();
  }
}



void drawCluster(float smooth) {
  for (int i=1; i<clstList.size (); i++) {
    if ( clstList.get(i).isIn(mouseX, mouseY) || clstList.get(i).id == clstSelected) {
      clstList.get(i).draw_highlight(smooth);
    } else {
      clstList.get(i).draw_rounded(smooth);
    }
  }
}

void drawHeatmap() {
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


void keyTyped() {
  if (key == 'h') {
    flag_htmap = !flag_htmap;
    flag_marker = false;
    marker.setLocation(new Location(0, 0));
  }
  if (key == 'm') {
    flag_marker = !flag_marker;
  }
}







void draw() {
  map.draw();
  //stroke(0);
  //drawCluster_polygon();
  drawCluster(0.1);

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

