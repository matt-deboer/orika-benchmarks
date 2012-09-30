/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package ma.glasnost.orika.benchmark.nested_benchmark;

import java.util.HashSet;
import java.util.Set;

import ma.glasnost.orika.benchmark.nested_benchmark.domain.Graph;
import ma.glasnost.orika.benchmark.nested_benchmark.domain.Point;
import ma.glasnost.orika.benchmark.nested_benchmark.domain.Segment;
import ma.glasnost.orika.benchmark.nested_benchmark.orika.BoundOrikaMapper;
import ma.glasnost.orika.benchmark.nested_benchmark.orika.OrikaNonCyclicMapper;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

/**
 * Caliper powered benchmark.
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 8:35:29 AM
 */
public class Benchmark extends SimpleBenchmark {


    public enum Library {

        JAVA_MANUAL(new ManualBasicMapper()),
        ORIKA_NOCYCLES(new OrikaNonCyclicMapper()),
        ORIKA_BOUND(new BoundOrikaMapper()),
        ORIKA_GENERAL(new GeneralOrikaMapper()),
        GEDA(new GeDABasicMapper()),
        MODELMAPPER(new ModelMapperMapper()),
        DOZER(new DozerBasicMapper())
;
        
        private Mapper mapper;

        Library(final Mapper mapper) {
            this.mapper = mapper;
        }
    }

    @Param
    private Library lib;
    
    @Param({ "1","1000","25000"})
    private int length;

    private Graph graphLoaded;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {

        final Graph graph = new Graph();
        Set<Segment> segments = new HashSet<Segment>();
        Set<Point> points = new HashSet<Point>();
        Point point1 = null;
        Point point2 = null;
        for (int i=0; i < 5; ++i) {
            for (int j=0; j < 5; ++j) {
                for (int k=0; k < 5; ++k) {
                    Point point = new Point();
                    point.setX(i);
                    point.setY(j);
                    point.setZ(k);
                    if (k % 2 == 0) {
                        point1 = point;
                    } else {
                        point2 = point;
                    }
                    points.add(point);
                    if (point1 != null && point2 != null) {
                        Segment segment = new Segment();
                        segment.setPoint1(point1);
                        segment.setPoint2(point2);
                        segments.add(segment);
                    }
                }
            }
        }
        graph.setPoints(points);
        graph.setSegments(segments);
        
        graphLoaded = graph;
        
        
        mapper = lib.mapper;
    }

    public int timeNestedEntityToDTO(int reps) {
        int dummy = 0;
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                dummy ^= mapper.fromEntity(graphLoaded).hashCode();
            }
        }
        return dummy;
    }

    public static void main(String[] args) throws Exception {
        Runner.main(Benchmark.class, args);
    }

}
