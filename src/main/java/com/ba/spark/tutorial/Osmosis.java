package com.ba.spark.tutorial; /**
 * Created by bartyushenko on 22.06.16.
 */

import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmRelationMember;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.xml.dynsax.OsmXmlIterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class Osmosis {
	private  static Logger log = LogManager.getLogger(Osmosis.class);

	public static OsmNode getNodeById(long id) {
		try (InputStream input = new URL("http://www.openstreetmap.org/api/0.6/node/" + id).openStream()) {
			return (OsmNode) new OsmXmlIterator(input, false).next().getEntity();
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static List<OsmNode> getBoundariesByWay(long wayId) throws IOException {
		try (InputStream input = new URL("http://www.openstreetmap.org/api/0.6/way/" + wayId).openStream()) {
			return MyOsmUtils.getMembers((OsmWay) new OsmXmlIterator(input, false).next().getEntity()).stream()
					.map(id -> getNodeById(id))
					.collect(Collectors.toList());
		}
	}

	public static String getCityRequest(String cityName) {
		return "http://www.overpass-api.de/api/xapi?relation[name=" + cityName + "]";
	}

	public static Boundary getOuterBoundary(long relationId, Boundary boundary) {
		String query = "http://www.openstreetmap.org/api/0.6/relation/" + relationId;
		return getOuterBoundary(query, boundary);
	}

	public static Set<OsmRelationMember> getMembers(String query) {
		Iterator<EntityContainer> entityContainerIterator = null;
		try (InputStream input = new URL(query)
				.openStream()) {
			entityContainerIterator = new OsmXmlIterator(input, false).iterator();
		}
		catch (Exception e) {
			log.error(e);
		}

		Set<OsmRelationMember> result = new HashSet<>();
		try {
			while (entityContainerIterator.hasNext()) {
				try {
					EntityContainer next = entityContainerIterator.next();
					if (next != null && (OsmRelation) next.getEntity() != null) {
						result.addAll(MyOsmUtils.getMembers((OsmRelation) next.getEntity()));
					}
				}
				catch (Exception e) {
					log.error(e);
				}
			}
		}
		catch (Exception e) {
			log.error(e);
		}

		return result;
	}

	public static List<Long> getInnerRelations(String query) {
		return
				getMembers(query).stream()
						.filter(m -> EntityType.Relation == m.getType())
						.map(m -> m.getId())
						.collect(Collectors.toList());
	}

	public static Boundary getOuterBoundary(Set<OsmRelationMember> members, Boundary boundary) {
		for (OsmRelationMember m : members) {
			try {
				if (EntityType.Node == m.getType()) {
					boundary.includeNode(getNodeById(m.getId()));
				}

				if (EntityType.Way == m.getType() && "inner".equals(m.getRole())) {
					getBoundariesByWay(m.getId()).forEach(n -> boundary.includeNode(n));
				}

				if (EntityType.Relation == m.getType()) {
					getOuterBoundary(m.getId(), boundary);
				}
			}
			catch (IOException | RuntimeException e) {
				log.error(e);
			}
		}

		return boundary;
	}

	public static Boundary getOuterBoundary(final List<Long> innerRelations) {
		Boundary boundary = new Boundary();
		for (Long id : innerRelations) {
			try {
				getOuterBoundary(id, boundary);
			}
			catch (Exception e) {
				log.error(e);
			}
		}

		return boundary;
	}

	public static Boundary getOuterBoundary(String query, Boundary boundary) {
		return getOuterBoundary(getMembers(query), boundary);
	}

	public static Map<String, String> countAmenitiesWithin(Boundary coordinates) throws ParserConfigurationException,
			SAXException, IOException {
		String query = "http://www.overpass-api.de/api/xapi?map?bbox="
				+ coordinates.longitudeMin + "," + coordinates.latitudeMin + "," + coordinates.longitudeMax + "," + coordinates.latitudeMax;

		try (InputStream input = new URL(query).openStream()) {
			Map<String, String> result = new HashMap<>();
			StreamSupport.stream(
					Spliterators.spliteratorUnknownSize(new OsmXmlIterator(input, false).iterator(), Spliterator.ORDERED),
					false)
					.filter(c -> c != null)
					.filter(c -> c.getType() == EntityType.Node)
					.map(c -> (OsmNode) c.getEntity())
					.map(n -> OsmModelUtil.getTagsAsMap(n))
					.filter(m -> m != null && m.containsKey("amenity"))
					.collect(Collectors.groupingBy(m -> m.get("amenity"), Collectors.counting()))
					.forEach((k, val) -> result.put(k, val.toString()));
			return result;
		}
		catch (IOException e) {
			log.error(e);
			return Collections.singletonMap("query", query);
		}

	}
/*
	public static void main(String[] strings) throws Exception {
		try (JavaSparkContext jsc = new JavaSparkContext(new SparkConf()
				.setJars(MyOsmUtils.JARS)
				.setAppName("RegressionAppLocal"))) {
			JavaRDD<String> dataSet = jsc.parallelize(Arrays.asList("Київ", "Москва", "London"));

			List<String> caps = dataSet
					.map(c -> new Tuple2<>(c, getCityRequest(c)))
					.map(m -> new Tuple2<>(m._1(), getOuterBoundary(getMembers(m._2()), new Boundary())))
					.filter(b -> !Boundary.isNull(b._2()))
					.map(b -> new Tuple3<>(b._1(), b._2(), countAmenitiesWithin(b._2())))
					.map(a -> a._1() + " " + a._2() + " " + a._3())
					.collect();

			for (String ct : caps) {
				System.out.println(ct);
			}
		}
	}
*/
}
