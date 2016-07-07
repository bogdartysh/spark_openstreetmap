package com.ba.spark.tutorial;

import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmRelationMember;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bartyushenko on 23.06.16.
 */
public final class MyOsmUtils {
	public static List<Long> getMembers(OsmWay way) {
		List<Long> list = new ArrayList<>(way.getNumberOfNodes());
		if (way.getNumberOfNodes() > 0)
			for (int i = 0; i < way.getNumberOfNodes(); i++) {
				list.add(way.getNodeId(i));
			}

		return list;
	}

	public static List<OsmRelationMember> getMembers(OsmRelation relation) {
		List<OsmRelationMember> list = new ArrayList<>(relation.getNumberOfMembers());
		if (relation.getNumberOfMembers() > 0)
			for (int i = 0; i < relation.getNumberOfMembers(); i++) {
				list.add(relation.getMember(i));
			}

		return list;
	}

	public static final String[] JARS = new String[] {"/work/sparl/target/spark-tutorial-1.0-SNAPSHOT.jar",
			//ls target/libs | while read -r lib; do echo \"$(pwd)/$lib\"\,; done | tr '\n' ' '
			"/work/sparl/target/lib/activation-1.1.jar", "/work/sparl/target/lib/adt-multicollections-0.0.3.jar",
			"/work/sparl/target/lib/akka-actor_2.10-2.3.11.jar", "/work/sparl/target/lib/akka-remote_2.10-2.3.11.jar",
			"/work/sparl/target/lib/akka-slf4j_2.10-2.3.11.jar", "/work/sparl/target/lib/aopalliance-1.0.jar",
			"/work/sparl/target/lib/asm-3.1.jar", "/work/sparl/target/lib/avro-1.7.7.jar",
			"/work/sparl/target/lib/avro-ipc-1.7.7.jar", "/work/sparl/target/lib/avro-ipc-1.7.7-tests.jar",
			"/work/sparl/target/lib/avro-mapred-1.7.7-hadoop2.jar", "/work/sparl/target/lib/chill_2.10-0.5.0.jar",
			"/work/sparl/target/lib/chill-java-0.5.0.jar", "/work/sparl/target/lib/commons-beanutils-1.7.0.jar",
			"/work/sparl/target/lib/commons-beanutils-core-1.8.0.jar", "/work/sparl/target/lib/commons-cli-1.2.jar",
			"/work/sparl/target/lib/commons-codec-1.3.jar", "/work/sparl/target/lib/commons-collections-3.2.1.jar",
			"/work/sparl/target/lib/commons-compiler-2.7.8.jar", "/work/sparl/target/lib/commons-compress-1.4.1.jar",
			"/work/sparl/target/lib/commons-configuration-1.6.jar", "/work/sparl/target/lib/commons-digester-1.8.jar",
			"/work/sparl/target/lib/commons-httpclient-3.1.jar", "/work/sparl/target/lib/commons-io-2.4.jar",
			"/work/sparl/target/lib/commons-lang-2.4.jar", "/work/sparl/target/lib/commons-lang3-3.3.2.jar",
			"/work/sparl/target/lib/commons-math-2.1.jar", "/work/sparl/target/lib/commons-math3-3.4.1.jar",
			"/work/sparl/target/lib/commons-net-2.2.jar", "/work/sparl/target/lib/compress-lzf-1.0.3.jar",
			"/work/sparl/target/lib/config-1.2.1.jar", "/work/sparl/target/lib/curator-client-2.4.0.jar",
			"/work/sparl/target/lib/curator-framework-2.4.0.jar", "/work/sparl/target/lib/curator-recipes-2.4.0.jar",
			"/work/sparl/target/lib/dynsax-0.0.1.jar", "/work/sparl/target/lib/gmbal-api-only-3.0.0-b023.jar",
			"/work/sparl/target/lib/grizzly-framework-2.1.2.jar", "/work/sparl/target/lib/grizzly-http-2.1.2.jar",
			"/work/sparl/target/lib/grizzly-http-server-2.1.2.jar", "/work/sparl/target/lib/grizzly-http-servlet-2.1.2.jar",
			"/work/sparl/target/lib/grizzly-rcm-2.1.2.jar", "/work/sparl/target/lib/guava-14.0.1.jar",
			"/work/sparl/target/lib/guice-3.0.jar", "/work/sparl/target/lib/hadoop-annotations-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-auth-2.2.0.jar", "/work/sparl/target/lib/hadoop-client-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-common-2.2.0.jar", "/work/sparl/target/lib/hadoop-hdfs-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-mapreduce-client-app-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-mapreduce-client-common-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-mapreduce-client-core-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-mapreduce-client-jobclient-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-mapreduce-client-shuffle-2.2.0.jar", "/work/sparl/target/lib/hadoop-yarn-api-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-yarn-client-2.2.0.jar", "/work/sparl/target/lib/hadoop-yarn-common-2.2.0.jar",
			"/work/sparl/target/lib/hadoop-yarn-server-common-2.2.0.jar", "/work/sparl/target/lib/ivy-2.4.0.jar",
			"/work/sparl/target/lib/jackson-annotations-2.4.4.jar", "/work/sparl/target/lib/jackson-core-2.4.4.jar",
			"/work/sparl/target/lib/jackson-core-asl-1.9.13.jar", "/work/sparl/target/lib/jackson-databind-2.4.4.jar",
			"/work/sparl/target/lib/jackson-jaxrs-1.8.3.jar", "/work/sparl/target/lib/jackson-mapper-asl-1.9.13.jar",
			"/work/sparl/target/lib/jackson-module-scala_2.10-2.4.4.jar", "/work/sparl/target/lib/jackson-xc-1.8.3.jar",
			"/work/sparl/target/lib/janino-2.7.8.jar", "/work/sparl/target/lib/javax.inject-1.jar",
			"/work/sparl/target/lib/javax.servlet-3.0.0.v201112011016.jar", "/work/sparl/target/lib/javax.servlet-3.1.jar",
			"/work/sparl/target/lib/javax.servlet-api-3.0.1.jar", "/work/sparl/target/lib/jaxb-api-2.2.2.jar",
			"/work/sparl/target/lib/jaxb-impl-2.2.3-1.jar", "/work/sparl/target/lib/jcl-over-slf4j-1.7.10.jar",
			"/work/sparl/target/lib/jersey-client-1.9.jar", "/work/sparl/target/lib/jersey-core-1.9.jar",
			"/work/sparl/target/lib/jersey-grizzly2-1.9.jar", "/work/sparl/target/lib/jersey-guice-1.9.jar",
			"/work/sparl/target/lib/jersey-json-1.9.jar", "/work/sparl/target/lib/jersey-server-1.9.jar",
			"/work/sparl/target/lib/jersey-test-framework-core-1.9.jar",
			"/work/sparl/target/lib/jersey-test-framework-grizzly2-1.9.jar", "/work/sparl/target/lib/jets3t-0.7.1.jar",
			"/work/sparl/target/lib/jettison-1.1.jar", "/work/sparl/target/lib/jetty-util-6.1.26.jar",
			"/work/sparl/target/lib/jline-0.9.94.jar", "/work/sparl/target/lib/joda-time-2.7.jar",
			"/work/sparl/target/lib/json4s-ast_2.10-3.2.10.jar", "/work/sparl/target/lib/json4s-core_2.10-3.2.10.jar",
			"/work/sparl/target/lib/json4s-jackson_2.10-3.2.10.jar", "/work/sparl/target/lib/jsr305-1.3.9.jar",
			"/work/sparl/target/lib/jul-to-slf4j-1.7.10.jar", "/work/sparl/target/lib/kryo-2.21.jar",
			"/work/sparl/target/lib/leveldbjni-all-1.8.jar", "/work/sparl/target/lib/log4j-1.2.17.jar",
			"/work/sparl/target/lib/lz4-1.3.0.jar", "/work/sparl/target/lib/management-api-3.0.0-b012.jar",
			"/work/sparl/target/lib/mesos-0.21.1-shaded-protobuf.jar", "/work/sparl/target/lib/metrics-core-3.1.2.jar",
			"/work/sparl/target/lib/metrics-graphite-3.1.2.jar", "/work/sparl/target/lib/metrics-json-3.1.2.jar",
			"/work/sparl/target/lib/metrics-jvm-3.1.2.jar", "/work/sparl/target/lib/minlog-1.2.jar",
			"/work/sparl/target/lib/netty-3.8.0.Final.jar", "/work/sparl/target/lib/netty-all-4.0.29.Final.jar",
			"/work/sparl/target/lib/objenesis-1.2.jar", "/work/sparl/target/lib/oro-2.0.8.jar",
			"/work/sparl/target/lib/osm4j-core-0.0.18.jar", "/work/sparl/target/lib/osm4j-xml-0.0.3.jar",
			"/work/sparl/target/lib/paranamer-2.6.jar", "/work/sparl/target/lib/parquet-column-1.7.0.jar",
			"/work/sparl/target/lib/parquet-common-1.7.0.jar", "/work/sparl/target/lib/parquet-encoding-1.7.0.jar",
			"/work/sparl/target/lib/parquet-format-2.3.0-incubating.jar", "/work/sparl/target/lib/parquet-generator-1.7.0.jar",
			"/work/sparl/target/lib/parquet-hadoop-1.7.0.jar", "/work/sparl/target/lib/parquet-jackson-1.7.0.jar",
			"/work/sparl/target/lib/protobuf-java-2.5.0.jar", "/work/sparl/target/lib/py4j-0.9.jar",
			"/work/sparl/target/lib/pyrolite-4.9.jar", "/work/sparl/target/lib/reflectasm-1.07-shaded.jar",
			"/work/sparl/target/lib/RoaringBitmap-0.5.11.jar", "/work/sparl/target/lib/scala-compiler-2.10.0.jar",
			"/work/sparl/target/lib/scala-library-2.10.5.jar", "/work/sparl/target/lib/scalap-2.10.0.jar",
			"/work/sparl/target/lib/scala-reflect-2.10.4.jar", "/work/sparl/target/lib/slf4j-api-1.7.10.jar",
			"/work/sparl/target/lib/slf4j-log4j12-1.7.10.jar", "/work/sparl/target/lib/snappy-java-1.1.2.jar",
			"/work/sparl/target/lib/spark-catalyst_2.10-1.6.1.jar", "/work/sparl/target/lib/spark-core_2.10-1.6.1.jar",
			"/work/sparl/target/lib/spark-launcher_2.10-1.6.1.jar", "/work/sparl/target/lib/spark-network-common_2.10-1.6.1.jar",
			"/work/sparl/target/lib/spark-network-shuffle_2.10-1.6.1.jar", "/work/sparl/target/lib/spark-sql_2.10-1.6.1.jar",
			"/work/sparl/target/lib/spark-unsafe_2.10-1.6.1.jar", "/work/sparl/target/lib/stax-api-1.0.1.jar",
			"/work/sparl/target/lib/stream-2.7.0.jar", "/work/sparl/target/lib/tachyon-client-0.8.2.jar",
			"/work/sparl/target/lib/tachyon-underfs-hdfs-0.8.2.jar", "/work/sparl/target/lib/tachyon-underfs-local-0.8.2.jar",
			"/work/sparl/target/lib/tachyon-underfs-s3-0.8.2.jar", "/work/sparl/target/lib/trove4j-3.0.3.jar",
			"/work/sparl/target/lib/uncommons-maths-1.2.2a.jar", "/work/sparl/target/lib/unused-1.0.0.jar",
			"/work/sparl/target/lib/xbean-asm5-shaded-4.4.jar", "/work/sparl/target/lib/xmlenc-0.52.jar",
			"/work/sparl/target/lib/xz-1.0.jar", "/work/sparl/target/lib/zookeeper-3.4.5.jar"
	};
}
