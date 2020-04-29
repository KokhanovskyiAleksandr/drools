package com;

import cars.Car;
import cars.SupportedMarks;
import exception.UnsupportedCarException;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static cars.SupportedMarks.MAZDA;
import static cars.SupportedMarks.TOYOTA;

public class DroolsTest {
    private static WorkingMemory workingMemory;
    private static Properties properties = new Properties();
    private static ClassLoader classLoader = DroolsTest.class.getClassLoader();
    private static List<Car> cars = new LinkedList<>();


    @BeforeClass
    public static void beforeClass() throws IOException, DroolsParserException {
        properties.load(classLoader.getResourceAsStream("application.properties"));

        setUpDrool();
        crateProducts();
        addRules();
    }

    private static void addRules() {
        Arrays.stream(SupportedMarks.values()).forEach(mark->workingMemory.insert(mark));
        cars.forEach(car -> workingMemory.insert(car));
        workingMemory.fireAllRules();
    }

    private static void crateProducts() {
        cars.add(new Car(TOYOTA));
        cars.add(new Car(MAZDA));
    }

    private static void setUpDrool() throws IOException, DroolsParserException {
        PackageBuilder packageBuilder = new PackageBuilder();

        InputStream resourceAsStream = classLoader.getResourceAsStream(properties.getProperty("drools.rules"));

        assert resourceAsStream != null;
        Reader reader = new InputStreamReader(resourceAsStream);
        packageBuilder.addPackageFromDrl(reader);
        org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(rulesPackage);

        workingMemory = ruleBase.newStatefulSession();
    }

    @Test
    public void rulesTest() {
        cars.forEach(car -> {
            try {
                SupportedMarks mark = car.getMark();
                int price = getPrice(mark);
                Assert.assertEquals(price, car.getPrice());
            } catch (UnsupportedCarException e) {
                e.printStackTrace();
            }
        });
    }

    private int getPrice(SupportedMarks mark) throws UnsupportedCarException {
        switch (mark) {
            case TOYOTA:
                return Integer.parseInt(properties.getProperty("drools.rules.mark.toyota.price"));
            case MAZDA:
                return Integer.parseInt(properties.getProperty("drools.rules.mark.mazda.price"));
            default:
                throw new UnsupportedCarException(mark + "is't supported yet");
        }

    }
}
