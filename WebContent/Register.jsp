<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- TemplateBeginEditable name="doctitle" -->
<title>Registration</title>
<!-- TemplateEndEditable -->
<!-- TemplateBeginEditable name="head" -->
<!-- TemplateEndEditable -->

<link rel="stylesheet" type="text/css" href="css/style.css" media="screen" />

<script src="SpryAssets/SpryValidationSelect.js" type="text/javascript"></script>
<script src="SpryAssets/SpryValidationTextField.js" type="text/javascript"></script>
<script src="SpryAssets/SpryValidationPassword.js" type="text/javascript"></script>
<script src="SpryAssets/SpryValidationConfirm.js" type="text/javascript"></script>
<link href="SpryAssets/SpryValidationSelect.css" rel="stylesheet" type="text/css" />
<link href="SpryAssets/SpryValidationTextField.css" rel="stylesheet" type="text/css" />
<link href="SpryAssets/SpryValidationPassword.css" rel="stylesheet" type="text/css" />
<link href="SpryAssets/SpryValidationConfirm.css" rel="stylesheet" type="text/css" />
</head>

<body class="oneColFixCtrHdr">

<div id="container">
  <div id="header">
    <h1 style="display:none;">PennSocial</h1>
  <!-- end #header --></div>
  <div id="mainContent">
	<p style="margin:0;" align="center">&nbsp;</p>
    <h2 align="center">Registration </h2>
    <form id="form1" name="form1" method="post" action="Create">
      <label>
        <div align="center">
         
          <p><span id="sprypassword1">
            <label>Password&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <input type="password" name="password" id="password" />
            </label>
          <span class="passwordRequiredMsg">A value is required.</span></span></p>
          <p><span id="spryconfirm1">
            <label>Confirm Password&nbsp;&nbsp;
              <input type="password" name="confirm" id="confirm" />
            </label>
          <span class="confirmRequiredMsg">A value is required.</span><span class="confirmInvalidMsg">The values don't match.</span></span></p>
          <p><span id="sprytextfield2">
            <label>First Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <input type="text" name="firstname" id="firstname" />
            </label>
          <span class="textfieldRequiredMsg">A value is required.</span></span></p>
          <p><span id="sprytextfield3">
            <label>Last Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <input type="text" name="lastname" id="lastname" />
            </label>
          <span class="textfieldRequiredMsg">A value is required.</span></span></p>
          <p><span id="sprytextfield4">
          <label>Email
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="text" name="email" id="email" />
          </label>
          <span class="textfieldRequiredMsg">A value is required.</span><span class="textfieldInvalidFormatMsg">Invalid format.</span></span></p>
          
          <p><span id="sprytextfield88">
          <label>Address
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="text" name="address" id="address" />
          </label>
          <span class="textfieldRequiredMsg">A value is required.</span><span class="textfieldInvalidFormatMsg">Invalid format.</span></span></p>
          
          <p><span id="sprytextfield12">
          <label>Birthdate&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
            <input type="text" name="birthdate" id="birthdate" />
          </label>
          <span class="textfieldRequiredMsg">A value is required.</span><span class="textfieldInvalidFormatMsg">Invalid format.</span></span></p>
          <p><span id="spryselect1">
            <label>Gender&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;
            <select name="gender" id="gender">
              <option selected="selected">Select Gender</option>
              <option>Female</option>
              <option>Male</option>
              <option>Other</option>
            </select>
            </label>
          <span class="selectInvalidMsg">Please select a valid item.</span></span></p>
          
          <strong>Education</strong><br />
            <br />
            School Name
            <span id="sprytextfield7">
            <input name="school" type="text" id="school" size="64" maxlength="64" />
            <span class="textfieldRequiredMsg">A value is required.</span></span> <br />
            <br />
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Degree <span id="sprytextfield8">
           <input type="text" name="degree" id="degree" />
           <span class="textfieldRequiredMsg">A value is required.</span></span> <br />
            <br />
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Start Date
            <span id="sprytextfield9">
            <input type="text" name="startDate" id="startDate" />
          <span class="textfieldRequiredMsg">A value is required.</span><span class="textfieldInvalidFormatMsg">Invalid format.</span></span> &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;</p>
          <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; End Date&nbsp; <span id="sprytextfield10">
          <input type="text" name="endDate" id="endDate" />
          <span class="textfieldRequiredMsg">A value is required.</span><span class="textfieldInvalidFormatMsg">Invalid format.</span></span> <br />
          
          <p><br />
            <label>
               &nbsp;
               <input type="reset" name="clearfeild" id="clearfeild" value="Clear All" />
            </label>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" name="submit" id="submit" value="Submit" />
          </p>
         <input type="hidden" name="queryType" value="profile" />
      </div>
      </label>
    </form>
    <p>&nbsp;</p>
  <!-- end #mainContent --></div>
  <div id="footer">
    <p>&copy;&nbsp;2009 Copyrights reserved</p>
  <!-- end #footer --></div>
<!-- end #container --></div>
<script type="text/javascript">
<!--
var spryselect1 = new Spry.Widget.ValidationSelect("spryselect1", {isRequired:true, invalidValue:"Select Gender"});
var sprytextfield1 = new Spry.Widget.ValidationTextField("username");
var sprypassword1 = new Spry.Widget.ValidationPassword("sprypassword1");
var sprytextfield2 = new Spry.Widget.ValidationTextField("sprytextfield2");
var sprytextfield3 = new Spry.Widget.ValidationTextField("sprytextfield3");
var sprytextfield4 = new Spry.Widget.ValidationTextField("sprytextfield4", "email");
var sprytextfield5 = new Spry.Widget.ValidationTextField("sprytextfield5", "none");
var sprytextfield6 = new Spry.Widget.ValidationTextField("sprytextfield6");
var sprytextfield7 = new Spry.Widget.ValidationTextField("sprytextfield7");
var sprytextfield8 = new Spry.Widget.ValidationTextField("sprytextfield8");
var sprytextfield9 = new Spry.Widget.ValidationTextField("sprytextfield9", "date", {format:"mm/dd/yyyy", hint:" mm/dd/yyyy"});
var sprytextfield10 = new Spry.Widget.ValidationTextField("sprytextfield10", "date", {hint:"mm/dd/yyyy", format:"mm/dd/yyyy"});
var spryconfirm1 = new Spry.Widget.ValidationConfirm("spryconfirm1", "password");
var sprytextfield11 = new Spry.Widget.ValidationTextField("sprytextfield11", "integer");
var spryselect2 = new Spry.Widget.ValidationSelect("spryselect2", {invalidValue:"Status"});
var sprytextfield12 = new Spry.Widget.ValidationTextField("sprytextfield12", "date", {hint:"mm/dd/yyyy", format:"mm/dd/yyyy"});
var sprytextfield8 = new Spry.Widget.ValidationTextField("sprytextfield88");
var sprytextfield8 = new Spry.Widget.ValidationTextField("sprytextfield89");
//-->
</script>
</body>
</html>