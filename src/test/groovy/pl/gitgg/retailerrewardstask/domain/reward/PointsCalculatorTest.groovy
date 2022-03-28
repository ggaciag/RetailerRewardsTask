package pl.gitgg.retailerrewardstask.domain.reward

import spock.lang.Specification

class PointsCalculatorTest extends Specification {

    PointsCalculator pointsCalculator = new PointsCalculator();

    def "can correctly calculate points"(List<Integer> values, Long result){
        expect:
        result == pointsCalculator.calculate(values.collect {new BigDecimal(it)})
        where:
        values      | result
        [120]       | 90
        [120,120]   | 180
        [100]       | 50
        [101]       | 52
        [99]        | 49
        [50]        | 0
        [-10]       | 0
        [-120, -120]| 0
        [50, 50, 10]| 0
        [100, 100, 100]| 150
        [200]       | 250
        [0]         | 0
    }

}
