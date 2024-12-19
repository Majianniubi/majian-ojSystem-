
public class MainApplication {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b =Integer.parseInt(args[1]);
        System.out.println(a+b);
    }
}
//    docker run -d --name redis-container -p 6379:6379 -v /home/majian/redis-data:/data --restart=always redis redis-server --appendonly yes -requirepass 123456