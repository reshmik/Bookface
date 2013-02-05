<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- TemplateBeginEditable name="doctitle" -->
<title>PennSocial Login</title>
<!-- TemplateEndEditable -->
<!-- TemplateBeginEditable name="head" -->
<!-- TemplateEndEditable -->
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen" />
<link rel="icon" 
      type="images/favicon.ico" 
      href="images/favicon.ico"/>
<script src="SpryAssets/SpryValidationTextField.js" type="text/javascript"></script>
<script src="SpryAssets/SpryValidationPassword.js" type="text/javascript"></script>
<link href="SpryAssets/SpryValidationTextField.css" rel="stylesheet" type="text/css" />
<link href="SpryAssets/SpryValidationPassword.css" rel="stylesheet" type="text/css" />
</head>

<body class="oneColFixCtrHdr">

<div id="container">
  <div id="header">
    <h1 style="display:none;">PennSocial</h1>
  <!-- end #header --></div>
  <div id="mainContent">

	<form action="Profile" method="post">
     	 <br/>
		<h4>UserName: <input type="text" name="username" size="20" /></h4>
        <h4>Password:  <input type="password" name="password" size="20" /></h4><br/>
        <input type="hidden" name="queryType" value="login" />
        <input type="submit" value="Login" />
	</form>
	<br/>
	<p> New users, <a href="Register.jsp" style="text-decoration:none; color:#666666;">register here</a>! </p>
    
    <h1>
      <!-- end #mainContent -->
    </h1></div>
  <div id="footer">
    <p>&copy;&nbsp;2009 Copyrights reserved</p>
  <!-- end #footer --></div>
<!-- end #container --></div>
<script type="text/javascript">
<!--
var sprytextfield2 = new Spry.Widget.ValidationTextField("sprytextfield2");
var sprytextfield3 = new Spry.Widget.ValidationTextField("sprytextfield3");
var sprypassword1 = new Spry.Widget.ValidationPassword("sprypassword1");
//-->
</script>
</body>
</html>  