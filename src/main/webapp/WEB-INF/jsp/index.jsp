<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div align="center">
    <form method="post" action="/add_user">
        <table>
            <tr>
                <td>Nombre : </td><td><input type="text" name="first_name"/></td>
            </tr>
            <tr>
                <td>Apellido : </td><td><input type="text" name="last_name"/></td>
            </tr>
            <tr>
                <td>E-mail : </td><td><input type="text" name="email"/></td>
            </tr>
            <tr>
                <td colspan="2" align="left"><input type="submit" value="Guardar Usuario"/></td>
            </tr>
        </table>
    </form>
</div>
<p align="center">
    <c:if test="${users.size() > 0}">
        <table>
            <thead>
                <tr>
                    <th style="border: solid black 1px">id</th>
                    <th style="border: solid black 1px">Nombre</th>
                    <th style="border: solid black 1px">Apellido</th>
                    <th style="border: solid black 1px">E-mail</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td style="border: solid black 1px"><c:out value="${user.id}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.firstName}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.lastName}"/></td>
                        <td style="border: solid black 1px"><c:out value="${user.email}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</p>
</body>
</html>