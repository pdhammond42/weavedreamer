$fn=40;

weft=[193,156,23]/255;
warp=[234,99,7]/255;

color (warp) {
	square([2,10]);
	translate ([4.5, 0]) square([2,10]);
	translate ([9, 0]) square([2,10]);
	translate ([3.25, 0]) difference() {
	   circle(r=3.25);
	   translate ([-3.25, 0.1]) square([6.5, 3.25]);
	   circle (r=1.25);
	}
	translate ([7.75, 0]) difference() {
	   circle(r=3.25);
	   translate ([-3.25, 0.1]) square([6.5, 3.25]);
	   circle (r=1.25);
	}
}

color (weft) {
    translate ([-1, 0]) square ([12, 2]);
    translate ([-1, 6]) square ([12, 2]);
	translate ([11, 4.0]) difference() {
	   circle(r=4);
	   translate ([-4,-4]) square([4, 8]);
	   circle (r=2);
	}
}



color(warp) {
  translate ([0,6]) square (2);
  translate ([4.5,0]) square (2);
  translate ([9,2]) square ([2,6]);
}
