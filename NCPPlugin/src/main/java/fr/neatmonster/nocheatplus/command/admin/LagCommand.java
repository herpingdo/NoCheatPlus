package fr.neatmonster.nocheatplus.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.neatmonster.nocheatplus.NoCheatPlus;
import fr.neatmonster.nocheatplus.command.NCPCommand;
import fr.neatmonster.nocheatplus.permissions.Permissions;
import fr.neatmonster.nocheatplus.utilities.CheckUtils;
import fr.neatmonster.nocheatplus.utilities.TickTask;

public class LagCommand extends NCPCommand {

	public LagCommand(NoCheatPlus plugin) {
		super(plugin, "lag", Permissions.ADMINISTRATION_LAG);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		long max = 50L * (1L + TickTask.lagMaxTicks) * TickTask.lagMaxTicks;
		long medium = 50L * TickTask.lagMaxTicks;
		long second = 1200L;
		StringBuilder builder = new StringBuilder(300);
		builder.append("Lag tracking (roughly):");
		builder.append("\nAverage lag:");
		for (long ms : new long[]{second, medium, max}){
			double lag = TickTask.getLag(ms);
			int p = Math.max(0, (int) ((lag - 1.0) * 100.0));
			builder.append(" " + p + "%[" + CheckUtils.fdec1.format((double) ms / 1200.0) + "s]" );
		}
		builder.append("\nLast hour spikes:\n| ");
		long[] spikeDurations = TickTask.getLagSpikeDurations();
		int[] spikes = TickTask.getLagSpikes();
		if (spikes[0] > 0){
			for (int i = 0; i < spikeDurations.length; i++){
				if (i < spikeDurations.length - 1 && spikes[i] == spikes[i + 1]){
					// Ignore these, get printed later.
					continue;
				}
				builder.append((spikes[i] > 0 ? (spikes[i]) : "none") + " > " + spikeDurations[i] + " ms | ");
			}
		}
		else{
			builder.append("none > " + spikeDurations[0] +  " ms |");
		}
		sender.sendMessage(builder.toString());
		return true;
	}

}
