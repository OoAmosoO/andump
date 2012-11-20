package cn.toddapp.andump.data;

public class DumpSettings {

	private static DumpSettings settings;
	private String dumpFilePath;
	private String dumpCommand;
	private String dumpType;


	private boolean filterDebug;

	private DumpSettings() {

	}

	public String getDumpFilePath() {
		return dumpFilePath;
	}

	public void setDumpFilePath(String dumpFilePath) {
		this.dumpFilePath = dumpFilePath;
	}
	
	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

	public boolean isFilterDebug() {
		return filterDebug;
	}

	public void setFilterDebug(boolean filterDebug) {
		this.filterDebug = filterDebug;
	}

	public String getDumpCommand() {
		StringBuilder commandBuilder = new StringBuilder();
		// tcpdump -s 0 -w /sdcard/dump.cap ip host !10.0.2.2
		commandBuilder.append("tcpdump -s 0");
		if (!settings.dumpFilePath.equals("") && settings.dumpFilePath != null)
			commandBuilder.append(" -w " + settings.dumpFilePath);
		if (settings.dumpType != null && !settings.dumpType.equals("ALL"))
			commandBuilder.append(" " + settings.dumpType.toLowerCase());
		else
			commandBuilder.append(" ");
		if (settings.filterDebug == true)
			commandBuilder.append(" and host !10.0.2.2");
		dumpCommand = commandBuilder.toString();
		return dumpCommand;
	}

	public static DumpSettings getSettings() {
		if (settings == null) {
			settings = new DumpSettings();
			settings.dumpType = "ALL";
			settings.filterDebug = true;
			settings.dumpFilePath = "/sdcard/dump.cap";
		}
		return settings;
	}

}
