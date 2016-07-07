package com.ba.spark.tutorial;

import static com.ba.spark.tutorial.Osmosis.countAmenitiesWithin;
import static com.ba.spark.tutorial.Osmosis.getCityRequest;
import static com.ba.spark.tutorial.Osmosis.getInnerRelations;
import static com.ba.spark.tutorial.Osmosis.getOuterBoundary;

import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import scala.Tuple3;

/**
 * Created by bartyushenko on 06.07.16.
 */
public class Osmosis1 {
	public static void main(String[] strings) throws Exception {
		try (JavaSparkContext jsc = new JavaSparkContext(new SparkConf()
				.setJars(MyOsmUtils.JARS)
				.setAppName("RegressionAppLocal2"))) {
			JavaRDD<String> dataSet = jsc.parallelize(Arrays.asList("Київ", "Москва"));
			JavaRDD<Tuple3<String, List<Long>, String>> cityDistricts =
					dataSet
							.map(c -> new Tuple2<>(c, getCityRequest(c)))
							.map(c -> new Tuple3<>(c._1(), getInnerRelations(c._2()), c._2()))
							.persist(StorageLevel.MEMORY_ONLY());

			JavaRDD<Tuple2<String, Boundary>> cityOuterBounderies =
					cityDistricts
							.filter(m -> !m._2().isEmpty())
							.map(m -> new Tuple2<>(m._1(), getOuterBoundary(m._2())))
							.persist(StorageLevel.MEMORY_ONLY());

			List<String> caps = cityOuterBounderies
					.filter(b -> !Boundary.isNull(b._2()))
					.map(b -> new Tuple3<>(b._1(), b._2(), countAmenitiesWithin(b._2())))
					.map(a -> a._1() + " " + a._2() + " " + a._3())
					.collect();

			System.out.println("stats");
			for (String ct : caps) {
				System.out.println(ct);
			}

			System.out.println("boundaries");
			for (Tuple2<String, Boundary> cb : cityOuterBounderies.collect()) {
				System.out.println(cb);
			}
			System.out.println("districts");
			for (Tuple3<String, List<Long>, String> district : cityDistricts.collect()) {
				System.out.println(district);
			}

		}
	}
}
