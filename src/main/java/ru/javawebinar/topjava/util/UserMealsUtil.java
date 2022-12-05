package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsToByStream = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByStream.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> result = new ArrayList<>();

        Map<LocalDate, Integer> mealByDaysMap = new HashMap<>();
        for (UserMeal meal : meals) {
            final LocalDate dateKey = meal.getDateTime().toLocalDate();
            if (mealByDaysMap.containsKey(dateKey)) {
                mealByDaysMap.merge(dateKey, meal.getCalories(), Integer::sum);
            } else {
                mealByDaysMap.put(dateKey, meal.getCalories());
            }
        }

        for (UserMeal meal : meals) {
            if (!meal.getDateTime().toLocalTime().isBefore(startTime) &&
                    !meal.getDateTime().toLocalTime().isAfter(endTime)) {
                result.add(new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        mealByDaysMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .sorted(Comparator.comparing(UserMeal::getDateTime))
                .map(dish -> {
                    if(meals.stream()
                            .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(),
                                    Collectors.summingInt(UserMeal::getCalories)))
                            .get(dish.getDateTime().toLocalDate()) > caloriesPerDay)
                        return new UserMealWithExcess(dish.getDateTime(), dish.getDescription(), dish.getCalories(), true);
                    else
                        return new UserMealWithExcess(dish.getDateTime(), dish.getDescription(), dish.getCalories(), false);
                })
                .collect(toList());
    }
}
