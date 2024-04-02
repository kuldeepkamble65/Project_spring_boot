<!-- src/main/webapp/WEB-INF/views/view_product.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Product</title>
</head>
<body>
<h2>Product Details</h2>
<p>ID: ${product.id}</p>
<p>Name: ${product.name}</p>
<p>Description: ${product.description}</p>
<p>Price: ${product.price}</p>
<a href="<c:url value='/products/list'/>">Back to Product List</a>
</body>
</html>
