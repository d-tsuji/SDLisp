package info.gomi.lisp.sdlisp;

public class Toplevel {
	public static void main(String[] args) {
		System.out.println("Welcome to SDLisp! (2017-4-4)");
		System.out.println("> Copyright (C) GOMI Hiroshi 2017.");
		System.out.println("> Type quit and hit Enter for leaving SDLisp.");
		new Function().registSystemFunctions();			// システム関数の登録
		while (true){
			try {
				System.out.print("> ");					// プロンプト表示
				T sexp = Reader.read();					// リーダ
				if (sexp == Symbol.symbolQuit) break;	// quit と入力されれば Lisp 終了
				T ret = Eval.eval(sexp);				// 評価
				System.out.println(ret);				// toString(プリンタ)が呼ばれる
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		System.out.println("bye!");
	}
}