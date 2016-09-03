public class Main {

	public static void main(String[] args) {
		try {
			WebRequest req = new WebRequest(Integer.parseInt(args[0]));
			while(true){
				req.Listen();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
