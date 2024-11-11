package design;

enum SingletonEnum {
    INSTANCE;

    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
public class EnumSingletonDemo {
	public static void main(String[] args) {
        SingletonEnum singleton = SingletonEnum.INSTANCE;

        System.out.println(singleton.getValue());
        singleton.setValue(2);
        SingletonEnum singleton1 = SingletonEnum.INSTANCE;
        System.out.println(singleton1.getValue());
    }
}
