package com.bonc.monitor.software;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.ha.HAServiceProtocol;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.api.records.YarnClusterMetrics;
import org.apache.hadoop.yarn.client.RMHAServiceTarget;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.HAUtil;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.bonc.monitor.IMonitor;
import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class YARNMonitorImpl implements IMonitor {

	private Log log = LogFactory.getLog(YARNMonitorImpl.class);
	private DecimalFormat dformat = new DecimalFormat("#.00");
	private static Configuration conf;
	private YarnClient client;
	static {
		conf = new Configuration();
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/yarn-site.xml"));
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/mapred-site.xml"));
	}

	public YARNMonitorImpl() {
		client = YarnClient.createYarnClient();
		client.init(conf);
		client.start();

	}

	@Override
	public JSONArray getJsonData() {
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("node_state", getNodeState());
		obj.put("app_state", getApplicationState());
		obj.put("rm_state", getRMState()); 
		array.add(obj);
		return array;
	}

	public JSONArray getRMState() {

		YarnConfiguration yarnConf = new YarnConfiguration(conf);
		yarnConf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/yarn-site.xml"));
		yarnConf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/mapred-site.xml"));

		Collection<String> collect = HAUtil.getRMHAIds(yarnConf);
		JSONArray array = new JSONArray();
		for (String rmid : collect) {
			JSONObject json = new JSONObject();
			yarnConf.set(YarnConfiguration.RM_HA_ID, rmid);
			HAServiceProtocol proto;
			try {
				proto = new RMHAServiceTarget(yarnConf).getProxy(yarnConf,
						CommonConfigurationKeys.HA_FC_CLI_CHECK_TIMEOUT_DEFAULT);
				json.put("state", proto.getServiceStatus().getState());
			} catch (Exception e) {
				log.error("can not get connection to yarn resoucemanager ,please sure rm has start", e);
				json.put("state", "lost");
				json.put("reason", "can not connection " + rmid);
				continue;
			} finally{
				json.put("id", rmid);
				json.put("hostname", yarnConf.get("yarn.resourcemanager.address." + rmid).split(":")[0]);
				array.add(json);				
			}
		}
		return array;
	}

	public JSONObject getApplicationState() {

		JSONObject json = new JSONObject();
		try {
			EnumSet<YarnApplicationState> set = EnumSet.noneOf(YarnApplicationState.class);
			set.add(YarnApplicationState.RUNNING);
			json.put("runing_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.NEW);
			set.add(YarnApplicationState.NEW_SAVING);
			json.put("new_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.SUBMITTED);
			json.put("submited_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.FINISHED);
			json.put("finished_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.FAILED);
			json.put("failed_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.KILLED);
			json.put("killed_app", client.getApplications(set).size());
			set.clear();
			set.add(YarnApplicationState.ACCEPTED);
			json.put("accepted_app", client.getApplications(set).size());
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("get yarn app status exception : ", e);
		}
		return null;
	}

	/**
	 * 获取 yarn 的节点状态,和整体的资源状态
	 * 
	 * @param array
	 */
	public JSONObject getNodeState() {
		client.start();
		try {
			YarnClusterMetrics metrics = client.getYarnClusterMetrics();
			JSONObject state = new JSONObject();
			state.put("active_node", metrics.getNumActiveNodeManagers());
			state.put("decommissioned_node", metrics.getNumDecommissionedNodeManagers());
			state.put("lost_node", metrics.getNumLostNodeManagers());
			state.put("node", metrics.getNumNodeManagers());
			state.put("rebooted_node", metrics.getNumRebootedNodeManagers());
			state.put("unhealthy", metrics.getNumUnhealthyNodeManagers());
			List<NodeReport> reports = client.getNodeReports();
			JSONArray nodeArray = new JSONArray();
			state.put("memory", 0);
			state.put("vcores", 0);
			state.put("used_memory", 0);
			state.put("used_vcores", 0);
			for (NodeReport rp : reports) {
				JSONObject json = new JSONObject();
				json.put("hostname", rp.getHttpAddress());
				json.put("state", rp.getNodeState());
				json.put("containerNum", rp.getNumContainers());
				json.put("unit", "MB");
				// 计算整体资源状态
				state.put("memory", state.getInt("memory") + rp.getCapability().getMemory());
				state.put("vcores", state.getInt("vcores") + rp.getCapability().getVirtualCores());
				state.put("used_vcores", state.getInt("used_memory") + rp.getUsed().getMemory());
				state.put("used_vcore", state.getInt("used_vcores") + rp.getUsed().getVirtualCores());
				json.put("memory", rp.getCapability().getMemory());
				json.put("vcores", rp.getCapability().getVirtualCores());
				json.put("used_memory", rp.getUsed().getMemory());
				json.put("used_vcore", rp.getUsed().getVirtualCores());
				// json.put("id", rp.getNodeId());
				json.put("rack", rp.getRackName());
				json.put("last_health_time", rp.getLastHealthReportTime());
				nodeArray.add(json);
			}
			state.put("node_state", nodeArray);
			return state;
		} catch (Exception e) {
			log.error(" get Yarn node state error: ", e);
		}
		return null;
	}

	public static void main(String[] args) {

		YARNMonitorImpl monitor = new YARNMonitorImpl();
		System.out.println(monitor.getRMState());
		System.out.println(monitor.getJsonData());

	}

}
