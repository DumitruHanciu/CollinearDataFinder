/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Date: 20.12.2018
 *  Description: BruteCollinearPoints
 **************************************************************************** */

package dima.java.collinearpointsfinder;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        int len = points.length;
        for (int i = 0; i < len; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] pointsArray = points.clone();
        Arrays.sort(pointsArray, Point::compareTo);

        for (int i = 0; i < len - 1; i++) {
            if (pointsArray[i].compareTo(pointsArray[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> segmentsArray = new ArrayList<>();
        double slope = -0.0;
        for (int i = 0; i < len - 3; i++) {
            for (int j = i + 1; j < len - 2; j++) {
                for (int k = j + 1; k < len - 1; k++) {
                    slope = pointsArray[i].slopeTo(pointsArray[j]);
                    if (pointsArray[i].slopeTo(pointsArray[k]) == slope) {
                        for (int l = k + 1; l < len; l++) {
                            if (pointsArray[i].slopeTo(pointsArray[l]) == slope) {
                                // found 4 coliniar points i j k l
                                segmentsArray.add(new LineSegment(pointsArray[i], pointsArray[l]));
                            }
                        }
                    }
                }
            }
        }
        segments = segmentsArray.toArray(new LineSegment[segmentsArray.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0] + ".txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
            points[i] = new Point(in.readInt(), in.readInt());


        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) p.draw();
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
