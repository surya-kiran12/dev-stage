<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="Crud.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"> 
<script src="Crud.js" defer></script>
</head>
<body>


<form>
   
    <div class="container">
        <aside class="sidebar">
            <h2>(Hospital Name)</h2>
            <nav>
                <ul>
                    <li><a href="#">Dashboard</a></li>
                    <li><a href="#" class="active">Staff</a></li>
                    <li><a href="#">Finances</a></li>
                    <li><a href="#">Inventory</a></li>
                    <li><a href="#">Patients</a></li>
                    <li><a href="userregistration.html">Add Member</a></li>
                </ul>
            </nav>
        </aside>

        <main class="content">
            <header class="header">
                <h1>Staff Overview</h1>
            </header>

            <section class="filter-section">
                <div class="filter-item">
                    <label for="roleFilter">Search by Role</label>
                    <input type="text" id="roleFilter" placeholder="e.g., Doctor, Nurse" class="search-box" oninput="applyFilters()">
                </div>
                <div class="filter-item">
                    <label for="departmentFilter">Search by Department</label>
                    <input type="text" id="departmentFilter" placeholder="e.g., Cardiology, ICU" class="search-box" oninput="applyFilters()">
                </div>
                <div class="filter-item">
                    <label for="designationFilter">Search by Designation</label>
                    <input type="text" id="designationFilter" placeholder="e.g., HOD, Consultant" class="search-box" oninput="applyFilters()">
                </div>
            </section>
            
            
            
               

            <table class="inventory-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Role</th>
                        <th>Department</th>
                        <th>Designation</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="staffTable">
                </tbody>
            </table>
        </main>
    </div>
</form>


 
</body>
</html>