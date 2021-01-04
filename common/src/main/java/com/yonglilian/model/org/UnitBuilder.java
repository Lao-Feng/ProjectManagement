package com.yonglilian.model.org;

public class UnitBuilder {
	String[][] mDepartmentlist;

	public UnitBuilder() {
	}

	public Unit Build(String[][] Departmentlist) {
		Unit root = null;
		if (Departmentlist != null) {
			if (Departmentlist.length > 0) {
				mDepartmentlist = Departmentlist;
				root = new Unit();
				root.Code = Departmentlist[0][0];
				root.Name = Departmentlist[0][1];
				createTree(root, 2);
			}
		}
		return root;
	}

	private void createTree(Unit root, int len) {
		createTree1(root, 2);
		createTree2(root, len);
	}

	private void createTree1(Unit root, int len) { // 创建部门
		String code = root.Code;
		if (len > 6) {
			return;
		}
		for (int i = 0; i < mDepartmentlist.length; i++) {
			if (mDepartmentlist[i][0].equals(code)) {
				mDepartmentlist[i][0] = "IIIIIIIIIIII";
			}
		}
		for (int i = 0; i < mDepartmentlist.length; i++) {
			String tmp = (String) mDepartmentlist[i][0];
			if (!tmp.equals("IIIIIIIIIIII")) {
				String first = tmp.substring(0, len + 4);
				String last = tmp.substring(len + 6, 12);
				if (code.substring(0, len + 4).equals(first)) {
					if (code.substring(len + 6, 12).equals(last)) {
						Unit t = new Unit();
						t.Code = tmp;
						t.Name = mDepartmentlist[i][1];
						createTree1(t, len + 2);
						// createTree2(t, len + 2);
						root.Child.add(t);
					}
				}
			}
		}
	}

	private void createTree2(Unit root, int len) { // 创建行政区划
		String code = root.Code;
		if (len > 6) {
			return;
		}
		// 这种情况是行政区划
		for (int i = 0; i < mDepartmentlist.length; i++) {
			String tmp = mDepartmentlist[i][0];
			if (!tmp.equals(code)) {
				if (tmp.substring(0, len).equals(code.substring(0, len))) {
					if (Long.parseLong(tmp.substring(len + 2, 12)) == 0) {
						Unit t = new Unit();
						t.Code = tmp;
						t.Name = mDepartmentlist[i][1];

						createTree1(t, 2);
						createTree2(t, len + 2);
						root.Child.add(t);
					}
				}
			}
		}
	}
}
