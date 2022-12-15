<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table, td, th {
            border: 2px solid gray;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>



<div>
    <table>
        <thead>
            <tr>
                <th scope="col">№</th>
                <th scope="col">Описание</th>
                <th scope="col">Дата</th>
                <th scope="col">Каллории</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="meal" items="${meals}" varStatus="position">
                <tr style="${meal.excess ? 'color: #f00' : 'color: #00f'}">
                    <td> ${position.count} </td>
                    <td> ${meal.description} </td>

                    <td> ${meal.date} ${meal.time} </td>
                    <td> ${meal.calories} </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</div>

</body>
</html>