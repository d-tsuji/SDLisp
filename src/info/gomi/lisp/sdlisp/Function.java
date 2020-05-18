package info.gomi.lisp.sdlisp;

public class Function extends Atom {
	public T function;	// システム関数もS式関数もここに格納
	
	/**　関数オブジェクトの文字列化（シリアライズ）　*/
	public String toString() {
		// S式関数は単なるリスト(Cons)なので、Cons の toString が呼ばれる
		return "#<SYSTEM-FUNCTION " + this.getClass().getSimpleName() + ">";
	}

	/**
	 * 関数呼び出しのテンプレートメソッド
	 * @param arguments 引数リスト
	 * @throws Exception Lisp関数の各種エラー
	 */
	public T funcall(List arguments) throws Exception {
		return Null.Nil;
	}

	/**
	 * システム関数の定義一覧
	 */
	public void registSystemFunctions() {
		regist("CAR", new Car());
		regist("CDR", new Cdr());
		regist("CONS", new FunCons());
		regist("EQ", new Eq());
		regist("+", new Add());
		regist("-", new Sub());
		regist("*", new Mul());
		regist("/", new Div());
		regist(">=", new Ge());
		regist("<=", new Le());
		regist(">", new Gt());
		regist("<", new Lt());
		regist("=", new NumberEqual());
		regist("QUOTE", new Quote());
		regist("SETQ", new Setq());
		regist("DEFUN", new Defun());
		regist("IF", new If());
		regist("TYPE-OF", new TypeOf());
		regist("SYMBOL-FUNCTION", new SymbolFunction());		
	}

	/**
	 * システム関数の登録 regist
	 * @param name 関数名
	 * @param fun 関数クラス
	 */
	private void regist(String name, Function fun) {
		Symbol sym = Symbol.symbol(name);
		sym.function = fun;
	}

	/** CAR */
	class Car extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			return arg1 == Null.Nil ? Null.Nil : ((Cons)arg1).car;
		}
	}

	/** CDR */
	class Cdr extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			return arg1 == Null.Nil ? Null.Nil : ((Cons)arg1).cdr;
		}
	}
	
	/** CONS */
	class FunCons extends Function {
		public T funcall(List arguments) throws Exception {			// 引数はリストで受ける
			T arg1 = Eval.eval(((Cons)arguments).car);				// 第1引数を評価
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);	// 第2引数を評価
			return new Cons(arg1, arg2);
		}
	}

	/** EQ */
	class Eq extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			if (arg1 == arg2) return Symbol.symbolT;
			else return Null.Nil;
		}
	}

	/** + */
	class Add extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).add((Integer)arg2);
		}
	}

	/** - */
	class Sub extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).sub((Integer)arg2);
		}
	}

	/** * */
	class Mul extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).mul((Integer)arg2);
		}
	}

	/** / */
	class Div extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).div((Integer)arg2);
		}
	}

	/** >= */
	class Ge extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).ge((Integer)arg2);
		}
	}

	/** <= */
	class Le extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).le((Integer)arg2);
		}
	}

	/** > */
	class Gt extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).gt((Integer)arg2);
		}
	}

	/** < */
	class Lt extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).lt((Integer)arg2);
		}
	}

	/** = */
	class NumberEqual extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			return ((Integer)arg1).numberEqual((Integer)arg2);
		}
	}

	/** QUOTE */
	class Quote extends Function {
		public T funcall(List arguments) throws Exception {
			return ((Cons)arguments).car;
		}
	}

	/** SETQ */
	class Setq extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = ((Cons)arguments).car;
			T arg2 = Eval.eval(((Cons)((Cons)arguments).cdr).car);
			Symbol sym = (Symbol)arg1;
			T value = Eval.eval(arg2);
			sym.value = value;
			return value;
		}
	}

	/** DEFUN */
	class Defun extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = ((Cons)arguments).car;
			T args = ((Cons)arguments).cdr;
			Symbol fun = (Symbol)arg1;
			Cons lambda = new Cons();
			lambda.car = Symbol.symbol("LAMBDA");
			lambda.cdr = args;
			fun.function = lambda;
			return fun;
		}
	}

	/** IF */
	class If extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = ((Cons)arguments).car;
			T args = ((Cons)arguments).cdr;
			T arg2 = ((Cons)args).car;
			T arg3 = (((Cons)args).cdr == Null.Nil) ? Null.Nil : ((Cons)((Cons)args).cdr).car;
			if (Eval.eval(arg1) != Null.Nil)
				return Eval.eval(arg2);
			else
				return Eval.eval(arg3);
		}
	}
	
	/** TYPE-OF */
	class TypeOf extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			java.lang.String type = arg1.getClass().getSimpleName().toUpperCase();
			return Symbol.symbol(type);
		}
	}
	
	/** SYMBOL-FUNCTION */
	class SymbolFunction extends Function {
		public T funcall(List arguments) throws Exception {
			T arg1 = Eval.eval(((Cons)arguments).car);
			return ((Symbol)arg1).function;
		}
	}

}