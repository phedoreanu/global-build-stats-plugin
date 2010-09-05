package hudson.plugins.global_build_stats.xstream.migration.v2;

import hudson.plugins.global_build_stats.model.BuildStatConfiguration;
import hudson.plugins.global_build_stats.model.JobBuildResult;
import hudson.plugins.global_build_stats.model.ModelIdGenerator;
import hudson.plugins.global_build_stats.xstream.migration.GlobalBuildStatsXStreamReader;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * Reader for GlobalBuildStats v2 XStream representation
 * @author fcamblor
 */
public class V2XStreamReader implements GlobalBuildStatsXStreamReader<V2GlobalBuildStatsPOJO>{

	public V2GlobalBuildStatsPOJO readGlobalBuildStatsPOJO(
			HierarchicalStreamReader reader, UnmarshallingContext context) {

		V2GlobalBuildStatsPOJO pojo = new V2GlobalBuildStatsPOJO();
		
		reader.moveDown();
		List<JobBuildResult> jobBuildResults = new ArrayList<JobBuildResult>();
		while(reader.hasMoreChildren()){
			reader.moveDown();
			
			JobBuildResult jbr = (JobBuildResult)context.convertAnother(pojo, JobBuildResult.class);
			jobBuildResults.add(jbr);
			
			reader.moveUp();
		}
		reader.moveUp();
		
		reader.moveDown();
		List<BuildStatConfiguration> buildStatConfigs = new ArrayList<BuildStatConfiguration>();
		while(reader.hasMoreChildren()){
			reader.moveDown();
			
			BuildStatConfiguration bsc = (BuildStatConfiguration)context.convertAnother(pojo, BuildStatConfiguration.class);
			buildStatConfigs.add(bsc);
			
			// Registering BuildStatConfiguration's id in the ModelIdGenerator
			ModelIdGenerator.INSTANCE.registerIdForClass(BuildStatConfiguration.class, bsc.getId());

			reader.moveUp();
		}
		reader.moveUp();

		pojo.jobBuildResults = jobBuildResults;
		pojo.buildStatConfigs = buildStatConfigs;
		
		return pojo;
	}
}