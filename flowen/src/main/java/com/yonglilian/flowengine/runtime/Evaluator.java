package com.yonglilian.flowengine.runtime;

import com.yonglilian.common.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 表达式计算类 描述: 表达式示例: 2+3*EXP(3.3*X, 5)/3-2*(3+2/2*X)/2-3 说明:
 * 1.为简化算法,表达式中无正、负号,要得到负号的作用请用 (1-2)*? 来表示 2.表达式中的元素(现区分大小写)有: 数值常数(包括小数点): 如:
 * 3.2 8.3 运算符: 如: + - * / 括号: ( ) 变量: 运算时将实际值代入其中,用一单词(字符串,原则上不应包括数字)来表示,
 * 赋值时用类的索引器表示 如: X Y Delta 用户自定义函数: 函数的操作要用括号()括起来,里面的表达式用递归来求 如: EXP(2+3*X,
 * 2), 但是函数里的表达式不能再有函数(即用户自定义函数不允许递归) 分隔符: 空格(不会放入符号栈中)
 * 逗号(用于用户自定义函数中,分隔各参数,要放入栈中), 各运算符默认已是分隔符 3.算法中要用到的: 单词解析函数:
 * 将表达式中的各元素解析放到一字符串数组中(其中的每一字符串表示一个元素) 单词表(数组): 存放单词解析函数解析的元素 符号栈: 运算时存放符号 数值栈:
 * 运算时存放数值(及中间值) 解析函数: 核心,对表达式作运算并求出最终结果
 * 
 * @author:NFZR
 * 
 */
public class Evaluator {
	Log log;

	/******************* 开始*******表达式计算的必要前期设计内容 ************************/
	/**
	 *
	 *
	 * 数值栈
	 */
	public class ValStack {
		@SuppressWarnings("rawtypes")
		private Stack stack = new Stack();

		/**
		 * 弹出栈顶值
		 * 
		 * @return double
		 */
		public double Pop() {
			if (stack.size() == 0) {
				return 0;
			} else {
				return Double.parseDouble(String.valueOf(stack.pop()));
			}
		}

		/**
		 * 向栈中压入值
		 * 
		 * @param val
		 *            double
		 */
		@SuppressWarnings("unchecked")
		public void Push(double val) {
			Double d = new Double(val);
			stack.push((Object) d);
		}

		/**
		 * 取栈顶值，但不弹出
		 */
		public double Peek() {
			if (stack.size() == 0) {
				return 0;
			} else {
				return Double.parseDouble(String.valueOf(stack.peek()));
			}
		}

		/**
		 * 栈中数值个数
		 */
		public int Count() {
			return stack.size();
		}

		/**
		 * 清空栈
		 */
		public void Clear() {
			stack.clear();
		}
	}

	/**
	 * 符号栈
	 */
	public class SignStack {
		@SuppressWarnings("rawtypes")
		private Stack stack = new Stack();

		/**
		 * 弹出栈顶值
		 */
		public String Pop() {
			if (stack.size() == 0) {
				return "";
			} else {
				return String.valueOf(stack.pop());
			}
		}

		/**
		 * 向栈中压入值
		 */
		@SuppressWarnings("unchecked")
		public void Push(String val) {
			stack.push(val.toUpperCase());
		}

		/**
		 * 取栈顶值，但不弹出
		 */
		public String Peek() {
			if (stack.size() == 0) {
				return "";
			} else {
				return (String) stack.peek();
			}
		}

		/**
		 * 栈中字符
		 */
		public int Count() {
			return stack.size();
		}

		/**
		 * 清空栈
		 */

		public void Clear() {
			stack.clear();
		}
	}

	/**
	 * 单词解析
	 */
	public class WordAnalysis {
		/**
		 * 默认分隔符(有分隔单词功能的符号,因此包括一些运算符), 用户可以自定义这些分隔符
		 */
		public String[] Seperator = new String[] { "+", "-", "*", "/", ",", "(", ")", "ABC" };

