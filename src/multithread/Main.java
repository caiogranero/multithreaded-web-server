package multithread;

public class Main {

	public static void main(String[] args) {
		try {
			WebRequest req = new WebRequest(6663);
			while(true){
				req.Listen();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
