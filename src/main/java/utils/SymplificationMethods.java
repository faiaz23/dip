package utils;

import DPAlgorithm.SeriesReducer;

import java.util.List;

public class SymplificationMethods {
    public static List<Point> DouglasPeucker(List<Point> input, double epsilon){
        input.remove(input.size()-1);
        List<Point> reduced = SeriesReducer.reduce(input, epsilon);
        return reduced;
    }
}
