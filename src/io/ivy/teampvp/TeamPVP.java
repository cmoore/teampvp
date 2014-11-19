package io.ivy.teampvp;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;

public class TeamPVP extends Plugin {

	@Override
	public boolean enable() {
		Canary.log.info("Loading...");
		Canary.hooks().registerListener(new TeamPVPListener(), this);
		return true;
	}
	
	@Override
	public void disable() {
		Canary.log.info("Disabling...");
	}
}