		/**
		 * 判断字符是否为分隔符
		 */
		public boolean IsSeperator(String s) {

			for (int i = 0; i < Seperator.length; i++) {
				if (s.equals(Seperator[i])) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 单词分析 空格, TAB, 回车 一定是分隔符 如果分隔符由多个字符组成, 则分隔符的第一个字符前要有空格,TAB或回车符
		 */

		@SuppressWarnings("unchecked")
		public String[] Analysis(String str) {
			if (str == null || str.equals("")) {
				return null;
			}
			@SuppressWarnings("rawtypes")
			ArrayList list = new ArrayList();

			String cur = "";

			for (int i = 0; i < str.length(); i++) { // 循环扫描字符串中的每个字符
				String t = String.valueOf(str.charAt(i));
				if (t.equals(" ") || t.equals("\t") || t.equals("\n")) {
					if (!cur.equals("")) {
						list.add(cur);
						cur = "";
					}
				} else if (IsSeperator(t)) {
					if (!cur.equals("")) {
						list.add(cur);
						cur = "";
					}
					list.add(t);
				} else {
					cur += t;

					if (IsSeperator(cur.trim())) {
						list.add(cur);
						cur = "";
					}
				}
			}

			if (!cur.equals("")) {
				list.add(cur);
			}

			String[] ret = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ret[i] = String.valueOf(list.get(i));

			}
			return ret;
		}

	}

	/**
	 * 变量操作类
	 */
	public class Variable {

		private String[] Key;
		private double[] Value;
		private int index; // 已分配的变量个数

		/**
		 * 初始化
		 * 
		 * @param size
		 *            int 充许的最大变量个数
		 */
		public Variable(int size) {
			Key = new String[size];
			Value = new double[size];
			for (int i = 0; i < Key.length; i++) {
				Key[i] = "";
				Value[i] = 0;
			}
			index = 0;
		}

		/**
		 * 返回变量在数组中的索引
		 */
		public int IndexOf(String key) {
			if (key == null || key.equals("")) {
				return -1;
			} else {
				for (int i = 0; i < index; i++) {
					if (Key[i].equals(key)) {
						return i;
					}
				}

				return -1;

			}
		}

		/**
		 * 返回已申明变量的个数
		 */
		public int Count() {
			return index;
		}

		/**
		 * 变量数组的最大容量
		 */

		public int Length() {
			return Key.length;
		}

		/**
		 * 获取指定变量的值
		 */
		public double getValue(String key) {
			int idx = IndexOf(key);
			if (idx == -1) {
				return 0;
			} else {
				return Value[idx];
			}
		}

		/**
		 * 设置指定变量的值
		 */

		public void setValue(String key, double value) throws Exception {
			int idx = IndexOf(key);
			if (idx == -1) {
				if (index < Length()) {
					Key[index] = key;
					Value[index] = value;
					index++;

				} else {
					throw new Exception("新增变量出错, 变量数组已满");

				}
			} else {
				Value[idx] = value;
			}

		}

	}

	/**
	 * 用户自定义函数代理--------------------(未完成，需寻找相关的技术实现)
	 */

	public double Function(double[] param) {
		return 0;
	}

	/**
	 * 用户自定义函数列表
	 */

	public class FunctionList {

		private IAccount[] Func; // 函数执行体的代理
		private String[] FuncName; // 函数名
		private int[] ParamCount; // 参数的个数
		private int _Count; // 列表中已使用的代理的个数

		/**
		 * 获取自定义函数的个数
		 * 
		 * @return int
		 */
		public int Count() {
			return _Count;
		}

		/**
		 * 获取允许的自定义函数的最大个数
		 * 
		 * @return int
		 */
		public int Length() {
			return FuncName.length;
		}

		/**
		 * 初始化自定义函数的最大个数
		 * 
		 * @param size
		 *            int
		 */
		public FunctionList(int size) {
			Func = new IAccount[size];
			FuncName = new String[size];
			ParamCount = new int[size];
			for (int i = 0; i < FuncName.length; i++) {
				Func[i] = null;
				FuncName[i] = "";
				ParamCount[i] = 0;
			}
			_Count = 0;
		}

