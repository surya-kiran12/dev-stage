let staffData = [];
 
async function fetchData() {
    try {
        const response = await fetch('stfserv');
        if (!response.ok) {
            throw new Error("Failed to fetch staff data");
        }
        const data = await response.json();
        staffData = data;
        renderTable(staffData);
    } catch (error) {
        console.error("Error fetching staff data:", error);
    }
}
 
function renderTable(data) {
    const tableBody = document.getElementById("staffTable");
    tableBody.innerHTML = "";
 
    data.forEach(item => {
        const row = `
            <tr id="row-${item.staff_id}">
                <td>${item.staff_id}</td>
                <td contenteditable="false">${item.name}</td>
                <td>${item.role}</td>
                <td contenteditable="false">${item.department}</td>
                <td contenteditable="false">${item.designation}</td>
                <td>
                    <button type="button" class="edit" onclick="toggleEdit(${item.staff_id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button type="button" class="delete" onclick="deleteStaff(${item.staff_id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>`;
        tableBody.innerHTML += row;
    });
}
 
function applyFilters() {
    const roleFilter = document.getElementById("roleFilter").value.toLowerCase();
    const departmentFilter = document.getElementById("departmentFilter").value.toLowerCase();
    const designationFilter = document.getElementById("designationFilter").value.toLowerCase();
 
    const filteredData = staffData.filter(item =>
        item.role.toLowerCase().includes(roleFilter) &&
        item.department.toLowerCase().includes(departmentFilter) &&
        item.designation.toLowerCase().includes(designationFilter)
    );
 
    renderTable(filteredData);
}
 
function toggleEdit(id) {
    event.preventDefault();

    const row = document.getElementById(`row-${id}`);
    const editButton = row.querySelector(".edit");
    const isEditing = editButton.classList.contains("editing");

    if (isEditing) {
        const updatedData = {
            name: row.cells[1].textContent?.trim(),
            department: row.cells[3].textContent?.trim(),
            designation: row.cells[4].textContent?.trim(),
        };

        saveStaff(id, updatedData);

        row.cells[1].setAttribute("contenteditable", "false");
        row.cells[3].setAttribute("contenteditable", "false");
        row.cells[4].setAttribute("contenteditable", "false");
        editButton.innerHTML = '<i class="fas fa-edit"></i>';
        editButton.classList.remove("editing");
    } else {
        row.cells[1].setAttribute("contenteditable", "true");
        row.cells[3].setAttribute("contenteditable", "true");
        row.cells[4].setAttribute("contenteditable", "true");
        editButton.innerHTML = '<i class="fas fa-save"></i>';
        editButton.classList.add("editing");

        row.cells[1].focus();
    }
}

 
async function saveStaff(id, updatedData) {
    try {
        console.log("Sending update request for staff ID " + id);
        console.log(updatedData); 

        const response = await fetch(`stfserv?action=update&id=${id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });

        if (response.ok) {
            alert("Staff member updated successfully.");
            fetchData(); 
        } else {
            alert("Failed to update staff member.");
        }
    } catch (error) {
        console.error("Error updating staff:", error);
    }
}


 
async function deleteStaff(id) {
    if (confirm("Are you sure you want to delete this staff member?")) {
        try {
            const response = await fetch(`stfserv?action=delete&id=${id}`, { method: 'GET' });
            if (response.ok) {
       
                const row = document.getElementById(`row-${id}`);
                row.remove();
 
                staffData = staffData.filter(item => item.staff_id !== id);
 
                alert("Staff member deleted.");
            } else {
                alert("Failed to delete staff member.");
            }
        } catch (error) {
            console.error("Error deleting staff:", error);
        }
    }
}
 
document.getElementById("roleFilter").addEventListener("input", applyFilters);
document.getElementById("departmentFilter").addEventListener("input", applyFilters);
document.getElementById("designationFilter").addEventListener("input", applyFilters);
 
fetchData();
 
 