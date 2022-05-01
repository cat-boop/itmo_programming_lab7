package com.lab7.client;

import com.lab7.common.entity.Coordinates;
import com.lab7.common.entity.Location;
import com.lab7.common.entity.Route;
import com.lab7.client.client_exceptions.ReadElementFromScriptException;

import java.util.Locale;
import java.util.Scanner;

/**
 * Class that read new Route from console or from script
 */
public class RouteReader {
    private final Scanner scanner;
    private final boolean isScriptExecuting;

    public RouteReader(Scanner scanner, boolean isScriptExecuting) {
        this.scanner = scanner;
        this.isScriptExecuting = isScriptExecuting;
    }

    /**
     * @return new Route read from console or script
     */
    public Route readRoute() {
        return new Route(readName(), readCoordinates(), readFrom(), readTo(), readDistance());
    }

    /**
     * @return read name of Route from console or script
     */
    public String readName() {
        System.out.print("Введите название маршрута: ");
        String name = scanner.nextLine();
        while (name == null || name.isEmpty()) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Название маршрута не может быть пустым");
            }
            System.out.print("Название маршрута не может быть пустым, повторите попытку: ");
            name = scanner.nextLine();
        }
        return name;
    }

    /**
     * @return read coordinates of Route from console or script
     */
    public Coordinates readCoordinates() {
        final int xMaxValue = 412;
        int x;
        long y;
        System.out.print("Введите координату X маршрута: ");
        x = readInt();
        while (x > xMaxValue) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Координата X не может быть больше 412");
            }
            System.out.print("Координата X не может быть больше 412, повторите попытку: ");
            x = readInt();
        }
        System.out.print("Введите координату Y маршрута: ");
        y = readLong();
        return new Coordinates(x, y);
    }

    /**
     * @return read start location of Route from console or script
     */
    public Location readFrom() {
        System.out.print("Введите координату X точки начала маршрута: ");
        int x = readInt();
        System.out.print("Введите координату Y точки начала маршрута: ");
        int y = readInt();
        System.out.print("Введите координату Z точки начала маршрута: ");
        double z = readDouble();
        System.out.print("Введите название начальной локации маршрута: ");
        String name = scanner.nextLine();
        while (name == null || name.isEmpty()) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Имя начальной локации не может быть пустым");
            }
            System.out.print("Имя начальной локации не может быть пустым, повторите попытку: ");
            name = scanner.nextLine();
        }
        return new Location(x, y, z, name);
    }

    /**
     * @return read end location of Route from console or script
     */
    public Location readTo() {
        String response;
        while (true) {
            System.out.print("Известна ли конечная точка маршрута? (y/n): ");
            response = scanner.nextLine().toLowerCase(Locale.ROOT);
            if ("n".equals(response)) {
                return null;
            } else if ("y".equals(response)) {
                System.out.print("Введите координату X точки конца маршрута: ");
                int x = readInt();
                System.out.print("Введите координату Y точки конца маршрута: ");
                int y = readInt();
                System.out.print("Введите координату Z точки конца маршрута: ");
                double z = readDouble();
                System.out.print("Введите название конечной локации маршрута: ");
                String name = scanner.nextLine();
                while (name == null || name.isEmpty()) {
                    if (isScriptExecuting) {
                        throw new ReadElementFromScriptException("Имя конечной локации не может быть пустым");
                    }
                    System.out.print("Имя конечной локации не может быть пустым, повторите попытку: ");
                    name = scanner.nextLine();
                }
                return new Location(x, y, z, name);
            } else if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Ошибка при считывании конечной локации");
            }
        }
    }

    /**
     * @return read distance of Route from console or script
     */
    public double readDistance() {
        System.out.print("Введите дистанцию маршрута: ");
        double value = readDouble();
        while (Double.compare(value, 1) <= 0) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Дистанция должна быть больше 1");
            }
            System.out.print("Дистанция должна быть больше 1, повторите попытку: ");
            value = readDouble();
        }
        return value;
    }

    private int readInt() {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                if (isScriptExecuting) {
                    throw new ReadElementFromScriptException("Ошибка при вводе числа типа int");
                }
                System.out.print("Ошибка при вводе числа типа int, повторите попытку: ");
            }
        }
        return value;
    }

    private long readLong() {
        long value;
        while (true) {
            try {
                value = Long.parseLong(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                if (isScriptExecuting) {
                    throw new ReadElementFromScriptException("Ошибка при вводе числа типа long");
                }
                System.out.print("Ошибка при вводе типа long, повторите попытку: ");
            }
        }
        return value;
    }

    private double readDouble() {
        double value;
        while (true) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                if (isScriptExecuting) {
                    throw new ReadElementFromScriptException("Ошибка при вводе числа типа double");
                }
                System.out.print("Ошибка при вводе числа типа double, повторите попытку: ");
            }
        }
        return value;
    }
}