		/**
		 * 获取指定函数别名在列表中的索引
		 * 
		 * @param funcName
		 *            String 指定的函数别名
		 * @return int 返回的索引
		 */
		public int IndexOf(String funcName) {
			if (funcName == null || funcName.equals("")) {
				return -1;
			}
			for (int i = 0; i < _Count; i++) {
				if (FuncName[i].equals(funcName)) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * 设置指定函数别名的执行体代理
		 */
		public void Add(String funcName, int paramCount, IAccount func) {
			int index = IndexOf(funcName);
			if (index == -1) {
				if (_Count < this.Length()) {
					FuncName[_Count] = funcName;
					Func[_Count] = func;
					ParamCount[_Count] = paramCount;
					_Count++;
				} else {
					// throw new Exception("添加函数出错, 列表已满");
				}

			} else {
				Func[index] = func;
				ParamCount[index] = paramCount;
			}
		}

		/**
		 * 获取指定函数执行体的代理
		 * 
		 * @param func
		 *            String
		 * @return Function
		 */
		public IAccount getFunc(String func) {
			int index = IndexOf(func);
			if (index == -1) {
				return null;
			} else {
				return Func[index];
			}

		}

		/**
		 * 获取自定义函数的参数个数
		 * 
		 * @param func
		 *            String
		 * @return int
		 */
		public int ParameterCount(String func) {
			int index = IndexOf(func);
			if (index == -1) {
				return -1;
			} else {
				return ParamCount[index];
			}

		}

	}

	interface IAccount {
		public double Account(double[] pa);
	}

	/**
	 * 指定数字的指定次幂
	 */
	class POW implements IAccount {
		public double Account(double[] p) {
			return Math.pow(p[0], p[1]);
		}

	}

	/**
	 * 返回e的指定次幂
	 */
	class EXP implements IAccount {
		public double Account(double[] p) {
			return Math.exp(p[0]);
		}
	}

	/**
	 * 返回指定数字的平方根
	 */
	class SQRT implements IAccount {
		public double Account(double[] p) {
			return Math.sqrt(p[0]);
		}
	}

	/**
	 * 返回指定数字的自然对数函数值
	 */
	class LOG implements IAccount {
		public double Account(double[] p) {
			return Math.log(p[0]);
		}
	}

	/**
	 * 返回指定数字的绝对值。
	 */
	class ABS implements IAccount {
		public double Account(double[] p) {
			return Math.abs(p[0]);
		}
	}

	/**
	 * 返回两个数字中较大的一个
	 */
	class MAX implements IAccount {
		public double Account(double[] p) {
			return Math.max(p[0], p[1]);
		}
	}

	/**
	 * 返回两个数字中较小的一个
	 */
	class MIN implements IAccount {
		public double Account(double[] p) {
			return Math.min(p[0], p[1]);
		}
	}

	public class FunctionLib {
		/**
		 * 表达式默认支持函数
		 * 
		 * @param list
		 *            FunctionList
		 */
		public void AssignFunctionLib(FunctionList list) {
			try {
				list.Add("POW", 2, new POW());
				list.Add("EXP", 1, new EXP());
				list.Add("SQRT", 1, new SQRT());
				list.Add("LOG", 2, new LOG());
				list.Add("ABS", 1, new ABS());
				list.Add("MAX", 2, new MAX());
				list.Add("MIN", 2, new MIN());
			} catch (Exception e) {
				// 未处理异常
			}
		}

	}

	/******************* 结束*******表达式计算的必要前期设计内容 ************************/

	public String Expression; // 要计算的表达式
	private String[] Word;
	private ValStack Val;
	private SignStack Sign;

	private WordAnalysis wordAnalysis;

	private FunctionLib functionLib;

	public Variable Var; // 变量列表
	public FunctionList Func; // 函数执行体列表
	// public Function DefFunction; //以本实例表达式作为自定义函数

	/**
	 * 初始化
	 * 
	 * @param expression
	 *            String 要计算的表达式
	 */
	public Evaluator(String expression) {
		log = new Log();
		log.SetLogForClass("Evaluator");
		log.SetLogFile("Evaluator.log");
		Val = new ValStack();
		Sign = new SignStack();
		Var = new Variable(50);
		Func = new FunctionList(20);
		functionLib = new FunctionLib();
		wordAnalysis = new WordAnalysis();
		functionLib.AssignFunctionLib(Func);
		// DefFunction = new Function(this.function);
		this.Expression = expression;
	}

	public Evaluator() {
		this("");
	}

	/**
	 * 对每次更改计算表达式时进行初始化
	 */
	private void InitExpression() {
		try {

			Word = wordAnalysis.Analysis(Expression);

		} catch (Exception e) {

		}

	}

	/**
	 * 是否是自定义函数
	 * 
	 * @param word
	 *            string
	 * @return boolean
	 */
	private boolean IsFunc(String word) {
		return Func.IndexOf(word.toUpperCase()) != -1;
	}

	/**
	 * 是否是数字
	 */
	private boolean IsNum(String word) {
		try {
			Double.valueOf(word);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 是否是变量
	 */
	private boolean IsVar(String word) {
		return Var.IndexOf(word) != -1;
	}

	/**
	 * 是否是符号
	 * 
	 * @param word
	 *            String
	 * @return boolean
	 */
	private boolean IsSign(String word) {
		String sign[] = wordAnalysis.Seperator;
		if (word.trim().equals("")) {
			return false;
		}
		for (int i = 0; i < sign.length; i++) {
			if (word.equals(sign[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 不是数字, 变量, 函数名, 符号 的单词
	 * 
	 * @param word
	 *            String
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private boolean IsOtherWord(String word) {
		return !(IsNum(word) || IsVar(word) || IsFunc(word) || IsSign(word));
	}

	/**
	 * 符号优先级
	 * 
	 * @param sign
	 *            String
	 * @return int
	 */
	private int Priority(String sign) {
		if (sign.equals("(")) {
			return 32767;
		} else if (sign.equals(")")) {
			return -32767;
		} else if (sign.equals("*") || sign.equals("/")) {
			return 8;
		} else if (sign.equals("+") || sign.equals("-")) {
			return 4;
		} else if (sign.equals(",")) {
			return 2;
		} else {
			if (IsFunc(sign)) {
				return 16;
			} else {
				return 0;
			}
		}
	}

	/**
	 * 获取表达式中的变量
	 * 
	 * @return String[]
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[] GetVariables() {
		ArrayList wordList = new ArrayList();
		WordAnalysis wordAnalysis = new WordAnalysis();
		String[] words = wordAnalysis.Analysis(Expression);
		for (int i = 0; i < words.length; i++) {
			if (Character.isLetter(words[i].charAt(0)) && !IsFunc(words[i])) {
				if (wordList.contains(words[i]) == false) {
					wordList.add(words[i]);
				}
			}
		}
		String[] vars = new String[wordList.size()];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = String.valueOf(wordList.get(i));
		}
		return vars;
	}

	/**
	 * 根据计算符号进行运算
	 * 
	 * @param sign
	 *            String
	 * @param a
	 *            double
	 * @param b
	 *            double
	 * @return double
	 */
	private double Compute(String sign, double a, double b) {
		double ret = 0;
		char signFlag = sign.charAt(0);
		switch (signFlag) {
		case '+':
			ret = a + b;
			break;
		case '-':
			ret = a - b;
			break;
		case '*':
			ret = a * b;
			break;
		case '/':
			ret = a / b;
			break;
		default:
			if (IsFunc(sign)) {
				double[] parameter = new double[Func.ParameterCount(sign)];
				for (int i = parameter.length - 1; i >= 0; i--) {
					parameter[i] = Val.Pop();
				}
				IAccount func = Func.getFunc(sign);
				ret = func.Account(parameter);

			} else {
				// throw new Exception("无法识别的符号:" + sign);
			}
			break;
		}

		return ret;
	}

	/**
	 * 计算表达式的值
	 */
	public double Calculate() throws Exception {

		this.InitExpression();

		int cur; // 当前计算到的单词在Word数组中的索引(位置)
		for (cur = 0; cur < Word.length; cur++) { // 循环读单词,计算表达式.

			if (IsNum(Word[cur])) {
				Val.Push(Double.parseDouble(Word[cur]));
			} else if (Word[cur].equals("(")) {
				Sign.Push(Word[cur]);
			} else if (Word[cur].equals(",")) {
			} else if (IsFunc(Word[cur])) {
				Sign.Push(Word[cur]);
			} else if (IsVar(Word[cur])) {
				Val.Push(Var.getValue(Word[cur]));
			} else if (IsSign(Word[cur])) { // 可能是以下单词: "+-*/,)"
				while (Priority(Sign.Peek()) >= Priority(Word[cur])) {
					if (Sign.Peek().equals("(")) {
						if (Word[cur].equals(")")) {
							Sign.Pop();
						}
						break;
					} else if (IsFunc(Sign.Peek())) { // 计算自定义函数
						// double[] parameter=new double[Func.ParameterCount(Sign.Peek())];
						// for(int i=parameter.Length-1;i>=0;i--)
						// {
						// parameter[i]=Val.Pop();
						// }
						Val.Push(Compute(Sign.Peek(), 0, 0));
					} else { // 只计算有 "+-*/" 的情况
						double b = Val.Pop(), a = Val.Pop();
						Val.Push(Compute(Sign.Peek(), a, b));
					}
					Sign.Pop(); // 将当前栈顶已计算过的符号舍弃
				}
				if (!Word[cur].equals(")") && !Word[cur].equals(",")) {
					Sign.Push(Word[cur]);
				}
			} else {
				throw new Exception("表达式包含非法字符: " + Word[cur]);
			}
		}
		while (Sign.Count() > 0) { // 还有未计算完的符号,剩下的符号一定是栈底的优先级最低, 栈顶的最高
			String curSign = Sign.Pop();
			if (curSign.equals("(") || curSign.equals(",")) {
				continue;
			} else if (IsFunc(curSign)) {
				Val.Push(Compute(curSign, 0, 0));
			} else if (IsSign(curSign)) {
				double b = Val.Pop(), a = Val.Pop();
				Val.Push(Compute(curSign, a, b));
			}
		}
		return Val.Pop(); // 最终数值栈只剩一个数值,这就是最终结果.
	}
}