/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Date: 20.12.2018
 *  Description: FastCollinearPoints
 **************************************************************************** */

package dima.java.collinearpointsfinder;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
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

        ArrayList<MinMaxPoints> pointsMinMax = new ArrayList<>();
        if (len > 2) {
            Point[] tempPoints = pointsArray.clone();
            boolean existing = false;
            for (Point point : pointsArray) {
                for (MinMaxPoints segmentPoints : getSegmentsForPoint(point, tempPoints)) {
                    existing = false;
                    for (MinMaxPoints slopePoints : pointsMinMax) {
                        if (segmentPoints.minP.equals(slopePoints.minP) &&
                                segmentPoints.maxP.equals(slopePoints.maxP)) {
                            existing = true;
                        }
                    }
                    if (!existing) {
                        pointsMinMax.add(segmentPoints);
                    }
                }
            }
        }
        segments = new LineSegment[pointsMinMax.size()];
        for (int i = 0; i < pointsMinMax.size(); i++) {
            segments[i] = new LineSegment(pointsMinMax.get(i).minP, pointsMinMax.get(i).maxP);
        }
    }

    private class MinMaxPoints {
        Point minP;
        Point maxP;

        MinMaxPoints(Point minPoint, Point maxP) {
            this.minP = minPoint;
            this.maxP = maxP;
        }
    }

    private ArrayList<MinMaxPoints> getSegmentsForPoint(Point p, Point[] points) {
        int low = 1;
        int high = 1;
        int len = points.length;
        ArrayList<MinMaxPoints> outMinMax = new ArrayList<>();
        Arrays.sort(points, p.slopeOrder());
        ArrayList<Point> slopePoints = new ArrayList<>();
        slopePoints.add(p);
        slopePoints.add(points[1]);
        for (int i = 1; i < len - 1; i++) {
            if (p.slopeTo(points[i]) != p.slopeTo(points[i + 1])) {
                if (low < high - 1) {
                    slopePoints.sort(Point::compareTo);
                    outMinMax.add(new MinMaxPoints(slopePoints.get(0),
                                                   slopePoints.get(slopePoints.size() - 1)));
                }
                low = i + 1;
                slopePoints = new ArrayList<>();
                slopePoints.add(p);
                slopePoints.add(points[i + 1]);
            }
            else {
                high = i + 1;
                slopePoints.add(points[i + 1]);
                if ((i + 2) == len && low < high - 1) {
                    slopePoints.sort(Point::compareTo);
                    outMinMax.add(new MinMaxPoints(slopePoints.get(0),
                                                   slopePoints.get(slopePoints.size() - 1)));
                }
            }
        }
        return outMinMax;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            if (segment != null) {
                StdOut.println(segment);
                segment.draw();
            }
        }
        StdDraw.show();
    }
}
