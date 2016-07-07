package com.ba.spark.tutorial;

import static java.lang.Math.round;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

/**
 * Created by bartyushenko on 15.06.16.
 */
public class Regression {
	public static final int N = 1000;
	public static BigDecimal factorial(long n) {
		BigDecimal ret = BigDecimal.ONE;
		for (int i = 1; i <= n; ++i) ret = ret.multiply(BigDecimal.valueOf(i));
		return ret;
	}

	public static BigDecimal c_n_k(long n, long k) {
		return factorial(n).divide(factorial(k).multiply(factorial(n-k)));
	}

	public static BigDecimal prob_gG_bB(long g, long G, long b, long B) {
		return  c_n_k(G, g)
				.divide(c_n_k(G+B, G ) , MathContext.DECIMAL32)
				.multiply(c_n_k(B,b))
				.divide(c_n_k(G+B, B), MathContext.DECIMAL32)
				;
	}

	public static double prob_goodness(double rg, double rb, double goodness) {
		return prob_gG_bB(round(rg *N / 100), round(rb * N / 100), round(goodness * N /100), round((100.0 - goodness) * N / 100)).doubleValue();
	}

	public static void main2(String [] strings) {







		SparkConf conf = new SparkConf()
				//.setMaster("spark://172.26.133.82:7077")

				.setAppName("RegressionApp")
				//.set("spark.executor.memory", "1g")
				;

		JavaSparkContext jsc = new JavaSparkContext(conf);

		int slices =  2;

		List<Double> alice= Arrays.asList(7.6, 45.3, 42+5.1);
		List<Double> bob=Arrays.asList(13.0,52.4,28.5+6.1);
		List<Double> sam=Arrays.asList(10.2, 45.5, 38.8+5.5);

		List<List<Double>> input = IntStream.range(5,55).filter(i -> i%5 == 0).boxed().map(inp -> Arrays.asList(
				inp/100.0,
				alice.get(0), alice.get(1),
				alice.get(0), alice.get(1),
				alice.get(0), alice.get(1),
				bob.get(0), bob.get(1),
				sam.get(0), sam.get(1)
		)).collect(Collectors.toList());

		JavaRDD<List<Double>> dataSet = jsc.parallelize(input, slices);

		double total = dataSet.map(inp -> Arrays.asList(
				inp.get(0),

				prob_goodness(inp.get(0), inp.get(1), inp.get(2)),
				prob_goodness(inp.get(0), inp.get(3), inp.get(4)),
				prob_goodness(inp.get(0), inp.get(5), inp.get(6))
				))
				.persist(StorageLevel.MEMORY_AND_DISK())
				.map(arr -> Arrays.asList(arr.get(0),
						arr.get(1)*arr.get(2)*arr.get(3)
						))
				.persist(StorageLevel.MEMORY_AND_DISK())
				.map( arr -> arr.get(1))
				.reduce((a, b) -> a + b).doubleValue()


						;


		System.out.println("done");

		jsc.stop();


		//System.out.println(factorial(N));
	}
}
