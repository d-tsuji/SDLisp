package info.gomi.lisp.sdlisp;

/** S式の評価 EVAL */
public class Eval {
	private static final int maxStackSize = 65536;	// Lisp スタックサイズ
	private static T[] stack = new T[maxStackSize];	// Lisp スタック
	private static int stackP = 0;					// Lisp スタックポインタ

	/** 与えられた S式を評価する Eval.eval(Sexp) */
	public static T eval(T form) throws Exception {
		// シンボルのときの評価
		if (form instanceof Symbol) {
			T symbolValue = ((Symbol)form).value;
			if (symbolValue == null) throw new Exception("Unbound Variable Error: " + (Symbol)form);
			return symbolValue;
		}
		// シンボル以外のアトムのときの評価
		if (form instanceof Null) return form;		
		if (form instanceof Atom) return form;
		// リストの評価（関数の評価）
		T car = ((Cons)form).car;
		if (car instanceof Symbol) {
			// システム関数は内部クラスを用いて O(1) で検索
			T fun = ((Symbol)car).function;
			if (fun == null) throw new Exception("Undefined Function Error: " + (Symbol)car);
			// システム関数の評価
			if (fun instanceof Function) {
				T argumentList = ((Cons)form).cdr;
				return ((Function)fun).funcall((List)argumentList);
			}
			// S式関数の評価
			if (fun instanceof Cons) {
				Cons cdr = (Cons)((Cons)fun).cdr;
				T lambdaList = cdr.car;
				Cons body = (Cons)cdr.cdr;
				// 引数がないときは、束縛せずに本体 body をそのまま評価
				if (lambdaList == Null.Nil) return evalBody(body);
				// 引数があるときは、引数評価と束縛、本体 body を評価
				return bindEvalBody((Cons)lambdaList, body, (Cons)((Cons)form).cdr);
			}
			throw new Exception("Not a Function: " + fun);
		}
		// car 部がシンボルでないとき
		throw new Exception("Not a Symbol: " + car);
	}

	/**
	 * S式定義関数の評価 sexpEval
	 * @param lambda ラムダリスト（束縛変数） 例. (x y)
	 * @param body S式関数本体 例. (+ x y)
	 * @param form 評価するS式 例. (add 10 20)
	 * @return 評価した結果
	 * @throws Exception Lispの各種エラー（未束縛の変数、関数定義がないなど）
	 */
	private static T bindEvalBody(Cons lambda, Cons body, Cons form) throws Exception {
		// (1) 束縛前の環境で引数評価(評価した値の格納場所に一時的にスタックを使用)
		int OldStackP = stackP;
		while (true) {
			T ret = eval(form.car);
			stack[stackP++] = ret;
			if (form.cdr == Null.Nil) break;
			form = (Cons)form.cdr;
		}
		// (2) 束縛(シンボルの過去の値をスタックに退避し(1)で評価した値をシンボルに入れる(スワップ))
		Cons argList = lambda;
		int sp = OldStackP;
		while (true) {
			Symbol sym = (Symbol)argList.car;
			T swap = sym.value;
			sym.value = stack[sp];
			stack[sp++] = swap;
			if (argList.cdr == Null.Nil) break;
			argList = (Cons)argList.cdr;
		}
				
		// body の評価
		T ret = evalBody(body);
		
		// スタックから前の値に戻す
		argList = lambda;
		stackP = OldStackP;
		while (true) {
			Symbol sym = (Symbol)argList.car;
			sym.value = stack[OldStackP++];
			if (argList.cdr == Null.Nil) break;
			argList = (Cons)argList.cdr;
		}
		// 値を返す
		return ret;
	}

	/**
	* 本体の評価
	*/
	private static T evalBody(Cons body) throws Exception {
		T ret;
		while (true) {
			ret = eval(body.car);
			if (body.cdr == Null.Nil) break;
			body = (Cons)body.cdr;
		}
		return ret;
	}
}