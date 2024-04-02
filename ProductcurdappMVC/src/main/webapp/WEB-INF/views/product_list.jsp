<!-- src/main/webapp/WEB-INF/views/product_list.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
</head>
<body>
<h2>Product List</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Action</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.description}</td>
            <td>${product.price}</td>
            <td>
                <a href="<c:url value='/products/view/${product.id}'/>">View</a>
                <a href="<c:url value='/products/delete/${product.id}'/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="<c:url value='/products/add'/>">Add Product</a>
</body>
</html>
