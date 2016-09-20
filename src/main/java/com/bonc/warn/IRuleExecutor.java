package com.bonc.warn;

import net.sf.json.JSONObject;

public interface IRuleExecutor {

	public JSONObject execute(JSONObject json, Regulation rule);
}
