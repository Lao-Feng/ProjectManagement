/* Generated by Together */

package zr.zrpower.flowengine.runtime;

/**
 * 流程过程类
 * @author lwk
 *
 */
public class Process {
    public Process() {
    }

    public Process(String ID) {
    }

    public Activity getCurrActivity() {
    	return null;
    }

    private String ID;
    private FlowPackage packageInfo;
	private Activity[] activityList;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public FlowPackage getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(FlowPackage packageInfo) {
		this.packageInfo = packageInfo;
	}

	public Activity[] getActivityList() {
		return activityList;
	}

	public void setActivityList(Activity[] activityList) {
		this.activityList = activityList;
	}
}